package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_ProjectileUseAttributeMixin {

    @Shadow(remap = false) public abstract float getAttackDamage(double damageScale);

    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getDamage()D"),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageWithDamageAttribute(double original){
        return this.getAttackDamage(1);
    }
}
