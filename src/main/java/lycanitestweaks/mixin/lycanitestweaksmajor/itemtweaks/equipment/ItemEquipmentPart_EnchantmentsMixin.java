package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.EquipmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;
import java.util.Collections;

@Mixin(ItemEquipmentPart.class)
public abstract class ItemEquipmentPart_EnchantmentsMixin extends ItemBase {

    @Shadow(remap = false)
    public abstract int getLevel(ItemStack itemStack);

    @Unique
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Unique
    @Override
    @Nonnull
    public IRarity getForgeRarity(@Nonnull ItemStack stack){
        return ItemEnchantedBook.getEnchantments(stack).isEmpty() ? super.getForgeRarity(stack) : EnumRarity.UNCOMMON;
    }

    @Unique
    @Override
    public int getItemEnchantability(@Nonnull ItemStack stack){
        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantability == -1) return 1;
        return this.getLevel(stack) * ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantability;
    }

    // Used by both Enchantment Table and Anvil
    @Override
    @Unique
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment){
        return ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants
                && EquipmentUtil.canApplyEnchantmentToParts(stack, enchantment, Collections.singletonList((ItemEquipmentPart)(Object)this))
                || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
