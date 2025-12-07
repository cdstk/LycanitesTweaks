package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import com.lycanitesmobs.core.entity.FearEntity;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.vecmath.Vector3d;

@Mixin(ExtendedEntity.class)
public abstract class ExtendedEntity_PickupMixin {

    @Shadow(remap = false)
    public EntityLivingBase entity;
    @Shadow(remap = false)
    public FearEntity fearEntity;
    @Shadow(remap = false)
    public Entity pickedUpByEntity;
    @Shadow(remap = false)
    Vector3d lastSafePos;

    @Unique
    private double lycanitesTweaks$pickupRange = 32D;

    // Fix Fear when unloaded
    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/FearEntity;isEntityAlive()Z")
    )
    private boolean lycanitesTweaks_lycanitesMobsExtendedEntity_onUpdateFearUnloaded(boolean isEntityAlive){
        return isEntityAlive && this.fearEntity.isAddedToWorld();
    }

    // Calculate range for client and server and store when holdingEntity becomes null
    @Inject(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getEntityWorld()Lnet/minecraft/world/World;", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntityCalcRange(CallbackInfo ci){
        this.lycanitesTweaks$pickupRange = Helpers.getAutoDropPickupDistance(this.pickedUpByEntity, this.entity);
    }

    // Prevent desync where carrying mob still thinks it's holding something
    @WrapOperation(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/ExtendedEntity;setPickedUpByEntity(Lnet/minecraft/entity/Entity;)V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntityBaseCreatureDrop(ExtendedEntity pickupVictim, Entity nullHoldingEntity, Operation<Void> original){
        if(this.pickedUpByEntity instanceof BaseCreatureEntity && this.pickedUpByEntity.isEntityAlive())
            ((BaseCreatureEntity) this.pickedUpByEntity).dropPickupEntity();
        else original.call(pickupVictim, nullHoldingEntity);
    }

    // Replace with config value for auto self drop
    @ModifyConstant(
            method = "updatePickedUpByEntity",
            constant = @Constant(doubleValue = 32.0D),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntityConfigPickupDistance(double constant) {
        return this.lycanitesTweaks$pickupRange;
    }

    // Main position setting, add distance check
    @WrapWithCondition(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setPosition(DDD)V")
    )
    private boolean lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntityContinuousTeleport(EntityLivingBase pickupVictim, double x, double y, double z){
        return (pickupVictim.getDistance(x, y, z) <= this.lycanitesTweaks$pickupRange);
    }

    // Fix Fear being broken if picked up by another mob
    @Inject(
            method = "setPickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getRidingEntity()Lnet/minecraft/entity/Entity;")
    )
    private void lycanitesTweaks_lycanitesMobsExtendedEntity_setPickedUpByEntityClearPrevious(Entity pickedUpByEntity, CallbackInfo ci){
        if(this.pickedUpByEntity instanceof BaseCreatureEntity) ((BaseCreatureEntity) this.pickedUpByEntity).pickupEntity = null;
    }

    // Add distance check
    @WrapWithCondition(
            method = "setPickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setPosition(DDD)V")
    )
    private boolean lycanitesTweaks_lycanitesMobsExtendedEntity_setPickedUpByEntityEndTeleport(EntityLivingBase pickupVictim, double x, double y, double z){
        return (pickupVictim.getDistance(this.lastSafePos.getX(), this.lastSafePos.getY(), this.lastSafePos.getZ()) <= this.lycanitesTweaks$pickupRange);
    }
}
