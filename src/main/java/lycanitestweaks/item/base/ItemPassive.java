package lycanitestweaks.item.base;

import com.google.common.collect.Multimap;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItemHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.features.item.ConfigurableItemHandler;
import lycanitestweaks.item.interfaces.IAttributeBauble;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

@Optional.Interface(iface = "lycanitestweaks.item.interfaces.IAttributeBauble", modid = ModLoadedUtil.BAUBLES_MODID, striprefs = true)
public abstract class ItemPassive extends ItemBase implements IAttributeBauble {

    public static final String TOGGLE_CAP = LycanitesTweaks.MODID + "ToggleableSync";

    public ItemPassive(String modid, String name) {
        super(modid, name);
    }

    public ItemPassive(String name) {
        this(LycanitesTweaks.MODID, name);
    }

    public boolean isToggleable() {
        return false;
    }

    public void tickAbility(ItemStack itemstack, EntityLivingBase entity) {
        this.tickAbility(entity);
    }

    public void tickAbility(EntityLivingBase entity) {

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
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(stack);
        if(slots != null) {
            return slots.equipmentSlot;
        }
        return super.getEquipmentSlot(stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if(entityIn instanceof EntityLivingBase) {
            this.tickAbility(stack, (EntityLivingBase) entityIn);
        }
    }

    @Optional.Method(modid = "baubles")
    public void onWornTick(ItemStack itemStack, EntityLivingBase player) {
        this.tickAbility(itemStack, player);
    }

    @Override
    @Nullable
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound nbt = super.getNBTShareTag(stack);
        if(nbt != null) {
            ToggleableItem toggleableItem = ToggleableItem.getForItemStack(stack);
            if (toggleableItem != null) {
                NBTBase capTag = ToggleableItemHandler.TOGGLE_ITEM.writeNBT(toggleableItem, null);
                if (capTag != null) {
                    NBTTagCompound newTag = nbt.copy();
                    newTag.setTag(TOGGLE_CAP, capTag);
                    return newTag;
                }
            }
        }
        return nbt;
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if(nbt != null && nbt.hasKey(TOGGLE_CAP)) {
            NBTTagCompound capTag = nbt.getCompoundTag(TOGGLE_CAP);
            ToggleableItem toggleableItem = ToggleableItem.getForItemStack(stack);
            if(toggleableItem != null) {
                ToggleableItemHandler.TOGGLE_ITEM.readNBT(toggleableItem, null, capTag);
                nbt.removeTag(TOGGLE_CAP);
            }
        }
        super.readNBTShareTag(stack, nbt);
    }
}
