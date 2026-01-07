package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityEechetik;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityEechetik.class)
public abstract class EntityEechetikEffectsCapMixin {

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityEechetik;getEffectDuration(I)I", remap = false)
    )
    public int lycanitesTweaks_lycanitesMobsEntityEechetik_onLivingUpdateEffectLevelLimit(int original){
        if(ForgeConfigProvider.getLevelLimitedEffects().containsKey("eechetik")){
            return LycanitesEntityUtil.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 2, ForgeConfigProvider.getLevelLimitedEffects().get("eechetik"));
        }
        return original;
    }
}
