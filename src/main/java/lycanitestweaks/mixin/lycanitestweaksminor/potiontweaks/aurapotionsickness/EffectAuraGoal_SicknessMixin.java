package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.goals.actions.abilities.EffectAuraGoal;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EffectAuraGoal.class)
public abstract class EffectAuraGoal_SicknessMixin {

    @ModifyArg(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsEffectAuraGoal_updateTaskNoPotionSickness(PotionEffect potioneffectIn){
        if(!potioneffectIn.getPotion().isBadEffect()) {
            potioneffectIn = new SpecialPotionEffect(potioneffectIn).setNeutral(true);
        }
        return potioneffectIn;
    }
}
