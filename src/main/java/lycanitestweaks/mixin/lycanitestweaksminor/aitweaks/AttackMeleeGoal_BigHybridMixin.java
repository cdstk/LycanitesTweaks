package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.actions.AttackMeleeGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AttackMeleeGoal.class)
public abstract class AttackMeleeGoal_BigHybridMixin {

    @Shadow(remap = false) private BaseCreatureEntity host;

    @ModifyExpressionValue(
            method = "shouldExecute",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/goals/actions/AttackMeleeGoal;maxChaseDistance:F", remap = false)
    )
    private float lycanitesTweaks_lycanitesMobsAttackMeleeGoal_shouldExecuteMeleeMinimum(float maxChaseDistance){
        return (float) Math.max(maxChaseDistance, this.host.getPhysicalRange());
    }

    @ModifyExpressionValue(
            method = "shouldContinueExecuting",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/goals/actions/AttackMeleeGoal;maxChaseDistance:F", remap = false)
    )
    private float lycanitesTweaks_lycanitesMobsAttackMeleeGoal_shouldContinueExecutingMeleeMinimum(float maxChaseDistance){
        return (float) Math.max(maxChaseDistance, this.host.getPhysicalRange());
    }
}
