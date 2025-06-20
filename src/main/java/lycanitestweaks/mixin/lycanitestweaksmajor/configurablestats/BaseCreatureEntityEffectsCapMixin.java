package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ElementInfo;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityEffectsCapMixin {

    @ModifyArgs(
            method = "applyDebuffs",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ElementInfo;debuffEntity(Lnet/minecraft/entity/EntityLivingBase;II)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyDebuffsLevelLimit(Args args, @Local(argsOnly = true, ordinal = 0)int duration, @Local(argsOnly = true, ordinal = 1) int amplifier, @Local ElementInfo element){
        if(CreatureStatsConfig.getLevelLimitedElementDebuffs().containsKey(element.name)){
            args.set(1,
                    Helpers.getEffectDurationLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            duration,
                            CreatureStatsConfig.getLevelLimitedElementDebuffs().get(element.name)));
            args.set(2,
                    Helpers.getEffectAmplifierLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            amplifier,
                            CreatureStatsConfig.getLevelLimitedElementDebuffs().get(element.name)));
        }
    }
}
