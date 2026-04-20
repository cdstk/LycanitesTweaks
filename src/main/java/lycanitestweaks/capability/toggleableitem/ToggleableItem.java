package lycanitestweaks.capability.toggleableitem;

import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketToggleableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ToggleableItem implements IToggleableItem {

    public static final String TAG_NAME = "ToggleableItem";
    public static final String NBT_ENABLED = "Enabled";
    public static final String NBT_MODE = "Mode";

    private boolean enabled = false;
    private int mode = 0;
    private ItemStack itemStack = ItemStack.EMPTY;

    ToggleableItem() {}
    ToggleableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static IToggleableItem getForItemStack(ItemStack itemStack) {
        ToggleableItem toggleableItem = itemStack.getCapability(ToggleableItemHandler.TOGGLE_ITEM, null);

        // Client Property Override needed a late nbt sync for some reason
        if(toggleableItem != null && itemStack.hasTagCompound())
            toggleableItem.readNBT(itemStack.getTagCompound());

        return toggleableItem;
    }

    @Override
    public boolean isAbilityToggled() {
        return this.enabled;
    }

    @Override
    public int getToggleMode() {
        return this.mode;
    }

    @Override
    public void toggleAbility(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void toggleAbility(boolean enabled, ItemStack itemStack, EntityPlayer player) {
        this.toggleAbility(enabled);
        this.sync(itemStack, player);
    }

    @Override
    public void toggleMode(int value) {
        this.mode = value;
    }

    @Override
    public void toggleMode(int value, ItemStack itemStack, EntityPlayer player) {
        this.toggleMode(value);
        this.sync(itemStack, player);
    }

    @Override
    public void readNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = nbtTagCompound.getCompoundTag(TAG_NAME);

        if(extTagCompound.hasKey(NBT_ENABLED))
            this.toggleAbility(extTagCompound.getBoolean(NBT_ENABLED));
        if(extTagCompound.hasKey(NBT_MODE))
            this.toggleMode(extTagCompound.getInteger(NBT_MODE));
    }

    @Override
    public void writeNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = new NBTTagCompound();

        extTagCompound.setBoolean(NBT_ENABLED, this.isAbilityToggled());
        extTagCompound.setInteger(NBT_MODE, this.getToggleMode());

        nbtTagCompound.setTag(TAG_NAME, extTagCompound);
    }

    @Override
    public void sync(ItemStack itemStack, EntityPlayer player) {
        if(!player.world.isRemote) {
            if(player instanceof EntityPlayerMP) {
                NBTTagCompound tagCompound = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
                this.writeNBT(tagCompound);
                itemStack.setTagCompound(tagCompound);
                PacketToggleableItem packetToggleableItem = new PacketToggleableItem(false, itemStack, this.isAbilityToggled(), this.getToggleMode());
                PacketHandler.instance.sendToAllTracking(packetToggleableItem, player);
            }
        }
    }
}
