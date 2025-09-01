package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.pickup;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ExtendedEntity.class)
public abstract class ExtendedEntityPickupMixin {

    @Shadow(remap = false)
    public EntityLivingBase entity;
    @Shadow(remap = false)
    public Entity pickedUpByEntity;

    // Replace with config value for auto self drop
    @ModifyConstant(
            method = "updatePickedUpByEntity",
            constant = @Constant(doubleValue = 32.0D),
            remap = false
    )
    private double lycanitesTweaks_lycanitesExtendedEntity_configPickUpDistance(double constant) {
        return ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.pickUpDistance;
    }

    // Prevent desync where carrying mob still thinks it's holding something
    @WrapWithCondition(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/ExtendedEntity;setPickedUpByEntity(Lnet/minecraft/entity/Entity;)V"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesExtendedEntity_baseCreatureDrop(ExtendedEntity instance, Entity message){
        if(this.pickedUpByEntity instanceof BaseCreatureEntity && this.pickedUpByEntity.isEntityAlive())
            ((BaseCreatureEntity) this.pickedUpByEntity).dropPickupEntity();
        else instance.setPickedUpByEntity(message);

        return false;
    }

    // Main position setting, add distance check
    @WrapWithCondition(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setPosition(DDD)V")
    )
    public boolean lycanitesTweaks_lycanitesExtendedEntity_updatePickedUpByEntity(EntityLivingBase instance, double x, double y, double z){
        return !(instance.getDistance(x, y, z) > ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.pickUpDistance);
    }

    // Add distance check
    @WrapWithCondition(
            method = "setPickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setPosition(DDD)V")
    )
    public boolean lycanitesTweaks_lycanitesExtendedEntity_setPickedUpByEntity(EntityLivingBase instance, double x, double y, double z){
        return !(instance.getDistance(x, y, z) > ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.pickUpDistance);
    }
}
