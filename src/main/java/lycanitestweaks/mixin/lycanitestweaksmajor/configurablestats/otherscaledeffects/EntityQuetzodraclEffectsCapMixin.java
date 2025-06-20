package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityQuetzodracl;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityQuetzodracl.class)
public abstract class EntityQuetzodraclEffectsCapMixin {

    @ModifyExpressionValue(
            method = "dropPickupEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityQuetzodracl;getEffectDuration(I)I", remap = false),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityQuetzodracl_dropPickupEntityEffectLevelLimit(int original){
        if(CreatureStatsConfig.getLevelLimitedEffects().containsKey("quetzodracl")){
            return Helpers.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 5, CreatureStatsConfig.getLevelLimitedEffects().get("quetzodracl"));
        }
        return original;
    }
}
