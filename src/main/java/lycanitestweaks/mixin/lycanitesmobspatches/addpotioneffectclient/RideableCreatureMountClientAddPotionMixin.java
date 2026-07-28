package lycanitestweaks.mixin.lycanitesmobspatches.addpotioneffectclient;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.creature.EntityIoray;
import com.lycanitesmobs.core.entity.creature.EntityRoa;
import com.lycanitesmobs.core.entity.creature.EntitySalamander;
import com.lycanitesmobs.core.entity.creature.EntityThresher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityIoray.class,
        EntityRoa.class,
        EntitySalamander.class,
        EntityThresher.class
})
public abstract class RideableCreatureMountClientAddPotionMixin {

    @WrapWithCondition(
            method = "riderEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", remap = true),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsRideableCreatureMount_riderEffectsClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.getEntityWorld().isRemote;
    }

    @WrapWithCondition(
            method = "onDismounted",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", remap = true),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsRideableCreatureMount_onDismountedClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.getEntityWorld().isRemote;
    }
}

