package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_PickupMixin extends EntityLiving {

    @Shadow(remap = false)
    public EntityLivingBase pickupEntity;

    public BaseCreatureEntity_PickupMixin(World worldIn) {
        super(worldIn);
    }

    // Use config value
    @ModifyConstant(
            method = "onLivingUpdate",
            constant = @Constant(doubleValue = 32.0D)
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateConfigPickUpDistance(double constant){
        return Helpers.getAutoDropPickupDistance((BaseCreatureEntity)(Object)this, this.pickupEntity);
    }

    // Fix calculation (was sqrt sqrt)
    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D")
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateDistanceCalc(double original){
        return this.getDistance(this.pickupEntity);
    }

    @ModifyReturnValue(
            method = "canPickupEntity",
            at = @At("RETURN"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityCheckRange(boolean original, EntityLivingBase entity){
        return original && this.getDistance(entity) <= Helpers.getAutoDropPickupDistance((BaseCreatureEntity)(Object)this, entity);
    }
}
