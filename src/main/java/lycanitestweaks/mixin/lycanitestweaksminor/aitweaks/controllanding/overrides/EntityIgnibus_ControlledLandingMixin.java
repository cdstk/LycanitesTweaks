package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.controllanding.overrides;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import lycanitestweaks.util.IBaseCreatureEntity_FlightCommandMixin;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibus_ControlledLandingMixin extends RideableCreatureEntity {

    public EntityIgnibus_ControlledLandingMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityIgnibus;isTamed()Z", ordinal = 0, remap = false)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityIgnibus_onLivingUpdateAutoFly(boolean isTamed){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            return ((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.LAND;
        }
        return false;
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityIgnibus;isInWater()Z", ordinal = 1)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityIgnibus_onLivingUpdateCommandFly(boolean isInWater){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            isInWater = isInWater || ((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.FLY;
        }
        return isInWater;
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityIgnibus;isTamed()Z", ordinal = 1, remap = false)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityIgnibus_onLivingUpdateCommandLand(boolean isTamed){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            isTamed = isTamed && ((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.LAND;
        }
        return isTamed;
    }
}
