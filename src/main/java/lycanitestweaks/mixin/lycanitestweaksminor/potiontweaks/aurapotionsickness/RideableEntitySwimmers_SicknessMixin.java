package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.creature.EntityIoray;
import com.lycanitesmobs.core.entity.creature.EntityRoa;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        EntityRoa.class,
        EntityIoray.class
})
public abstract class RideableEntitySwimmers_SicknessMixin {

    @ModifyArg(
            method = "riderEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", ordinal = 0)
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsRideableCreatureEntitys_riderEffectsNoPotionSickness(PotionEffect potioneffectIn){
        return new SpecialPotionEffect(potioneffectIn).setNeutral(true);
    }

    @ModifyArg(
            method = "onDismounted",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", ordinal = 0)
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsRideableCreatureEntitys_onDismountedNoPotionSickness(PotionEffect potioneffectIn){
        return new SpecialPotionEffect(potioneffectIn).setNeutral(true);
    }
}
