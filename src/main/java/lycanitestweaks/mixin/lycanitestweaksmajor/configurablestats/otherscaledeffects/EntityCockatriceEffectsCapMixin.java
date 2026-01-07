package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityCockatrice;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCockatrice.class)
public abstract class EntityCockatriceEffectsCapMixin {

    @ModifyExpressionValue(
            method = "specialAttack",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityCockatrice;getEffectDuration(I)I", remap = false),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityCockatrice_specialAttackEffectLevelLimit(int original){
        if(ForgeConfigProvider.getLevelLimitedEffects().containsKey("cockatrice")){
            return LycanitesEntityUtil.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 5, ForgeConfigProvider.getLevelLimitedEffects().get("cockatrice"));
        }
        return original;
    }
}
