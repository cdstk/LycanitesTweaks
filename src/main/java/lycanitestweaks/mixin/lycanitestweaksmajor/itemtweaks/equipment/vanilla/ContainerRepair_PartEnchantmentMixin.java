package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerRepair.class)
public abstract class ContainerRepair_PartEnchantmentMixin {

    @ModifyExpressionValue(
            method = "updateRepairOutput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canApply(Lnet/minecraft/item/ItemStack;)Z")
    )
    public boolean lycanitesTweaks_vanillaContainerRepair_updateRepairOutputEquipmentPart(boolean original, @Local(ordinal = 0) ItemStack itemstack){
        return original || (itemstack.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants);
    }
}
