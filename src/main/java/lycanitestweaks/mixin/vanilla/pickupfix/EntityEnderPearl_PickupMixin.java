package lycanitestweaks.mixin.vanilla.pickupfix;

import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityEnderPearl.class)
public abstract class EntityEnderPearl_PickupMixin {

    @Inject(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isRiding()Z")
    )
    private void lycanitesTweaks_vanillaEntityEnderPearl_onImpactDropPickup(RayTraceResult result, CallbackInfo ci, @Local EntityLivingBase entitylivingbase){
        LycanitesMobsWrapper.dropPickedUpBy(entitylivingbase);
    }
}
