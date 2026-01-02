package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.item.equipment.features.SummonEquipmentFeature;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SummonEquipmentFeature.class)
public abstract class SummonEquipmentFeature_FlagMixin {

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;setPVP(Z)V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummonEquipmentFeature_onHitEntityPetFlags(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker, CallbackInfoReturnable<Boolean> cir, @Local TameableCreatureEntity entityTameable){
        if(entityTameable instanceof ITameableCreatureEntity_TargetFlagMixin){
            ((ITameableCreatureEntity_TargetFlagMixin)entityTameable).lycanitesTweaks$setTargetBoss(
                    !target.isNonBoss()
                    || target instanceof BaseCreatureEntity && ((BaseCreatureEntity) target).isBoss());
        }
    }
}
