package lycanitestweaks.block.fluid;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.block.BlockFluidBase;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidMixHelper {

    // Up to 1 Self OR up to 5 Neighbor in one tick
    public enum MixTarget {
        SELF,     // Replaces the fluid block running the check
        NEIGHBOR  // Replaces the adjacent fluid block that was touched
    }

    public static class MixResult {
        public final MixTarget target;
        public final IBlockState sourceResult;
        public final IBlockState flowingResult;
        public final SoundEvent soundEvent;

        public MixResult(MixTarget target, IBlockState sourceResult, IBlockState flowingResult, ResourceLocation soundID) {
            this(target, sourceResult, flowingResult, soundID == null ? null : ForgeRegistries.SOUND_EVENTS.getValue(soundID));
        }

        public MixResult(MixTarget target, IBlockState sourceResult, IBlockState flowingResult, SoundEvent soundEvent) {
            this.target = target;
            this.sourceResult = sourceResult;
            this.flowingResult = flowingResult;
            this.soundEvent = soundEvent;
        }
    }

    // Registry is sorted so the start SELF entries are processed first, order matters since SELF transformation cancels the Neighbor
    private static final Map<Fluid, Map<Fluid, List<MixResult>>> MIXTURES = new HashMap<>();

    public static void registerMixture(Fluid selfFluid, Fluid touchingFluid, MixTarget target, IBlockState sourceOut, IBlockState flowingOut, ResourceLocation soundID) {
        List<MixResult> list = MIXTURES.computeIfAbsent(selfFluid, k -> new HashMap<>())
                .computeIfAbsent(touchingFluid, k -> new ArrayList<>());
        list.add(target == MixTarget.SELF ? 0 : list.size(), new MixResult(target, sourceOut, flowingOut, soundID));
    }

    public static void registerMixture(Fluid selfFluid, Fluid touchingFluid, MixTarget target, IBlockState sourceOut, IBlockState flowingOut, SoundEvent soundEvent) {
        List<MixResult> list = MIXTURES.computeIfAbsent(selfFluid, k -> new HashMap<>())
                .computeIfAbsent(touchingFluid, k -> new ArrayList<>());
        list.add(target == MixTarget.SELF ? 0 : list.size(), new MixResult(target, sourceOut, flowingOut, soundEvent));
    }

    private static void registerLycanitesStyleMix(Fluid selfFluid, Fluid touchingFluid, IBlockState result, SoundEvent soundEvent) {
        if(!areDifferentFluids(selfFluid, touchingFluid)) {
            return;
        }
        if(!ForgeConfigHandler.mixinPatchesConfig.fixFluidsSoundEvents) {
            soundEvent = null;
        }
        registerMixture(selfFluid, touchingFluid, MixTarget.NEIGHBOR, result, result, soundEvent);
    }

    private static void registerLycanitesReverseMix(Fluid selfFluid, Fluid touchingFluid, IBlockState sourceOut, IBlockState flowingOut, SoundEvent soundEvent) {
        if(!ForgeConfigHandler.mixinPatchesConfig.fixFluidsReverseMixtures) {
            return;
        }
        if(!areDifferentFluids(selfFluid, touchingFluid)) {
            return;
        }
        if(!ForgeConfigHandler.mixinPatchesConfig.fixFluidsSoundEvents) {
            soundEvent = null;
        }
        registerMixture(selfFluid, touchingFluid, MixTarget.SELF, sourceOut, flowingOut, soundEvent);
    }

    public static List<MixResult> getMixResults(Fluid self, Block neighborBlock) {
        Fluid neighborFluid = FluidRegistry.lookupFluidForBlock(neighborBlock);
        if (neighborFluid == null || !MIXTURES.containsKey(self)) {
            return Collections.emptyList();
        }
        return MIXTURES.get(self).getOrDefault(neighborFluid, Collections.emptyList());
    }

    public static boolean isFluidSource(World world, BlockPos pos, IBlockState state) {
        Block block = state.getBlock();
        if (block instanceof BlockFluidClassic) {
            return ((BlockFluidClassic) block).isSourceBlock(world, pos);
        } else if (block instanceof BlockLiquid) {
            return state.getValue(BlockLiquid.LEVEL) == 0;
        }
        return false;
    }

    // Prevent the auto registry from defining mixtures between fluid variants, config can override
    private static boolean areDifferentFluids(Fluid left, Fluid right) {
        if(left == right) return false;

        if(ForgeConfigHandler.mixinPatchesConfig.fixFluidsDisableVariantMixtures) {
            if(left.getBlock() instanceof BlockFluidBase)
                if(left.getBlock().getClass().isInstance(right.getBlock())) return false;
            if(right.getBlock() instanceof BlockFluidBase)
                if(right.getBlock().getClass().isInstance(left.getBlock())) return false;
        }

        return true;
    }

    public static void initLycanitesFluidSounds() {
        if(!ForgeConfigHandler.mixinPatchesConfig.fixFluidsSoundEvents)
            return;

        ModInfo group = LycanitesMobs.modInfo;
        AssetManager.addSound("ooze", group, "block.ooze");
        AssetManager.addSound("rabbitooze", group, "block.rabbitooze");
        AssetManager.addSound("moglava", group, "block.moglava");
        AssetManager.addSound("acid", group, "block.acid");
        AssetManager.addSound("sharacid", group, "block.sharacid");
        AssetManager.addSound("poison", group, "block.poison");
        AssetManager.addSound("vesspoison", group, "block.vesspoison");
//        AssetManager.addSound("veshoney", group, "block.veshoney"); // I can't think of a sound to reuse
    }

    // TODO BetterConfig
    public static void initLycanitesFluidBehaviors() {
        MIXTURES.clear();

        Collection<Fluid> lavaLiquids = new ArrayList<>();
        Collection<Fluid> waterLiquids = new ArrayList<>();
        FluidRegistry.getRegisteredFluids().values().forEach(liquid -> {
            if(liquid.getBlock() != null) {
                Material material = liquid.getBlock().getDefaultState().getMaterial();
                if(material == Material.LAVA) {
                    lavaLiquids.add(liquid);
                }
                else if(material == Material.WATER) {
                    waterLiquids.add(liquid);
                }
            }
        });

        ObjectManager.fluids.forEach((fluidName, fluid) -> {
            switch (fluidName) {
                case "ooze":
                case "rabbitooze":
                    waterLiquids.forEach(water -> {
                        IBlockState blockState = Blocks.PACKED_ICE.getDefaultState();
                        registerLycanitesStyleMix(fluid, water, blockState, SoundEvents.BLOCK_GLASS_BREAK);
                        registerLycanitesReverseMix(fluid, water, blockState, blockState, SoundEvents.BLOCK_GLASS_BREAK);
                    });
                    lavaLiquids.forEach(lava -> {
                        registerLycanitesStyleMix(fluid, lava, Blocks.OBSIDIAN.getDefaultState(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        registerLycanitesReverseMix(fluid, lava, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
                    });
                    break;
                case "moglava":
                    waterLiquids.forEach(water -> {
                        registerLycanitesStyleMix(fluid, water, Blocks.STONE.getDefaultState(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        registerLycanitesReverseMix(fluid, water, Blocks.OBSIDIAN.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
                    });
                    break;
                case "acid":
                case "sharacid":
                    waterLiquids.forEach(water -> {
                        IBlockState blockState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
                        registerLycanitesStyleMix(fluid, water, blockState, SoundEvents.BLOCK_STONE_BREAK);
                        registerLycanitesReverseMix(fluid, water, blockState, blockState, SoundEvents.BLOCK_STONE_BREAK);
                    });
                    lavaLiquids.forEach(lava -> {
                        IBlockState blockState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);
                        registerLycanitesStyleMix(fluid, lava, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        registerLycanitesReverseMix(fluid, lava, blockState, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                    });
                    break;
                case "poison":
                case "vesspoison":
                    waterLiquids.forEach(water -> {
                        IBlockState blockState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
                        registerLycanitesStyleMix(fluid, water, blockState, SoundEvents.BLOCK_STONE_BREAK);
                        registerLycanitesReverseMix(fluid, water, blockState, blockState, SoundEvents.BLOCK_STONE_BREAK);
                    });
                    lavaLiquids.forEach(lava -> {
                        IBlockState blockState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);
                        registerLycanitesStyleMix(fluid, lava, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        registerLycanitesReverseMix(fluid, lava, blockState, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                    });
                    break;
                case "veshoney":
                    waterLiquids.forEach(water -> {
                        IBlockState blockState = Blocks.DIRT.getDefaultState();
                        registerLycanitesStyleMix(fluid, water, Blocks.DIRT.getDefaultState(), SoundEvents.BLOCK_GRAVEL_BREAK);
                        registerLycanitesReverseMix(fluid, water, blockState, blockState, SoundEvents.BLOCK_GRAVEL_BREAK);
                    });
                    lavaLiquids.forEach(lava -> {
                        if(areDifferentFluids(fluid, lava)) {
                            IBlockState blockState = Blocks.COBBLESTONE.getDefaultState();
                            registerLycanitesStyleMix(fluid, lava, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                            registerLycanitesReverseMix(fluid, lava, blockState, blockState, SoundEvents.BLOCK_FIRE_EXTINGUISH);
                        }
                    });
                    break;
            }
        });
    }
}
