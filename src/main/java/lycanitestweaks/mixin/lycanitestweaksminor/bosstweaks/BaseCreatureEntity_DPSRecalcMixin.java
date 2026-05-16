package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_DPSRecalcMixin {

    @Shadow(remap = false)
    public int damageMax;
    @Shadow(remap = false)
    public float damageLimit;
    @Shadow(remap = false)
    public float damageTakenThisSec;

    // [Boss DPS Limit] LivingAttackEvent -> LivingDamageEvent
    // [Boss Single Hit Max] Pre-LivingDamageEvent -> LivingDamageEvent
    @WrapMethod(
            method = "onDamage",
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onDamageRecalc(DamageSource damageSrc, float damage, Operation<Void> original) {
        // no op, Move Damage Limit to after Living Damage, but allow any Overrides to run
    }

    @WrapOperation(
            method = "getDamageAfterDefense",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"),
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getDamageAfterDefenseRecalc(float damage, float damageMax, Operation<Float> original) {
        return damage; // Move Max Damage to after LivingDamage
    }

    @ModifyExpressionValue(
            method = "damageEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/util/DamageSource;F)F", remap = false)
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_damageEntityRecalc(float damageAmount, @Local(argsOnly = true) DamageSource damageSrc) {
        if (this.damageMax > 0F) {
            damageAmount = Math.min(damageAmount, this.damageMax);
        }
        if (this.damageLimit > 0F) {
            damageAmount = Math.min(damageAmount, this.damageLimit - this.damageTakenThisSec);
        }
        this.damageTakenThisSec += damageAmount;
        return damageAmount;
    }
}
