package lycanitestweaks.mixin.lycanitestweaksclient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.lycanitesmobs.core.item.equipment.ItemEquipment.PART_LIMIT;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_TooltipMixin {

    @Shadow(remap = false)
    public abstract String getFeatureSummaries(ItemStack itemStack, String featureType);

    @ModifyExpressionValue(
            method = "getAdditionalDescriptions",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;getEquipmentPartStacks(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/NonNullList;"),
            remap = false
    )
    public NonNullList<ItemStack> lycanitesTweaks_lycanitesMobsItemEquipment_getAdditionalDescriptionsShiftHidden(NonNullList<ItemStack> original){
        if(!GuiScreen.isShiftKeyDown()) return NonNullList.withSize(PART_LIMIT, ItemStack.EMPTY);
        else return original;
    }

    @Inject(
            method = "getAdditionalDescriptions",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;getFeatureSummaries(Lnet/minecraft/item/ItemStack;Ljava/lang/String;)Ljava/lang/String;", ordinal = 0),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipment_getAdditionalDescriptionsShiftTooltip(ItemStack itemStack, World world, ITooltipFlag tooltipFlag, CallbackInfoReturnable<List<String>> cir, @Local List<String> descriptions){
        if(!GuiScreen.isShiftKeyDown()) {
            String projectileSummaries = this.getFeatureSummaries(itemStack, "projectile");
            if (!projectileSummaries.isEmpty()) {
                descriptions.add("-------------------\n");
                descriptions.add(I18n.format("equipment.feature.projectile") + " " + projectileSummaries);
            }
            descriptions.add("-------------------\n");
            descriptions.add(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
            cir.setReturnValue(descriptions);
        }
    }
}
