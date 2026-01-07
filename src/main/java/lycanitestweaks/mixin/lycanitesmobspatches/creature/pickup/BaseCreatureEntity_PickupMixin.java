package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.LycanitesEntityUtil;
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
        return LycanitesEntityUtil.getAutoDropPickupDistance((BaseCreatureEntity)(Object)this, this.pickupEntity);
    }

    // Fix calculation (was sqrt sqrt)
    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D")
    )
    private double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateDistanceCalc(double original){
        return this.getDistance(this.pickupEntity);
    }
}
