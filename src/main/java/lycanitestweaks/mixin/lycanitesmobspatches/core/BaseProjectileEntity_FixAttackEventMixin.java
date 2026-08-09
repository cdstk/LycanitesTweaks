package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseProjectileEntity.class)
public abstract class BaseProjectileEntity_FixAttackEventMixin {

    @WrapOperation(
            method = "canDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/EventBus;post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseProjectileEntity_canDamageNoPlayerAttackEntityEvent(EventBus instance, Event event, Operation<Boolean> original){
        return false;
    }
}
