package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.damageability;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityVorach;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityVorach.class)
public abstract class EntityVorach_DamageAbilityMixin {

    @ModifyArg(
            method = "attackMelee",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityVorach;heal(F)V"),
            remap = false
    )
    public float lycanitesTweaks_lycanitesMobsEntityVorach_attackMeleeLeechBoss(float healAmount, @Local(argsOnly = true) Entity target){
        if(target instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) target;
            if(creature.damageMax > 0) {
                return Math.min(healAmount, creature.damageMax);
            }
        }
        return healAmount;
    }
}
