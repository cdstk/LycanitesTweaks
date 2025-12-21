package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_AttackSpeedCrashMixin {

    @ModifyReturnValue(
            method = "getMeleeCooldown",
            at = @At("RETURN"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesBaseCreatureEntity_getMeleeCooldownMinimum(int original){
        return Math.max(original, 1);
    }

    // ADDRESS (--this.attackTime == 0) in AttackRangedGoal
    @ModifyReturnValue(
            method = "getRangedCooldown",
            at = @At("RETURN"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesBaseCreatureEntity_getRangedCooldownMinimum(int original){
        return Math.max(original, 1);
    }
}
