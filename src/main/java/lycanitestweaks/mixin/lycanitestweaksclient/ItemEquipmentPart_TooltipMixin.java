package lycanitestweaks.mixin.lycanitestweaksclient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import com.lycanitesmobs.core.item.equipment.features.EquipmentFeature;
import com.lycanitesmobs.core.item.equipment.features.SlotEquipmentFeature;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemEquipmentPart.class)
public abstract class ItemEquipmentPart_TooltipMixin {

    @ModifyExpressionValue(
            method = "getAdditionalDescriptions",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/features/EquipmentFeature;getDescription(Lnet/minecraft/item/ItemStack;I)Ljava/lang/String;"),
            remap = false
    )
    public String lycanitesTweaks_lycanitesMobsItemEquipmentPart_getAdditionalDescriptionsShiftHidden(String original, @Local EquipmentFeature feature){
        return GuiScreen.isShiftKeyDown() ? original : (feature instanceof SlotEquipmentFeature) ? original : null;
    }

    @Inject(
            method = "getAdditionalDescriptions",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipmentPart_getAdditionalDescriptionsShiftTooltip(ItemStack itemStack, World world, ITooltipFlag tooltipFlag, CallbackInfoReturnable<List<String>> cir, @Local List<String> descriptions){
        if(!GuiScreen.isShiftKeyDown()) descriptions.add(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
    }
}
