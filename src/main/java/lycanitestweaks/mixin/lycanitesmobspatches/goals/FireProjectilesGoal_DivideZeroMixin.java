package lycanitestweaks.mixin.lycanitesmobspatches.goals;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.goals.actions.abilities.FireProjectilesGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FireProjectilesGoal.class)
public abstract class FireProjectilesGoal_DivideZeroMixin {

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I")
    )
    public int lycanitesTweaks_lycanitesMobsFireProjectilesGoal_updateTaskMinimum(int original){
        return Math.max(original, 1);
    }
}
