package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.features.HarvestEquipmentFeature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HarvestEquipmentFeature.class)
public abstract class HarvestEquipmentFeature_ForgeEventMixin {

    @WrapWithCondition(
            method = "onBlockDestroyed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;destroyBlock(Lnet/minecraft/util/math/BlockPos;Z)Z")
    )
    public boolean lycanitesTweaks_lycanitesMobsHarvestEquipmentFeature_onBlockDestroyedForgeGriefEvent(World instance, BlockPos pos, boolean dropBlock, @Local(argsOnly = true) EntityLivingBase livingEntity){
        return ForgeEventFactory.onEntityDestroyBlock(livingEntity, pos, instance.getBlockState(pos));
    }
}
