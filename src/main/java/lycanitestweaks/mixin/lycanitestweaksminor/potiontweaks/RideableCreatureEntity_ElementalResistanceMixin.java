package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Collections;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_ElementalResistanceMixin extends TameableCreatureEntity {

    public RideableCreatureEntity_ElementalResistanceMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_onLivingUpdateRehandleFireRes(EntityLivingBase instance, PotionEffect potioneffectIn, Operation<Void> original){
        // no op, handled by overEffects
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
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_riderEffectsOwnerEffects(CallbackInfo ci){
        this.ownerEffects();
    }
}
