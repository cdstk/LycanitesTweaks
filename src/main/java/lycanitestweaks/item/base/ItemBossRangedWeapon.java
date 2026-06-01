package lycanitestweaks.item.base;

import com.google.common.collect.Multimap;
import lycanitestweaks.handlers.features.item.ConfigurableItemHandler;
import lycanitestweaks.item.ItemChargeStaff;
import lycanitestweaks.item.interfaces.IAttributeBauble;
import lycanitestweaks.item.interfaces.IItemWithCreatureInfo;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemBossRangedWeapon extends ItemChargeStaff implements IItemWithCreatureInfo, IAttributeBauble {

    public ItemBossRangedWeapon(String itemName) {
        super(itemName);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(stack);
        if (stats != null) {
            String modifierName = "Item modifier";
            if (slot == this.getEquipmentSlot(stack)) {
                if(slot == EntityEquipmentSlot.MAINHAND) {
                    if (stats.damage != 0)
                        multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, modifierName, stats.damage - 1, 0));
                    if (stats.attackSpeed != 0)
                        multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, modifierName, stats.attackSpeed - 4D, 0));
                    ConfigurableItemHandler.getMainHandAttributeModifiers(stats, modifierName, multimap);
                }
                else {
                    ConfigurableItemHandler.getOtherSlotAttributeModifiers(stats, modifierName, multimap);
                }
            }
        }

        return multimap;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
        if(repair.getItem() == Items.NETHER_STAR) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(stack);
        if(slots != null) {
            return slots.equipmentSlot;
        }
        return super.getEquipmentSlot(stack);
    }

    // Standard Item with Creature Info Stats

    public NBTTagCompound getTagCompound(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return itemStack.getTagCompound();
        }
        return new NBTTagCompound();
    }

    @Override
    public String getCreatureTypeName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CREATURE_TYPE_NBT)) return nbt.getString(CREATURE_TYPE_NBT);
        return "";
    }

    @Override
    public String getCustomName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CUSTOM_NAME_NBT)) return nbt.getString(CUSTOM_NAME_NBT);
        return "";
    }

    @Override
    public int getLevel(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(MOB_LEVEL_NBT)) return nbt.getInteger(MOB_LEVEL_NBT);
        return -1;
    }

    @Override
    public int getEntitySubspecies(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(SUBSPECIES_NBT)) return nbt.getInteger(SUBSPECIES_NBT);
        return -1;
    }

    @Override
    public int getEntityVariant(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(VARIANT_NBT)) return nbt.getInteger(VARIANT_NBT);
        return -1;
    }

    @Override
    public String getEntityVariantRarity(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(VARIANT_RARITY_NBT)) return nbt.getString(VARIANT_RARITY_NBT);
        return COMMON_VALUE;
    }

    @Override
    public void setCreatureTypeName(ItemStack itemStack, String type){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CREATURE_TYPE_NBT, type);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setCustomName(ItemStack itemStack, String name){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CUSTOM_NAME_NBT, name);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setLevel(ItemStack itemStack, int level){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(MOB_LEVEL_NBT, level);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntitySubspecies(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(SUBSPECIES_NBT, index);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntityVariant(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(VARIANT_NBT, index);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntityVariantRarity(ItemStack itemStack, String rarity){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(VARIANT_RARITY_NBT, rarity);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }
}
