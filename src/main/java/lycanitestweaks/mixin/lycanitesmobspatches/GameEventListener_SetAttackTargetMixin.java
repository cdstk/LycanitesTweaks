package lycanitestweaks.mixin.lycanitesmobspatches;

import com.lycanitesmobs.GameEventListener;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameEventListener.class)
public abstract class GameEventListener_SetAttackTargetMixin {

    @Inject(
            method = "onAttackTarget",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingSetAttackTargetEvent;isCancelable()Z", ordinal = 0),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsGameEventListener_onAttackTargetInvisible(LivingSetAttackTargetEvent event, CallbackInfo ci){
        if(!ForgeConfigHandler.mixinPatchesConfig.fixCanBeTargetedLegacy) {
            ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
            ci.cancel();
        }
    }

    @Inject(
            method = "onAttackTarget",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingSetAttackTargetEvent;isCancelable()Z", ordinal = 1),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsGameEventListener_onAttackTargetCreatureTrait(LivingSetAttackTargetEvent event, CallbackInfo ci){
        ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
        ci.cancel();
    }
}
