package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.consumable.ItemCleansingCrystal;
import com.lycanitesmobs.core.item.consumable.ItemImmunizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {ItemCleansingCrystal.class, ItemImmunizer.class})
public abstract class ItemCuring_CooldownMixin extends ItemBase {

    @WrapOperation(
            method = "onItemRightClick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    private void lycanitesTweaks_lycanitesItemCuring_onItemRightClickCooldown(EntityPlayer player, PotionEffect potionEffect, Operation<Void> original){
        original.call(player, potionEffect);
//        player.playSound(SoundEvents.BLOCK_GLASS_BREAK, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
        if(player.isPotionActive(potionEffect.getPotion())){
            player.getCooldownTracker().setCooldown(this, 5);
        }
    }
}
