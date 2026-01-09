package lycanitestweaks.mixin.potioncore;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.tmtravlr.potioncore.PotionCoreEventHandler;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionCoreEventHandler.class)
public abstract class PotionCoreEventHandler_ProjectileDamageMixin {

    @ModifyExpressionValue(
            method = "onLivingHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;isProjectile()Z")
    )
    private static boolean lycanitesTweaks_potionCorePotionCoreEventHandler_onLivingHurtIgnoreLycanites(boolean original, LivingHurtEvent event){
        return original && !(event.getSource().getTrueSource() instanceof BaseCreatureEntity);
    }
}
