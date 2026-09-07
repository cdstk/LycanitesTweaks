package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_PickupMixin extends EntityLiving {

    @Shadow(remap = false)
    public CreatureInfo creatureInfo;
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
        if(this.isRiding()) {
            return 0D;
        }
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

    @Inject(
            method = "canBeRidden",
            at = @At("HEAD"),
            cancellable = true
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canBeRiddenFearCant(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(this.creatureInfo.dummy)
            cir.setReturnValue(false);
    }

    @Inject(
            method = "canPickupEntity",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityCurrentlyRiding(EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir){
        if(this.isRiding())
            cir.setReturnValue(false);
    }
}
