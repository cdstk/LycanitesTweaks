package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks.aurapotionsickness;

import com.lycanitesmobs.core.entity.creature.EntityNymph;
import com.lycanitesmobs.core.entity.creature.EntityPixen;
import com.lycanitesmobs.core.entity.creature.EntityStrider;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        EntityNymph.class,
        EntityPixen.class,
        EntityStrider.class
})
public abstract class TameableEntityAuras_SicknessMixin {

    @ModifyArg(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V", ordinal = 0)
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsEntityStrider_onLivingUpdateNoPotionSickness(PotionEffect potioneffectIn){
        return new SpecialPotionEffect(potioneffectIn).setNeutral(true);
    }
}
