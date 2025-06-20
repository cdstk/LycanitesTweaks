package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityShade;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityShade.class)
public abstract class EntityShadeEffectsCapMixin {

    @ModifyExpressionValue(
            method = "specialAttack",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityShade;getEffectDuration(I)I", remap = false),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityShade_specialAttackEffectLevelLimit(int original){
        if(CreatureStatsConfig.getLevelLimitedEffects().containsKey("shade")){
            return Helpers.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 5, CreatureStatsConfig.getLevelLimitedEffects().get("shade"));
        }
        return original;
    }
}
