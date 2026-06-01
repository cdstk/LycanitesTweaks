package lycanitestweaks.item.interfaces;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.item.ItemStack;

public interface IItemWithCreatureInfo {

    String CREATURE_TYPE_NBT = "creatureTypeName";
    String CUSTOM_NAME_NBT = "CustomName";
    String MOB_LEVEL_NBT = "MobLevel";
    String SUBSPECIES_NBT = "Subspecies";
    String VARIANT_NBT = "Variant";
    String VARIANT_RARITY_NBT = "VariantRarity";

    String COMMON_VALUE = "COMMON";

    String getCreatureTypeName(ItemStack itemStack);
    String getCustomName(ItemStack itemStack);
    int getLevel(ItemStack itemStack);
    int getEntitySubspecies(ItemStack itemStack);
    int getEntityVariant(ItemStack itemStack);
    default String getEntityVariantRarity(ItemStack itemStack) {
        return COMMON_VALUE;
    }

    default void setAllFromCreature(ItemStack itemStack, BaseCreatureEntity creature){
        this.setCreatureTypeName(itemStack, creature.creatureInfo.getName());
        if(creature.hasCustomName()) this.setCustomName(itemStack, creature.getCustomNameTag());
        this.setLevel(itemStack, creature.getLevel());
        this.setEntitySubspecies(itemStack, creature.getSubspeciesIndex());
        this.setEntityVariant(itemStack, creature.getVariantIndex());
        if(creature.getVariant() != null) {
            this.setEntityVariantRarity(itemStack, creature.getVariant().rarity.toUpperCase());
        }
    }
    void setCreatureTypeName(ItemStack itemStack, String type);
    void setCustomName(ItemStack itemStack, String name);
    void setLevel(ItemStack itemStack, int level);
    void setEntitySubspecies(ItemStack itemStack, int index);
    void setEntityVariant(ItemStack itemStack, int index);
    default void setEntityVariantRarity(ItemStack itemStack, String rarity) {}
}
