package lycanitestweaks.mixin.lycanitesmobspatches.addpotioneffectclient;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibusClientAddPotionMixin {

    @WrapWithCondition(
            method = "riderEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", remap = true),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityIgnibus_riderEffectsClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.getEntityWorld().isRemote;
    }
}
