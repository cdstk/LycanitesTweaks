package lycanitestweaks.item.interfaces;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.Multimap;
import lycanitestweaks.compat.BaublesHandler;
import lycanitestweaks.handlers.features.item.ConfigurableItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public interface IAttributeBauble extends IBauble {

    // TODO make more unique in next version
    String BAUBLE_MODIFIER_NAME = "Bauble modifier";

    // Above bounds = All Slots
    // Below bounds = Not a bauble
    @Optional.Method(modid = "baubles")
    default BaubleType getBaubleType(ItemStack itemStack) {
        ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemStack);
        if(slots != null) {
            if(slots.baubleTypeOrdinal < BaubleType.values().length)
                return BaubleType.values()[slots.baubleTypeOrdinal];
        }
        return BaubleType.TRINKET;
    }

    @Optional.Method(modid = "baubles")
    default void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;

        if(player instanceof EntityPlayer) {
            ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(itemstack);
            ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemstack);
            if(slots != null && stats != null && slots.baubleAttributes) {
                Multimap<String, AttributeModifier> multimap = ConfigurableItemHandler.getBaubleAttributeModifiers(stats, BAUBLE_MODIFIER_NAME);
                player.getAttributeMap().applyAttributeModifiers(multimap);
            }
        }
    }

    // Some mods like Bountiful Baubles reforger doesn't fire this
    @Optional.Method(modid = "baubles")
    default void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        if (player.world.isRemote) return;

        if(player instanceof EntityPlayer) {
            ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(itemstack);
            ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemstack);
            if(slots != null && stats != null) {
                Multimap<String, AttributeModifier> multimap = ConfigurableItemHandler.getBaubleAttributeModifiers(stats, BAUBLE_MODIFIER_NAME);
                player.getAttributeMap().removeAttributeModifiers(multimap);
            }
        }
    }

    // Actual check where below bounds = not a bauble
    @Optional.Method(modid = "baubles")
    default boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemstack);
        if(slots != null) {
            if(slots.baubleTypeOrdinal >= BaubleType.values().length)
                return false;

            if(player instanceof EntityPlayer) {
                if(BaublesHandler.getBaubleCount((EntityPlayer) player, itemstack.getItem()) >= slots.baubleCountLimit)
                    return false;
            }
        }

        return true;
    }
}
