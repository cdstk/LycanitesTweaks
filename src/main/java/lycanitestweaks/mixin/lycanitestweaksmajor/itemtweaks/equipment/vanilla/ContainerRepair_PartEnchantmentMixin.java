package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerRepair.class)
public abstract class ContainerRepair_PartEnchantmentMixin {

    @Shadow public int maximumCost;

    @ModifyExpressionValue(
            method = "updateRepairOutput",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canApply(Lnet/minecraft/item/ItemStack;)Z")
    )
    public boolean lycanitesTweaks_vanillaContainerRepair_updateRepairOutputEquipmentPart(boolean original, @Local(ordinal = 0) ItemStack itemstack){
        return original || (itemstack.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants);
    }

    @Inject(
            method = "updateRepairOutput",
            at = @At(value = "FIELD", target = "Lnet/minecraft/inventory/ContainerRepair;maximumCost:I", ordinal = 8)
    )
    private void lycanitesTweaks_vanillaContainerRepair_updateRepairOutputCost(CallbackInfo ci, @Local(name = "itemstack1") ItemStack left){
        if(left.getItem() instanceof ItemEquipmentPart){
            this.maximumCost = Math.round(this.maximumCost * ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCost);
        }
    }
}
