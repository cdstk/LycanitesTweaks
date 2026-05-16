package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.damageability;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityDarkling;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityDarkling.class)
public abstract class EntityDarkling_DamageAbilityMixin {

    @Shadow(remap = false) public abstract EntityLivingBase getLatchTarget();

    @ModifyArg(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityDarkling;heal(F)V"),
            remap = false
    )
    public float lycanitesTweaks_lycanitesMobsEntityDarkling_attackMeleeLeechBoss(float healAmount){
        if(this.getLatchTarget() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) this.getLatchTarget();
            if(creature.damageMax > 0) {
                return Math.min(healAmount, creature.damageMax);
            }
        }
        return healAmount;
    }
}
