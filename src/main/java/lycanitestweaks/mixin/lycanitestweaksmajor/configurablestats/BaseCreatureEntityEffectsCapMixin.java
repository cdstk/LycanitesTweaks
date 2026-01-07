package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ElementInfo;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityEffectsCapMixin {

    @WrapWithCondition(
            method = "applyBuffs",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ElementInfo;buffEntity(Lnet/minecraft/entity/EntityLivingBase;II)V"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyBuffsBlacklisted(ElementInfo elementInfo, EntityLivingBase targetEntity, int duration, int amplifier){
        return !ForgeConfigProvider.getElementsApplyBuffBlacklist().contains(elementInfo.name);
    }

    @ModifyArgs(
            method = "applyBuffs",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ElementInfo;buffEntity(Lnet/minecraft/entity/EntityLivingBase;II)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyBuffsLevelLimit(Args args, @Local(argsOnly = true, ordinal = 0)int duration, @Local(argsOnly = true, ordinal = 1) int amplifier, @Local ElementInfo element){
        if(ForgeConfigProvider.getLevelLimitedElementBuffs().containsKey(element.name)){
            args.set(1,
                    LycanitesEntityUtil.getEffectDurationLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            duration,
                            ForgeConfigProvider.getLevelLimitedElementBuffs().get(element.name)));
            args.set(2,
                    LycanitesEntityUtil.getEffectAmplifierLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            amplifier,
                            ForgeConfigProvider.getLevelLimitedElementBuffs().get(element.name)));
        }
    }

    @ModifyArgs(
            method = "applyDebuffs",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ElementInfo;debuffEntity(Lnet/minecraft/entity/EntityLivingBase;II)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_applyDebuffsLevelLimit(Args args, @Local(argsOnly = true, ordinal = 0)int duration, @Local(argsOnly = true, ordinal = 1) int amplifier, @Local ElementInfo element){
        if(ForgeConfigProvider.getLevelLimitedElementDebuffs().containsKey(element.name)){
            args.set(1,
                    LycanitesEntityUtil.getEffectDurationLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            duration,
                            ForgeConfigProvider.getLevelLimitedElementDebuffs().get(element.name)));
            args.set(2,
                    LycanitesEntityUtil.getEffectAmplifierLevelLimited(
                            (BaseCreatureEntity)(Object)this,
                            amplifier,
                            ForgeConfigProvider.getLevelLimitedElementDebuffs().get(element.name)));
        }
    }
}
