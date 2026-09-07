package lycanitestweaks.mixin.vanilla.pickupfix;

import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class Entity_PickupMixin {

    @Inject(
            method = "dismountRidingEntity",
            at = @At("HEAD")
    )
    private void lycanitesTweaks_vanillaEntity_dismountRidingEntityDropPickup(CallbackInfo ci){
        Entity entity = (Entity) (Object) this;
        if(!(entity instanceof EntityPlayer))
            LycanitesMobsWrapper.dropPickedUpBy(entity);
    }
}
