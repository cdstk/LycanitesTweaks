package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ContainerEnchantment.class)
public abstract class ContainerEnchantment_EquipmentPartMixin {

    @WrapOperation(
            method = "enchantItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V")
    )
    public void lycanitesTweaks_vanillaContainerEnchantment_enchantItemEquipmentPart(ItemStack instance, Enchantment enchantment, int enchantmentLevel, Operation<Void> original, @Local EnchantmentData enchantmentdata){
        if(instance.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants){
            ItemEnchantedBook.addEnchantment(instance, enchantmentdata);
        }
        else {
            original.call(instance, enchantment, enchantmentLevel);
        }
    }
}
