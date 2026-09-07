package lycanitestweaks.mixin.vanilla.pickupfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayer_PickupMixin extends EntityLivingBase {

    @Unique
    private boolean lycanitesTweaks$keybindDismount = false;

    public EntityPlayer_PickupMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "updateRidden",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;dismountRidingEntity()V")
    )
    private void lycanitesTweaks_vanillaEntityPlayer_updateRiddenDropPickupNonSneak(EntityPlayer instance, Operation<Void> original){
        this.lycanitesTweaks$keybindDismount = true;
        original.call(instance);
        this.lycanitesTweaks$keybindDismount = false;
    }

    @Inject(
            method = "dismountRidingEntity",
            at = @At("HEAD")
    )
    private void lycanitesTweaks_vanillaEntityPlayer_dismountRidingEntityDropPickup(CallbackInfo ci){
        if(!this.lycanitesTweaks$keybindDismount)
            LycanitesMobsWrapper.dropPickedUpBy(this);
    }
}
