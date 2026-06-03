package lycanitestweaks.capability.toggleableitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class ToggleableItem implements IToggleableItem {

    public static final String TAG_NAME = "ToggleableItem";
    public static final String NBT_ENABLED = "Enabled";
    public static final String NBT_MODE = "Mode";

    private boolean enabled = false;
    private int mode = 0;
    private ItemStack itemStack = ItemStack.EMPTY;
    private NBTTagCompound cachedNBT;

    public ToggleableItem() {}
    public ToggleableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /** Creative players on dedicated servers will often desnyc with inventory open as server replaces with clientside copy **/
    public static ToggleableItem getForItemStack(ItemStack itemStack) {
        ToggleableItem toggleableItem = itemStack.getCapability(ToggleableItemHandler.TOGGLE_ITEM, null);

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
        this.setCachedNBT(null);
    }

    @Override
    public void toggleAbility(boolean enabled, ItemStack itemStack, EntityPlayer player) {
        this.toggleAbility(enabled);
    }

    @Override
    public void toggleMode(int value) {
        this.mode = value;
        this.setCachedNBT(null);
    }

    @Override
    public void toggleMode(int value, ItemStack itemStack, EntityPlayer player) {
        this.toggleMode(value);
    }

    @Override
    public void readNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound tagCompound = nbtTagCompound.getCompoundTag(TAG_NAME);

        if(tagCompound.hasKey(NBT_ENABLED))
            this.toggleAbility(tagCompound.getBoolean(NBT_ENABLED));
        if(tagCompound.hasKey(NBT_MODE))
            this.toggleMode(tagCompound.getInteger(NBT_MODE));
    }

    @Override
    public void writeNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setBoolean(NBT_ENABLED, this.isAbilityToggled());
        tagCompound.setInteger(NBT_MODE, this.getToggleMode());

        nbtTagCompound.setTag(TAG_NAME, tagCompound);
    }

    @Override
    @Nullable
    public NBTTagCompound getCachedNBT() {
        return cachedNBT;
    }

    @Override
    public void setCachedNBT(@Nullable NBTTagCompound nbt) {
        this.cachedNBT = nbt;
    }
}
