package lycanitestweaks.mixin.lycanitestweakscore.genericbestiary;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.handlers.features.entity.GenericBestiaryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemSoulgazer.class)
public abstract class ItemSoulgazer_GenericSoulgazeMixin {

    @WrapMethod(
            method = "onItemRightClickOnEntity",
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsItemSoulgazer_onItemRightClickOnEntityGeneric(EntityPlayer player, Entity entity, ItemStack itemStack, Operation<Boolean> original){
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null) {
            if(GenericBestiaryHandler.soulgazeGenericEntity(player, entity)) return true;
        }
        return original.call(player, entity, itemStack);
    }
}
