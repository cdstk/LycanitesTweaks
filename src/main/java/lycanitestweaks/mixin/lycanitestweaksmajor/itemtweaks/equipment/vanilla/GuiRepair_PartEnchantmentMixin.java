package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiRepair.class)
public abstract class GuiRepair_PartEnchantmentMixin {

    @Shadow @Final private ContainerRepair anvil;

    @ModifyConstant(
            method = "drawGuiContainerForegroundLayer",
            constant = @Constant(intValue = 40)
    )
    private int lycanitesTweaks_vanillaGuiRepair_drawGuiContainerForegroundLayerCost(int original) {
        ItemStack itemStack = this.anvil.getSlot(0).getStack();
        if(itemStack.getItem() instanceof ItemEquipmentPart || itemStack.getItem() instanceof ItemEquipment) {
            int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
            if (limit > 0) {
                return limit;
            }
        }
        return original;
    }
}
