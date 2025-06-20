package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityRaiko;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRaiko.class)
public abstract class EntityRaikoEffectsCapMixin {

    @ModifyExpressionValue(
            method = "dropPickupEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRaiko;getEffectDuration(I)I", remap = false),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRaiko_dropPickupEntityEffectLevelLimit(int original){
        if(CreatureStatsConfig.getLevelLimitedEffects().containsKey("raiko")){
            return Helpers.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 5, CreatureStatsConfig.getLevelLimitedEffects().get("raiko"));
        }
        return original;
    }
}
