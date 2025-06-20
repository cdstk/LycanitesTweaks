package lycanitestweaks.mixin.lycanitestweakscore.enchantedsoulkey;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.mobevent.trigger.AltarMobEventTrigger;
import lycanitestweaks.item.ItemEnchantedSoulkey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AltarMobEventTrigger.class)
public abstract class AltarMobEventTriggerEnchantedMixin {

    @ModifyArg(
            method = "onActivate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/mobevent/trigger/AltarMobEventTrigger;trigger(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;II)Z"),
            index = 3,
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsAltarMobEventTrigger_onActivateEnchantedLevels(int level, @Local(argsOnly = true) Entity entity){
        if(entity instanceof EntityPlayer && ((EntityPlayer) entity).getHeldItemMainhand().getItem() instanceof ItemEnchantedSoulkey){
            ItemStack itemStack = ((EntityPlayer) entity).getHeldItemMainhand();
            return level + ((ItemEnchantedSoulkey) itemStack.getItem()).getLevel(itemStack) - 1;
        }
        return level;
    }
}
