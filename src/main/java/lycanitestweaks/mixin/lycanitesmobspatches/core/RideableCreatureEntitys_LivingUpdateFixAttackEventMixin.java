package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.creature.EntityBarghest;
import com.lycanitesmobs.core.entity.creature.EntityFeradon;
import com.lycanitesmobs.core.entity.creature.EntityMaug;
import com.lycanitesmobs.core.entity.creature.EntityWarg;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityBarghest.class,
        EntityFeradon.class,
        EntityMaug.class,
        EntityWarg.class
})
public abstract class RideableCreatureEntitys_LivingUpdateFixAttackEventMixin {

    @WrapOperation(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/EventBus;post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsRideableCreatureEntitys_onLivingUpdateNoPlayerAttackEntityEvent(EventBus instance, Event event, Operation<Boolean> original){
        return false;
    }
}
