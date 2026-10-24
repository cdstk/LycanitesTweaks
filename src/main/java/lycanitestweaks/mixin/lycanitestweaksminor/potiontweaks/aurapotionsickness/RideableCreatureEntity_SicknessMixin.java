package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_SicknessMixin {

    @ModifyArg(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsRideableCreatureEntity_onLivingUpdateNoPotionSickness(PotionEffect potioneffectIn){
        return new SpecialPotionEffect(potioneffectIn).setNeutral(true);
    }
}
