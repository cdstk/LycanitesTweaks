package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks;

import com.lycanitesmobs.core.item.GenericFoodItem;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GenericFoodItem.class)
public abstract class GenericFoodItem_NoPotionSicknessMixin {

    @ModifyArg(
            method = "onFoodEaten",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private PotionEffect lycanitesTweaks_lycanitesMobsGenericFoodItem_onFoodEatenPotionNoPotionSickness(PotionEffect potionEffect) {
        return new SpecialPotionEffect(potionEffect).setNeutral(true);
    }
}
