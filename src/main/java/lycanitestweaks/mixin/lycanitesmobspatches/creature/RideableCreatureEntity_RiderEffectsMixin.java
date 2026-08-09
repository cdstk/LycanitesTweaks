package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_RiderEffectsMixin extends TameableCreatureEntity {

    public RideableCreatureEntity_RiderEffectsMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_onLivingUpdateRehandleFireRes(EntityLivingBase instance, PotionEffect potioneffectIn, Operation<Void> original){
        // no op, move it into riderEffects()
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getActivePotionEffects()Ljava/util/Collection;")
    )
    private Collection<PotionEffect> lycanitesTweaks_lycanitesMobsRideableCreatureEntity_onLivingUpdateRehandlePotionCleanse(Collection<PotionEffect> original){
        return Collections.emptyList();
    }

    @Inject(
            method = "riderEffects",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_riderEffectsRehandler(CallbackInfo ci, @Local(argsOnly = true) EntityLivingBase rider){
        // Protect Rider from Potion Effects:
        if(!this.canBurn()) {
            rider.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, (5 * 20) + 5, 1));
        }

        List<PotionEffect> removeEffects = new ArrayList<>();
        rider.getActivePotionEffects().forEach(potionEffect -> {
            if(!this.isPotionApplicable(potionEffect))
                removeEffects.add(potionEffect);
        });
        removeEffects.forEach(potionEffect -> rider.removePotionEffect(potionEffect.getPotion()));
    }
}
