package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntityPotionApplicableMixin {

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/RideableCreatureEntity;isPotionApplicable(Lnet/minecraft/potion/PotionEffect;)Z")
    )
    private boolean lycanitesTweaks_lycanitesRideableCreatureEntity_onLivingUpdateCleanseNegative(boolean isPotionApplicable, @Local PotionEffect effectInstance){
        if(!effectInstance.getPotion().isBadEffect()) return true;
        return isPotionApplicable;
    }
}
