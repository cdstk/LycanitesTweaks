package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.PotionBase;
import com.lycanitesmobs.PotionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionEffects.class)
public abstract class PotionEffects_CancelOriginalCuringMixin {

    // Cancel original handling
    @ModifyExpressionValue(
            method = "onEntityUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ObjectManager;getEffect(Ljava/lang/String;)Lcom/lycanitesmobs/PotionBase;", ordinal = 9),
            remap = false
    )
    public PotionBase lycanitesTweaks_lycanitesPotionEffects_onEntityUpdateImmunization(PotionBase original){
        return null;
    }

    @ModifyExpressionValue(
            method = "onEntityUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ObjectManager;getEffect(Ljava/lang/String;)Lcom/lycanitesmobs/PotionBase;", ordinal = 15),
            remap = false
    )
    public PotionBase lycanitesTweaks_lycanitesPotionEffects_onEntityUpdateCleansed(PotionBase original){
        return null;
    }
}
