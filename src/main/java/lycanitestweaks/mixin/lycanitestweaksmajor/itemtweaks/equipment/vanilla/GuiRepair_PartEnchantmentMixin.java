package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiRepair.class)
public abstract class GuiRepair_PartEnchantmentMixin {

    @Shadow @Final private ContainerRepair anvil;
    @Shadow @Final private InventoryPlayer playerInventory;

    // Enchantment Control cope
    @ModifyConstant(
            method = "drawGuiContainerForegroundLayer",
            constant = @Constant(intValue = 8453920)
    )
    private int lycanitesTweaks_vanillaGuiRepair_drawGuiContainerForegroundLayerPartColor(int constant){
        if(!this.playerInventory.player.capabilities.isCreativeMode) {
            ItemStack itemStack = this.anvil.getSlot(2).getStack();
            if (itemStack.getItem() instanceof ItemEquipmentPart || itemStack.getItem() instanceof ItemEquipment) {
                int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
                if (limit > 0) {
                    return 16736352;
                }
            }
        }
        return constant;
    }

    @ModifyExpressionValue(
            method = "drawGuiContainerForegroundLayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/I18n;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", ordinal = 1)
    )
    private String lycanitesTweaks_vanillaGuiRepair_drawGuiContainerForegroundLayerPartString(String original) {
        if(!this.playerInventory.player.capabilities.isCreativeMode) {
            ItemStack itemStack = this.anvil.getSlot(2).getStack();
            if (itemStack.getItem() instanceof ItemEquipmentPart || itemStack.getItem() instanceof ItemEquipment) {
                int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
                if (limit > 0) {
                    return I18n.format("container.repair.expensive");
                }
            }
        }
        return original;
    }

    // Vanilla
    @ModifyConstant(
            method = "drawGuiContainerForegroundLayer",
            constant = @Constant(intValue = 40)
    )
    private int lycanitesTweaks_vanillaGuiRepair_drawGuiContainerForegroundLayerCost(int original) {
        ItemStack itemStack = this.anvil.getSlot(2).getStack();
        if (itemStack.getItem() instanceof ItemEquipmentPart || itemStack.getItem() instanceof ItemEquipment) {
            int limit = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantingCostLimit;
            if (limit > 0) {
                return limit;
            }
        }
        return original;
    }
}