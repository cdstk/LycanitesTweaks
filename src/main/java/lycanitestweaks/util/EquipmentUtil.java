package lycanitestweaks.util;

import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import com.lycanitesmobs.core.item.equipment.features.HarvestEquipmentFeature;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.SMEHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EquipmentUtil {

    /**
     * Checks part level -> black list -> if Weapon enchantment OR check modded enchantment conditions. Super call is important for SME
     */
    public static boolean canApplyEnchantmentToParts(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment, List<ItemEquipmentPart> equipmentParts){
        if(enchantment.type == EnumEnchantmentType.ALL) return true;

        if(!arePartLevelsValid(stack)) return false;
        if(ForgeConfigProvider.getEquipmentEnchantmentBlacklist().contains(enchantment)) return false;

        if(enchantment == Enchantments.UNBREAKING) return true;
        if(enchantment.type == EnumEnchantmentType.BREAKABLE && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentMending) return true;

        Set<String> featureTypeSet = new HashSet<>();
        for(ItemEquipmentPart equipmentPart : equipmentParts) {
            equipmentPart.features.forEach(equipmentFeature -> {
                if(equipmentFeature instanceof HarvestEquipmentFeature) featureTypeSet.add(((HarvestEquipmentFeature) equipmentFeature).harvestType);
                featureTypeSet.add(equipmentFeature.featureType);
            });
        }

        if(enchantment.type == EnumEnchantmentType.WEAPON && featureTypeSet.contains("damage")) return true;
        if(enchantment.type == EnumEnchantmentType.DIGGER && featureTypeSet.contains("harvest")) return true;
        if(ModLoadedUtil.isSMETypesLoaded() && SMEHandler.doesEquipmentHaveType(enchantment, featureTypeSet)) return true;

        return false;
    }

    public static boolean arePartLevelsValid(ItemStack stack) {
        int minimum = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.minimumPartLevelForEnchants;
        if(minimum <= 0) return true;

        if(stack.getItem() instanceof ItemEquipment) {
            ItemEquipment equipmentItem = (ItemEquipment) stack.getItem();
            for (ItemStack equipmentPartStack : equipmentItem.getEquipmentPartStacks(stack)) {
                ItemEquipmentPart equipmentPart = equipmentItem.getEquipmentPart(equipmentPartStack);
                if (equipmentPart != null) {
                    if (equipmentPart.getLevel(equipmentPartStack) < minimum) {
                        return false;
                    }
                }
            }
            return true;
        }
        else if(stack.getItem() instanceof ItemEquipmentPart) {
            return ((ItemEquipmentPart)stack.getItem()).getLevel(stack) >= minimum;
        }
        return false;
    }
}
