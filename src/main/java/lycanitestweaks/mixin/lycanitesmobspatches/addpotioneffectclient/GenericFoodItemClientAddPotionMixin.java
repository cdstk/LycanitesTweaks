package lycanitestweaks.mixin.lycanitesmobspatches.addpotioneffectclient;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.item.GenericFoodItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GenericFoodItem.class)
public abstract class GenericFoodItemClientAddPotionMixin {

    @WrapWithCondition(
            method = "onFoodEaten",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    public boolean lycanitesTweaks_lycanitesMobsGenericFoodItem_onFoodEatenClientPotion(EntityPlayer instance, PotionEffect potionEffect){
        return !instance.getEntityWorld().isRemote;
    }
}
