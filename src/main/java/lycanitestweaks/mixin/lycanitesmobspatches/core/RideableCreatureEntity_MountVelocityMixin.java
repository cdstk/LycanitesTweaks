package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_MountVelocityMixin extends TameableCreatureEntity {

    public RideableCreatureEntity_MountVelocityMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;dismountRidingEntity()V")
    )
    public void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_onLivingUpdateDismountMotion(CallbackInfo ci){
        this.motionX = this.motionY = this.motionZ = 0;
    }

    @Unique
    @Override
    protected void markVelocityChanged() {
        if(!this.hasRiderTarget()) super.markVelocityChanged();
    }
}
