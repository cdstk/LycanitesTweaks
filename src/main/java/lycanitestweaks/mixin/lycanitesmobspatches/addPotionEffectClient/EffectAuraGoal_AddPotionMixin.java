package lycanitestweaks.mixin.lycanitesmobspatches.addPotionEffectClient;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.goals.actions.abilities.EffectAuraGoal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectAuraGoal.class)
public abstract class EffectAuraGoal_AddPotionMixin {

    @WrapWithCondition(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private boolean lycanitesTweaks_lycanitesMobsEffectAuraGoal_updateTaskClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.world.isRemote;
    }
}
