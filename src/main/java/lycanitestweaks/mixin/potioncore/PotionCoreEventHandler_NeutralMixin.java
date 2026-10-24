package lycanitestweaks.mixin.potioncore;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.tmtravlr.potioncore.PotionCoreEventHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionCoreEventHandler.class)
public abstract class PotionCoreEventHandler_NeutralMixin {

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;isBadEffect()Z")
    )
    private static boolean lycanitesTweaks_potionCorePotionCoreEventHandler_onLivingUpdateNeutralNoSickness(boolean isBadEffect, LivingEvent.LivingUpdateEvent event, @Local PotionEffect potionEffect){
        if(ForgeConfigHandler.integrationConfig.potionCoreAuraNoSickness || ForgeConfigHandler.integrationConfig.potionCoreFoodNoSickness) {
            if(event.getEntityLiving().getEntityWorld().isRemote) return true;
        }

        boolean isNeutral = false;
        if(potionEffect instanceof SpecialPotionEffect) {
            isNeutral = ((SpecialPotionEffect) potionEffect).getNeutral();
        }

        return isBadEffect || isNeutral;
    }
}
