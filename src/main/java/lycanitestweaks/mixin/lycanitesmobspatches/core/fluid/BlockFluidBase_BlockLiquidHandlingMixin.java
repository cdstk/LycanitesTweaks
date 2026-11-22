package lycanitestweaks.mixin.lycanitesmobspatches.core.fluid;

import com.lycanitesmobs.core.block.BlockFluidBase;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.block.fluid.FluidMixHelper;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

@Mixin(BlockFluidBase.class)
public abstract class BlockFluidBase_BlockLiquidHandlingMixin extends BlockFluidClassic {

    @Shadow(remap = false) public String blockName;

    public BlockFluidBase_BlockLiquidHandlingMixin(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBlockFluidBase_initSetTickRandomly(Fluid fluid, Material material, ModInfo group, String blockName, CallbackInfo ci){
        this.setHardness(100.0F);
        this.setTickRandomly(false);

        // Fire Tick
        if (material == Material.LAVA) {
            this.setTickRandomly(true);
        }

        // TODO Better Config
        switch (this.blockName) {
            case "moglava":
            case "rabbitooze":
            case "sharacid":
            case "vesspoison":
                this.canCreateSources = true;
        }
    }

    @Unique
    @Override
    protected boolean canFlowInto(IBlockAccess world, BlockPos pos) {
        return super.canFlowInto(world, pos) || this.material == world.getBlockState(pos).getMaterial();
    }

    @Unique
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        int result = super.getLightValue(state, world, pos);
        if(ForgeConfigHandler.mixinPatchesConfig.fixFluidsReduceLightUpdates) {
            // Source blocks (LEVEL == 0) emit full light (15).
            // Flowing blocks emit a constant light (e.g., 12 or 0) so changing LEVEL from 1->2->3
            // Reduces but does not eliminate the massive TPS strain from block light updates
            if(!this.isSourceBlock(world, pos)) {
                result = Math.min(12, result);
            }
        }
        return result;
    }

    @Unique
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.lycanitesTweaks$checkForMixing(worldIn, pos, state)) {
            super.onBlockAdded(worldIn, pos, state);
            // If self exists/wasn't transformed
            if (!ForgeConfigHandler.mixinPatchesConfig.fixFluidsContactMixVanilla) {
                this.lycanitesTweaks$checkAreaForMix(worldIn, pos);
            }
        }
    }

    @Unique
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.lycanitesTweaks$checkForMixing(worldIn, pos, state)) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
    }

    @Unique
    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        if (!world.isAreaLoaded(pos,4)) return;

        // BlockDynamicLiquid.updateTick, only does Lyca renewable in the air
        this.lycanitesTweaks$updateTickDynamicLiquid(world, pos, state, rand);
        // BlockFluidClassic.updateTick, will schedule Block Updates
        super.updateTick(world, pos, state, rand);
        // BlockStaticLiquid.updateTick, only does nearby burnable fire tick
        this.lycanitesTweaks$updateTickStaticLiquid(world, pos, state, rand);
    }

    // TODO BetterConfig move to super class to modify other fluids
    /**
     * @return Skip block update
     */
    @Unique
    private boolean lycanitesTweaks$checkForMixing(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return false;

        Fluid selfFluid = this.getFluid();
        if (selfFluid == null) return false;

        for(int i = EnumFacing.values().length - 1; i >= 0; i--) {
            EnumFacing facing = EnumFacing.values()[i];
            BlockPos neighborPos = pos.offset(facing);
            IBlockState neighborState = world.getBlockState(neighborPos);
            List<FluidMixHelper.MixResult> results = FluidMixHelper.getMixResults(selfFluid, neighborState.getBlock());

            for (FluidMixHelper.MixResult mix : results) {
                // Vanilla behavior, do not transform self when above neighbor target
                if(facing == EnumFacing.DOWN) {
                    if(mix.target == FluidMixHelper.MixTarget.SELF) {
                        continue;
                    }
                }
                // Vanilla behavior, do not transform neighbor when below
                else if(facing == EnumFacing.UP) {
                    if(mix.target == FluidMixHelper.MixTarget.NEIGHBOR) {
                        continue;
                    }
                }

                boolean isNeighborTarget = (mix.target == FluidMixHelper.MixTarget.NEIGHBOR);
                BlockPos targetPos = isNeighborTarget ? neighborPos : pos;
                IBlockState targetState = isNeighborTarget ? neighborState : state;

                boolean isSource = FluidMixHelper.isFluidSource(world, targetPos, targetState);
                IBlockState resultState = isSource ? mix.sourceResult : mix.flowingResult;

                if (resultState != null) {
                    resultState = ForgeEventFactory.fireFluidPlaceBlockEvent(world, targetPos, pos, resultState);
                    world.setBlockState(targetPos, resultState);

                    if (mix.soundEvent != null) {
                        Material effectMaterial = state.getMaterial() == Material.LAVA
                                ? state.getMaterial()
                                : neighborState.getMaterial();
                        this.lycanitesTweaks$triggerMixEffects(world, targetPos, mix.soundEvent, effectMaterial);
                    }

                    // Only cancel the caller's tick/placement if THIS block was replaced
                    if (!isNeighborTarget) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Looks ridiculous but meant to copy BlockFluidClassic.getOptimalFlowDirections
     * Lycanites overrides canFlowInto, which get called for an area via getOptimalFlowDirections
     */
    @Unique
    private void lycanitesTweaks$checkAreaForMix(World world, BlockPos pos) {
        for (int side = 0; side < 4; side++) {
            BlockPos flowPos = pos.offset(SIDES.get(side));

            if (!canFlowInto(world, flowPos) || isSourceBlock(world, flowPos)) {
                continue;
            }

            this.lycanitesTweaks$checkForMixing(world, flowPos, this.getDefaultState());
            if (!canFlowInto(world, flowPos.up(densityDir))) {
                this.lycanitesTweaks$checkFlowForMix(world, flowPos, 1, side);
            }
        }
    }

    /**
     * Looks ridiculous but meant to copy BlockFluidClassic.calculateFlowCost
     * Lycanites overrides canFlowInto, which get called for an area via calculateFlowCost
     */
    @Unique
    private int lycanitesTweaks$checkFlowForMix(World world, BlockPos pos, int recurseDepth, int side) {
        int cost = 1000;
        for (int adjSide = 0; adjSide < 4; adjSide++) {
            if (SIDES.get(adjSide) == SIDES.get(side).getOpposite()) {
                continue;
            }

            BlockPos flowPos = pos.offset(SIDES.get(adjSide));

            if (!canFlowInto(world, flowPos) || isSourceBlock(world, flowPos)) {
                continue;
            }

            this.lycanitesTweaks$checkForMixing(world, flowPos, this.getDefaultState());
            if (canFlowInto(world, flowPos.up(densityDir))) {
                return recurseDepth;
            }

            if (recurseDepth >= quantaPerBlock / 2) {
                continue;
            }

            cost = Math.min(cost, this.lycanitesTweaks$checkFlowForMix(world, flowPos, recurseDepth + 1, adjSide));
        }
        return cost;
    }

    @Unique
    private void lycanitesTweaks$triggerMixEffects(World world, BlockPos pos, SoundEvent soundEvent, Material targetMaterial) {
        world.playSound(
                null,
                pos,
                soundEvent,
                SoundCategory.BLOCKS,
                0.5F,
                2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
        );
        if (world instanceof WorldServer) {
            if(targetMaterial == Material.LAVA) {
                ((WorldServer) world).spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE,
                        pos.getX() + Math.random(),
                        pos.getY() + 1.2D,
                        pos.getZ() + Math.random(),
                        8, // particle count
                        0.0D, 0.0D, 0.0D,
                        0.0D
                );
            }
            else {
                ((WorldServer) world).spawnParticle(
                        EnumParticleTypes.CLOUD,
                        pos.getX() + Math.random(),
                        pos.getY() + 1.2D,
                        pos.getZ() + Math.random(),
                        2, // particle count
                        0.0D, 0.0D, 0.0D,
                        0.0D
                );
            }
        }
    }

    // BlockDynamicLiquid.updateTick
    @Unique
    public void lycanitesTweaks$updateTickDynamicLiquid(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        // BlockLiquid.updateTick flow behavior, but only care about source creation
        if(!ForgeConfigHandler.mixinPatchesConfig.fixFluidsCreateSourceVanilla
                && ForgeEventFactory.canCreateFluidSource(worldIn, pos, state, this.canCreateSources)) {
            int adjacentSourceBlocks = 0;
            for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
                if (isSourceBlock(worldIn, pos.offset(side))) adjacentSourceBlocks++;
            }

            if (adjacentSourceBlocks >= 2) {
                worldIn.setBlockState(pos, this.getDefaultState().withProperty(LEVEL, 0), 2);
            }
        }
    }

    // BlockStaticLiquid.updateTick
    @Unique
    public void lycanitesTweaks$updateTickStaticLiquid(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (this.material == Material.LAVA) {
            if (worldIn.getGameRules().getBoolean("doFireTick")) {
                int burnAttempts = rand.nextInt(3);

                // [1,3] Side Burn
                if (burnAttempts > 0) {
                    BlockPos sideFirePos = pos;

                    for (int j = 0; j < burnAttempts; ++j) {
                        sideFirePos = sideFirePos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);

                        if (sideFirePos.getY() >= 0 && sideFirePos.getY() < worldIn.getHeight() && !worldIn.isBlockLoaded(sideFirePos)) {
                            return;
                        }

                        IBlockState block = worldIn.getBlockState(sideFirePos);

                        if (block.getBlock().isAir(block, worldIn, sideFirePos)) {
                            if (this.lycanitesTweaks$isSurroundingBlockFlammable(worldIn, sideFirePos)) {
                                worldIn.setBlockState(sideFirePos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, sideFirePos, pos, Blocks.FIRE.getDefaultState()));
                                return;
                            }
                        }
                        else if (block.getMaterial().blocksMovement()) {
                            return;
                        }
                    }
                }
                // [3,3] Ground Burn
                else {
                    for (int k = 0; k < 3; ++k) {
                        BlockPos groundFirePos = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);

                        if (groundFirePos.getY() >= 0 && groundFirePos.getY() < 256 && !worldIn.isBlockLoaded(groundFirePos)) {
                            return;
                        }

                        if (worldIn.isAirBlock(groundFirePos.up()) && this.lycanitesTweaks$getCanBlockBurn(worldIn, groundFirePos)) {
                            worldIn.setBlockState(groundFirePos.up(), net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, groundFirePos.up(), pos, Blocks.FIRE.getDefaultState()));
                        }
                    }
                }
            }
        }
    }

    @Unique
    private boolean lycanitesTweaks$isSurroundingBlockFlammable(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.values()) {
            if (this.lycanitesTweaks$getCanBlockBurn(worldIn, pos.offset(enumfacing))) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private boolean lycanitesTweaks$getCanBlockBurn(World worldIn, BlockPos pos) {
        return (pos.getY() < 0 || pos.getY() >= 256 || worldIn.isBlockLoaded(pos)) && worldIn.getBlockState(pos).getMaterial().getCanBurn();
    }
}
