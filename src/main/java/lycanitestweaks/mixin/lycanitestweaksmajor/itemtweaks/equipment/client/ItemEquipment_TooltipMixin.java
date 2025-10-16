package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.client;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.EquipmentUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_TooltipMixin extends ItemBase {

    @Inject(
            method = "addInformation",
            at = @At("TAIL")
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipment_addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag tooltipFlag, CallbackInfo ci){
        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants) return;
        if(!EquipmentUtil.arePartLevelsValid(itemStack)){
            if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.minimumPartLevelForEnchantsTooltip)
                tooltip.add(I18n.format("item.equipment.description.mixin.enchrequirement", ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.minimumPartLevelForEnchants));
        }
        else {
            if(itemStack.isItemEnchanted()){
                if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.enchantsPreventDisassemble) tooltip.add(I18n.format("item.equipment.description.mixin.enchlock"));
                else tooltip.add(I18n.format("item.equipment.description.mixin.enchremove"));
            }
            else tooltip.add(I18n.format("item.equipment.description.mixin.enchantable"));
        }
    }
}
