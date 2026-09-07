package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.controllanding;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAfrit;
import com.lycanitesmobs.core.entity.creature.EntityArix;
import com.lycanitesmobs.core.entity.creature.EntityCockatrice;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import com.lycanitesmobs.core.entity.creature.EntityMorock;
import com.lycanitesmobs.core.entity.creature.EntityPixen;
import com.lycanitesmobs.core.entity.creature.EntityQuetzodracl;
import com.lycanitesmobs.core.entity.creature.EntityRaiko;
import lycanitestweaks.util.IBaseCreatureEntity_FlightCommandMixin;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = {
        EntityAfrit.class,
        EntityArix.class,
        EntityCockatrice.class,
        EntityIgnibus.class,
        EntityMorock.class,
        EntityPixen.class,
        EntityQuetzodracl.class,
        EntityRaiko.class
})
public abstract class TameableCreatureEntityLandableFly_ControlledLandingMixin extends TameableCreatureEntity {

    public TameableCreatureEntityLandableFly_ControlledLandingMixin(World world) {
        super(world);
    }

    @ModifyConstant(
            method = "onLivingUpdate",
            constant = @Constant(longValue = 100L, ordinal = 0)
    )
    private long lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_onLivingUpdateCommandFly(long constant){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            if(((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.FLY)
                return 1L;
        }
        return constant;
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;nextBoolean()Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_onLivingUpdateCommandFly(boolean original){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            if(((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.FLY)
                return true;
        }
        return original;
    }

    @ModifyConstant(
            method = "onLivingUpdate",
            constant = @Constant(longValue = 100L, ordinal = 1)
    )
    private long lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_onLivingUpdateCommandLand(long constant){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            if(((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.LAND)
                return 1L;
        }
        return constant;
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;nextBoolean()Z", ordinal = 1)
    )
    private boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_onLivingUpdateCommandLand(boolean original){
        if(this instanceof IBaseCreatureEntity_FlightCommandMixin) {
            if(((IBaseCreatureEntity_FlightCommandMixin) this).lycanitesTweaks$getFlightCommand() == IBaseCreatureEntity_FlightCommandMixin.COMMAND.LAND)
                return true;
        }
        return original;
    }
}
