package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntityPotionApplicableMixin {

    @ModifyExpressionValue(
            method = "ownerEffects",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;isPotionApplicable(Lnet/minecraft/potion/PotionEffect;)Z")
    )
    private boolean lycanitesTweaks_lycanitesRideableCreatureEntity_ownerEffectsCleanseNegative(boolean isPotionApplicable, @Local PotionEffect effectInstance){
        if(!effectInstance.getPotion().isBadEffect()) return true;
        return isPotionApplicable;
    }
}
