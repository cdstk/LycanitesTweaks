package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats.otherscaledeffects;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWarg;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityWarg.class)
public abstract class EntityWargEffectsCapMixin {

    @ModifyArgs(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionEffect;<init>(Lnet/minecraft/potion/Potion;II)V")
    )
    public void lycanitesTweaks_lycanitesMobsEntityWarg_onLivingUpdateEffectLevelLimit(Args args){
        if(ForgeConfigProvider.getLevelLimitedEffects().containsKey("warg")){
            args.set(1, LycanitesEntityUtil.getEffectDurationLevelLimited((BaseCreatureEntity)(Object)this, 1, ForgeConfigProvider.getLevelLimitedEffects().get("warg")));
            args.set(2, LycanitesEntityUtil.getEffectAmplifierLevelLimited((BaseCreatureEntity)(Object)this, 1.0F, ForgeConfigProvider.getLevelLimitedEffects().get("warg")));
        }
    }
}
