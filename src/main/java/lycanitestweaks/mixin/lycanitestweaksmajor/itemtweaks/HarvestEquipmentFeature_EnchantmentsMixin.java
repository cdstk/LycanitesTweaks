package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.features.HarvestEquipmentFeature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HarvestEquipmentFeature.class)
public abstract class HarvestEquipmentFeature_EnchantmentsMixin {

    @WrapWithCondition(
            method = "onBlockDestroyed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z", remap = true),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsHarvestEquipmentFeature_onBlockDestroyedHarvest(World instance, BlockPos blockPos, boolean dropBlockAsItem, @Local(argsOnly = true) EntityLivingBase livingEntity){
        if(livingEntity instanceof EntityPlayerMP){
            ((EntityPlayerMP)livingEntity).interactionManager.tryHarvestBlock(blockPos);
            return false;
        }
        return true;
    }
}
