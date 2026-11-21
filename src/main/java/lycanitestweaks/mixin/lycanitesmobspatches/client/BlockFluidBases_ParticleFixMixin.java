package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.block.BlockFluidBase;
import com.lycanitesmobs.core.block.fluid.BlockFluidAcid;
import com.lycanitesmobs.core.block.fluid.BlockFluidMoglava;
import com.lycanitesmobs.core.block.fluid.BlockFluidOoze;
import com.lycanitesmobs.core.block.fluid.BlockFluidPoison;
import com.lycanitesmobs.core.block.fluid.BlockFluidVeshoney;
import com.lycanitesmobs.core.info.ModInfo;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

@Mixin(value = {
        BlockFluidAcid.class,
        BlockFluidMoglava.class,
        BlockFluidOoze.class,
        BlockFluidPoison.class,
        BlockFluidVeshoney.class,
})
public abstract class BlockFluidBases_ParticleFixMixin extends BlockFluidBase {

    public BlockFluidBases_ParticleFixMixin(Fluid fluid, Material material, ModInfo group, String blockName) {
        super(fluid, material, group, blockName);
    }

    @WrapMethod(
            method = "randomDisplayTick"
    )
    private void lycanitesTweaks_lycanitesMobsBlockFluidBases_randomDisplayTickOnlySolidSource(IBlockState blockState, World world, BlockPos pos, Random random, Operation<Void> original){
        if(world.getBlockState(pos.up()).getMaterial() == Material.AIR && !world.getBlockState(pos.up()).isOpaqueCube()) {
            original.call(blockState, world, pos, random);
        }
    }

    @Inject(
            method = "randomDisplayTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V")
    )
    private void lycanitesTweaks_lycanitesMobsBlockFluidBases_randomDisplayTickSoundParticle(IBlockState blockState, World world, BlockPos pos, Random random, CallbackInfo ci, @Local(name = "f") float f, @Local(name = "f1") float f1, @Local(name = "f2") float f2){
        SoundEvent soundEvent = AssetManager.getSound(this.blockName);
        if(soundEvent != null) {
            world.playSound(
                    f, f1, f2,
                    soundEvent, SoundCategory.BLOCKS,
                    0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F,
                    false
            );
        }
    }

    @ModifyArgs(
            method = "randomDisplayTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnParticle(Lnet/minecraft/util/EnumParticleTypes;DDDDDD[I)V")
    )
    private void lycanitesTweaks_lycanitesMobsBlockFluidBases_randomDisplayTickParticleFix(Args args, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Random random){
        EnumParticleTypes particleType = args.get(0);
        if(particleType != EnumParticleTypes.LAVA) {
            // y Pos
            args.set(2,  pos.getY() + 0.05D + this.getQuantaPercentage(world, pos));
            // texture offset
            args.set(4, (random.nextDouble() - 0.5D) * 0.5D);
            args.set(5, random.nextDouble() * 0.5D);
            args.set(6, (random.nextDouble() - 0.5D) * 0.5D);
        }
    }
}
