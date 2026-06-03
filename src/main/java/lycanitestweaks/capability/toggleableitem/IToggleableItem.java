package lycanitestweaks.capability.toggleableitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public interface IToggleableItem {

    boolean isAbilityToggled();
    int getToggleMode();
    void toggleAbility(boolean enabled);
    void toggleAbility(boolean enabled, ItemStack itemStack, EntityPlayer player);
    void toggleMode(int value);
    void toggleMode(int value, ItemStack itemStack, EntityPlayer player);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);

    @Nullable
    NBTTagCompound getCachedNBT();
    void setCachedNBT(@Nullable NBTTagCompound nbt);
}
