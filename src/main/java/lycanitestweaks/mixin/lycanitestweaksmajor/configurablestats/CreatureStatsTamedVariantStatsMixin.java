package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import lycanitestweaks.util.LycanitesEntityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreatureStats.class)
public abstract class CreatureStatsTamedVariantStatsMixin {

    @Unique
    public boolean lycanitesTweaks$getConfigVariantCondition(BaseCreatureEntity entity){
        return !LycanitesEntityUtil.shouldApplyExtraMultipliers(entity);
    }

    @Redirect(
            method = "getHealth",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getHealth(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getDefense",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getDefense(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getArmor",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getArmor(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getSpeed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getSpeed(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getDamage(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getAttackSpeed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getAttackSpeed(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getRangedSpeed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getRangedSpeed(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getEffect",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getEffect(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getAmplifier",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getAmplifier(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getPierce",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getPierce(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }

    @Redirect(
            method = "getSight",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesCreatureStats_getSight(BaseCreatureEntity instance){
        return lycanitesTweaks$getConfigVariantCondition(instance);
    }
}
