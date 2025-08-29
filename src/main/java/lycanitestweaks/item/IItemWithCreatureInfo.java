package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.item.ItemStack;

public interface IItemWithCreatureInfo {

    String getCreatureTypeName(ItemStack itemStack);
    String getCustomName(ItemStack itemStack);
    int getLevel(ItemStack itemStack);
    int getEntitySubspecies(ItemStack itemStack);
    int getEntityVariant(ItemStack itemStack);

    default void setAllFromCreature(ItemStack itemStack, BaseCreatureEntity creature){
        this.setCreatureTypeName(itemStack, creature.creatureInfo.getName());
        if(creature.hasCustomName()) this.setCustomName(itemStack, creature.getCustomNameTag());
        this.setLevel(itemStack, creature.getLevel());
        this.setEntitySubspecies(itemStack, creature.getSubspeciesIndex());
        this.setEntityVariant(itemStack, creature.getVariantIndex());
    }
    void setCreatureTypeName(ItemStack itemStack, String type);
    void setCustomName(ItemStack itemStack, String name);
    void setLevel(ItemStack itemStack, int level);
    void setEntitySubspecies(ItemStack itemStack, int index);
    void setEntityVariant(ItemStack itemStack, int index);
}
