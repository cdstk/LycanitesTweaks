package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBarghest;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityBarghest.class)
public abstract class EntityBarghestEffectsCapMixin {

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityBarghest;getEffectDuration(I)I", remap = false)
    )
    public int lycanitesTweaks_lycanitesMobsEntityBarghest_onLivingUpdateEffectLevelLimit(int original){
        if(ForgeConfigProvider.getLevelLimitedEffects().containsKey("barghest")){
            return LycanitesEntityUtil.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 5, ForgeConfigProvider.getLevelLimitedEffects().get("barghest"));
        }
        return original;
    }
}
