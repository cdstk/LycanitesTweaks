package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBase_SpecialPotionMixin {

    @Shadow @Final private Map<Potion, PotionEffect> activePotionsMap;

    @Inject(
            method = "clearActivePotions",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onFinishedPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"),
            cancellable = true
    )
    private void lycanitesTweaks_vanillaEntityLivingBase_clearActivePotionsPurgeImmune(CallbackInfo ci, @Local PotionEffect effect){
        if(effect instanceof SpecialPotionEffect) {
            if(!((SpecialPotionEffect) effect).getPurgeable()) {
                ci.cancel();
            }
        }
    }

    @WrapOperation(
            method = "addPotionEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionEffect;combine(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private void lycanitesTweaks_vanillaEntityLivingBase_addPotionEffectCombineSpecial(PotionEffect current, PotionEffect other, Operation<Void> original, @Local(name = "potioneffect") LocalRef<PotionEffect> potionRef){
        Boolean upgrade = null;
        if(!(current instanceof SpecialPotionEffect) && other instanceof SpecialPotionEffect) {
            upgrade = true;
        }
        else if(current instanceof SpecialPotionEffect && !(other instanceof SpecialPotionEffect)) {
            upgrade = false;
        }

        boolean replace = false;

        if(upgrade != null) {
            if (other.getAmplifier() > current.getAmplifier()) {
                replace = true;
            }
            else if (other.getAmplifier() == current.getAmplifier() && current.getDuration() < other.getDuration()) {
                replace = true;
            }
        }

        if(replace) {
            PotionEffect replacement = upgrade
                    ? new SpecialPotionEffect(current)
                    : new PotionEffect(current);

            original.call(replacement, other);
            potionRef.set(replacement);
            this.activePotionsMap.replace(current.getPotion(), replacement);
        }
        else {
            original.call(current, other);
        }
    }

    @WrapOperation(
            method = "removePotionEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;removeActivePotionEffect(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;")
    )
    private PotionEffect lycanitesTweaks_vanillaEntityLivingBase_removePotionEffectCleanseImmune(EntityLivingBase instance, Potion potioneffectin, Operation<PotionEffect> original){
        PotionEffect potionEffect = instance.getActivePotionEffect(potioneffectin);
        if(potionEffect instanceof SpecialPotionEffect) {
            SpecialPotionEffect specialPotionEffect = (SpecialPotionEffect) potionEffect;
            if(!specialPotionEffect.getCleansable()) {
                return null;
            }
        }
        return original.call(instance, potioneffectin);
    }

    @Inject(
            method = "curePotionEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;onFinishedPotionEffect(Lnet/minecraft/potion/PotionEffect;)V"),
            cancellable = true
    )
    private void lycanitesTweaks_vanillaEntityLivingBase_curePotionEffectsCleanseImmune(CallbackInfo ci, @Local PotionEffect effect){
        if(effect instanceof SpecialPotionEffect) {
            if(!((SpecialPotionEffect) effect).getCleansable()) {
                ci.cancel();
            }
        }
    }
}
