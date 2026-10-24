package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibus_SicknessMixin {

    @ModifyArg(
            method = "riderEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsEntityIgnibus_riderEffectsNoPotionSickness(PotionEffect potioneffectIn){
        return new SpecialPotionEffect(potioneffectIn).setNeutral(true);
    }
}
