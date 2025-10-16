package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.client;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.EquipmentUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemEquipmentPart.class)
public abstract class ItemEquipmentPart_TooltipMixin extends ItemBase {

    @Inject(
            method = "addInformation",
            at = @At("TAIL")
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipmentPart_addInformationStoredEnchantments(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag tooltipFlag, CallbackInfo ci){
        if(!ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants) return;

        NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(itemStack);

        if(!EquipmentUtil.arePartLevelsValid(itemStack)){
            if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.minimumPartLevelForEnchantsTooltip)
                tooltip.add(I18n.format("item.equipment.description.mixin.enchrequirement", ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.minimumPartLevelForEnchants));
        }
        else {
            if(nbttaglist.isEmpty()) tooltip.add(I18n.format("item.equipment.description.mixin.enchantable"));
        }

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getShort("id");
            Enchantment enchantment = Enchantment.getEnchantmentByID(j);

            if (enchantment != null) {
                tooltip.add(enchantment.getTranslatedName(nbttagcompound.getShort("lvl")));
            }
        }
    }
}
