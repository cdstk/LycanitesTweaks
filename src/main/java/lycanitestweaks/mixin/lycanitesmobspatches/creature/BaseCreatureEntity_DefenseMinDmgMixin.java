package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_DefenseMinDmgMixin {

    @ModifyExpressionValue(
            method = "getDamageAfterDefense",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 0),
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getDamageAfterDefenseMinDmg(float dmgAfterDef, float damage){
        return damage <= 1F ? damage : dmgAfterDef;
    }
}
