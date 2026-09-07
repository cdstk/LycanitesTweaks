package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.FearEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FearEntity.class)
public abstract class FearEntity_PickupMixin extends BaseCreatureEntity {

    public FearEntity_PickupMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/FearEntity;canPickupEntity(Lnet/minecraft/entity/EntityLivingBase;)Z", remap = false)
    )
    private boolean lycanitesTweaks_lycanitesMobsFearEntity_onLivingUpdateSetLaterPickup(boolean original, @Local EntityLivingBase fearedEntityLiving){
        return false;
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ObjectManager;getEffect(Ljava/lang/String;)Lcom/lycanitesmobs/PotionBase;", ordinal = 0, remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsFearEntity_onLivingUpdateDoLaterPickup(CallbackInfo ci, @Local(name = "fearedEntityLiving") EntityLivingBase fearedEntityLiving){
        // Implied distance check via tp and properly resets when Vanilla mixin sets original fear as dead
        if(this.canPickupEntity(fearedEntityLiving)) {
            this.pickupEntity(fearedEntityLiving);
        }
    }
}
