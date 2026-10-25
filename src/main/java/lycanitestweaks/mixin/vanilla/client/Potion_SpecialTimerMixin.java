package lycanitestweaks.mixin.vanilla.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Not used, I know how to sync server -> client but I would rather get user feedback on how to display the extra properties
@Mixin(Potion.class)
public abstract class Potion_SpecialTimerMixin {

    @ModifyReturnValue(
            method = "getPotionDurationString",
            at = @At("RETURN")
    )
    private static String lycanitesTweaks_vanillaPotion_getPotionDurationStringSpecial(String original, PotionEffect effect){
        if(effect instanceof SpecialPotionEffect) {
            SpecialPotionEffect potionEffect = (SpecialPotionEffect) effect;
            boolean drawBracket = !potionEffect.getCleansable();
            boolean drawParen = !potionEffect.getPurgeable();

            if(potionEffect.getNeutral()) {
                original = "_" + original + "_";
            }

            if(drawBracket) {
                original = "[" + original;
            }
            else if(drawParen) {
                original = "(" + original;
            }

            if(drawParen) {
                original = original + ")";
            }
            else if(drawBracket) {
                original = original + "]";
            }
         }
        return original;
    }
}
