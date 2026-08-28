package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels.limiteddim;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.info.CreatureConfig;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PetEntry.class)
public abstract class PetEntryBoundDimLimitedMixin {

    @Shadow(remap = false)
    public EntityLivingBase host;
    @Shadow(remap = false)
    public Entity entity;

    // Prone to desyncs without Fix Client Pet Stat Desync
    // Will fail without Fix Properties Set After Stat Calculation

    // overrule dim blacklist active despawn
    @WrapWithCondition(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/pets/PetEntry;setSpawningActive(Z)Lcom/lycanitesmobs/core/pets/PetEntry;"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsPetEntry_onUpdate(PetEntry instance, boolean spawningActive){
        if(!ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability)
            return spawningActive;

        return !ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimOverruleBlacklist
                || !PlayerMobLevelsConfig.isDimensionLimitedMinion(this.host.dimension);
    }

    // overrule dim blacklist attempt to spawn
    @WrapOperation(
            method = "setSpawningActive",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureConfig;isSoulboundAllowed(Lnet/minecraft/world/World;)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsPetEntry_setSpawningActive(CreatureConfig instance, World inDimensionList, Operation<Boolean> original){
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability
                && ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimOverruleBlacklist
                && PlayerMobLevelsConfig.isDimensionLimitedMinion(this.host.dimension))
            return true;

        return original.call(instance, inDimensionList);
    }
}
