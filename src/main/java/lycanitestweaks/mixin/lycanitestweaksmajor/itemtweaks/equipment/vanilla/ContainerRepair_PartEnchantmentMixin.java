package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContainerRepair.class, priority = 1001) // Run after EnchantmentControl
public abstract class ContainerRepair_PartEnchantmentMixin {

    @Shadow public int maximumCost;

    @Inject(
            method = "updateRepairOutput",
            at = @At(value = "FIELD", target = "Lnet/minecraft/inventory/ContainerRepair;maximumCost:I", ordinal = 8)
    )
    private void lycanitesTweaks_vanillaContainerRepair_updateRepairOutputCost(CallbackInfo ci, @Local(name = "itemstack1") ItemStack left){
        if(left.getItem() instanceof ItemEquipmentPart || left.getItem() instanceof ItemEquipment){
            this.maximumCost = Math.round(this.maximumCost * ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCost);
        }
    }

    @ModifyConstant(
            method = "updateRepairOutput",
            constant = @Constant(intValue = 40, ordinal = 2)
    )
    private int lycanitesTweaks_vanillaContainerRepair_updateRepairOutputCostLimit(int maxCostLimit, @Local(name = "itemstack1") ItemStack left){
        if(left.getItem() instanceof ItemEquipmentPart || left.getItem() instanceof ItemEquipment){
            int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
            if (limit > 0) {
                return limit;
            }
        }
        return maxCostLimit;
    }
}
