package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.container.BaseSlot;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import com.lycanitesmobs.core.item.ChargeItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BaseSlot.class)
public abstract class BaseSlot_AdvancedArmorMixin extends Slot {

    public BaseSlot_AdvancedArmorMixin(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @ModifyReturnValue(
            method = "isItemValid",
            at = @At("RETURN")
    )
    public boolean lycanitesTweaks_lycanitesMobsBaseSlot_isItemValidChargeInHands(boolean isValid, ItemStack itemStack){
        if(isValid) {
            if(itemStack != null && this.inventory instanceof InventoryCreature) {
                String type = ((InventoryCreature) this.inventory).getTypeFromSlot(this.getSlotIndex());
                if(type != null) {
                    switch (type) {
                        case "weapon":
                        case "offhand":
                            return !(itemStack.getItem() instanceof ChargeItem);
                        default:
                    }
                }
            }
        }
        return isValid;
    }

    @ModifyConstant(
            method = "getSlotStackLimit",
            constant = @Constant(intValue = 1)
    )
    public int lycanitesTweaks_lycanitesMobsBaseSlot_getSlotStackLimitHandStack(int constant){
        String type = ((InventoryCreature) this.inventory).getTypeFromSlot(this.getSlotIndex());
        switch (type){
            case "weapon":
            case "offhand": return 64;
            default:
        }
        return constant;
    }
}
