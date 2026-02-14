package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.item.special.ItemSoulstone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSoulstone.class)
public abstract class ItemSoulstone_BindInvalidMixin {

    @Inject(
            method = "onItemRightClickOnEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;isTameable()Z"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesItemSoulstone_onItemRightClickOnEntityMinion(EntityPlayer player, Entity entity, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir, @Local TameableCreatureEntity entityTameable){
        if(entityTameable.isMinion()){
            if (!player.getEntityWorld().isRemote) {
                player.sendMessage(new TextComponentTranslation("message.soulstone.invalid"));
            }
            cir.setReturnValue(false);
        }
    }
}
