package lycanitestweaks.mixin.lycanitesmobspatches.core.fluid;

import com.lycanitesmobs.core.block.BlockFluidBase;
import com.lycanitesmobs.core.block.fluid.BlockFluidAcid;
import com.lycanitesmobs.core.block.fluid.BlockFluidMoglava;
import com.lycanitesmobs.core.block.fluid.BlockFluidOoze;
import com.lycanitesmobs.core.block.fluid.BlockFluidPoison;
import com.lycanitesmobs.core.block.fluid.BlockFluidRabbitooze;
import com.lycanitesmobs.core.block.fluid.BlockFluidSharacid;
import com.lycanitesmobs.core.block.fluid.BlockFluidVeshoney;
import com.lycanitesmobs.core.block.fluid.BlockFluidVesspoison;
import com.lycanitesmobs.core.info.ModInfo;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        BlockFluidAcid.class,
        BlockFluidMoglava.class,
        BlockFluidOoze.class,
        BlockFluidPoison.class,
        BlockFluidRabbitooze.class,
        BlockFluidSharacid.class,
        BlockFluidVeshoney.class,
        BlockFluidVesspoison.class
})
public abstract class BlockFluidBases_VanillaMixMixin extends BlockFluidBase {

    public BlockFluidBases_VanillaMixMixin(Fluid fluid, Material material, ModInfo group, String blockName) {
        super(fluid, material, group, blockName);
    }

    @Inject(
            method = "canDisplace",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBlockFluidBases_canDisplaceSuperOnly(IBlockAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        // Have all instances skip overrides and run BlockFluidBase.canDisplace
        cir.setReturnValue(super.canDisplace(world, pos));
    }
}
