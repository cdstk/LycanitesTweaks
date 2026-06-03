package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.inventory.ContainerRepair$2")
public abstract class ContainerRepairSlot_PartEnchantmentMixin extends Slot {

    @Shadow(remap = false) @Final ContainerRepair this$0;

    public ContainerRepairSlot_PartEnchantmentMixin(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @ModifyReturnValue(
            method = "canTakeStack",
            at = @At("RETURN")
    )
    private boolean lycanitesTweaks_vanillaContainerRepair$2_canTakeStackPart(boolean original, EntityPlayer playerIn){
        ItemStack output = this.getStack();
        if(output.getItem() instanceof ItemEquipmentPart || output.getItem() instanceof ItemEquipment){
            int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
            if (limit > 0) {
                if(this$0.maximumCost >= limit && !playerIn.capabilities.isCreativeMode) {
                    return false;
                }
            }
        }
        return original;
    }
}
