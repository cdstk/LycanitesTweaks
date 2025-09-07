package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.container.EquipmentForgeSlot;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EquipmentForgeSlot.class)
public abstract class EquipmentForgeSlot_EnchantmentsMixin {

    @ModifyReturnValue(
            method = "isItemValid",
            at = @At(value = "RETURN", ordinal = 2)
    )
    public boolean lycanitesTweaks_lycanitesMobsEquipmentForgeSlot_isItemValid(boolean isCompleteEquipment, @Local(argsOnly = true) ItemStack itemStack){
        return isCompleteEquipment && (!itemStack.isItemEnchanted() || !ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.craftedEquipEnchPreventsDisassemble);
    }
}
