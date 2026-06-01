package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import lycanitestweaks.handlers.ForgeConfigProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreatureStats.class)
public abstract class CreatureStatsCapMixin {

    @Shadow(remap = false)
    public BaseCreatureEntity entity;
    @Shadow(remap = false)
    protected abstract double getVariantMultiplier(String stat);

    @ModifyReturnValue(
            method = "getHealth",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getHealth(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("health");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.health * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getDefense",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getDefense(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("defense");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.defense * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getArmor",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getArmor(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("armor");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.armor * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getSpeed",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getSpeed(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("speed");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.speed * this.getVariantMultiplier(statName) * 0.01D);
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getDamage",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getDamage(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("damage");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.damage * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getAttackSpeed",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getAttackSpeed(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("attackspeed");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.attackSpeed * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getRangedSpeed",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getRangedSpeed(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("rangedspeed");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.rangedSpeed * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getEffect",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getEffectDuration(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("effectduration");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.effectDuration * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getAmplifier",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getEffectAmplifier(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("effectamplifier");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.effectAmplifier * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getPierce",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getPierce(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("pierce");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.pierce * this.getVariantMultiplier(statName));
        }
        return original;
    }

    @ModifyReturnValue(
            method = "getSight",
            at = @At("RETURN"),
            remap = false
    )
    public double lycanitesTweaks_lycanitesCreatureStats_getSight(double original, @Local String statName){
        double ratio = ForgeConfigProvider.getStatRatioCap("sight");
        if(ratio >= 0) {
            return Math.min(original, ratio * this.entity.creatureInfo.sight * this.getVariantMultiplier(statName));
        }
        return original;
    }
}
