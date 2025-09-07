package lycanitestweaks.util;

import lycanitestweaks.item.IItemWithCreatureInfo;
import net.minecraft.item.ItemStack;

public interface IItemBase_WithCreatureInfoMixin extends IItemWithCreatureInfo {

    // This is stupid, but I don't care

    String lycanitesTweaks$getCreatureTypeName(ItemStack itemStack);
    String lycanitesTweaks$getCustomName(ItemStack itemStack);
    int lycanitesTweaks$getLevel(ItemStack itemStack);
    int lycanitesTweaks$getEntitySubspecies(ItemStack itemStack);
    int lycanitesTweaks$getEntityVariant(ItemStack itemStack);

    @Override
    default String getCreatureTypeName(ItemStack itemStack){ return this.lycanitesTweaks$getCreatureTypeName(itemStack); }
    @Override
    default String getCustomName(ItemStack itemStack) { return this.lycanitesTweaks$getCustomName(itemStack); }
    @Override
    default int getLevel(ItemStack itemStack){ return this.lycanitesTweaks$getLevel(itemStack); }
    @Override
    default int getEntitySubspecies(ItemStack itemStack){ return this.lycanitesTweaks$getEntitySubspecies(itemStack); }
    @Override
    default int getEntityVariant(ItemStack itemStack){ return this.lycanitesTweaks$getEntityVariant(itemStack); }
    
    void lycanitesTweaks$setCreatureTypeName(ItemStack itemStack, String type);
    void lycanitesTweaks$setCustomName(ItemStack itemStack, String name);
    void lycanitesTweaks$setLevel(ItemStack itemStack, int level);
    void lycanitesTweaks$setEntitySubspecies(ItemStack itemStack, int index);
    void lycanitesTweaks$setEntityVariant(ItemStack itemStack, int index);

    @Override
    default void setCreatureTypeName(ItemStack itemStack, String type){ this.lycanitesTweaks$setCreatureTypeName(itemStack, type); }
    @Override
    default void setCustomName(ItemStack itemStack, String name){ this.lycanitesTweaks$setCustomName(itemStack, name); }
    @Override
    default void setLevel(ItemStack itemStack, int level){ this.lycanitesTweaks$setLevel(itemStack, level); }
    @Override
    default void setEntitySubspecies(ItemStack itemStack, int index){ this.lycanitesTweaks$setEntitySubspecies(itemStack, index); }
    @Override
    default void setEntityVariant(ItemStack itemStack, int index){ this.lycanitesTweaks$setEntityVariant(itemStack, index); }
}
