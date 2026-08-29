package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.actions.AttackRangedGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttackRangedGoal.class)
public abstract class AttackRangedGoal_BigHybridMixin {

    @Final
    @Shadow(remap = false) private BaseCreatureEntity host;

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/goals/actions/AttackRangedGoal;attackDistance:F", remap = false)
    )
    private float lycanitesTweaks_lycanitesMobsAttackMeleeGoal_shouldExecuteRangedAttackMaximum(float attackDistance){
        return attackDistance + (float) Math.sqrt(this.host.getPhysicalRange());
    }
}
