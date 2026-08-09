package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.creature.EntityCockatrice;
import com.lycanitesmobs.core.entity.creature.EntityPinky;
import com.lycanitesmobs.core.entity.creature.EntitySalamander;
import com.lycanitesmobs.core.entity.creature.EntityShade;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityCockatrice.class,
        EntityPinky.class,
        EntitySalamander.class,
        EntityShade.class
})
public abstract class RideableCreatureEntitys_SpecialAttackFixAttackEventMixins {

    @WrapOperation(
            method = "specialAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/eventhandler/EventBus;post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsRideableCreatureEntitys_specialAttackNoPlayerAttackEntityEvent(EventBus instance, Event event, Operation<Boolean> original){
        return false;
    }
}
