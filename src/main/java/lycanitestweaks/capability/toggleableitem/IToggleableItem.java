package lycanitestweaks.capability.toggleableitem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IToggleableItem {

    void sync(ItemStack itemStack, EntityPlayer player);

    boolean isAbilityToggled();
    int getToggleMode();
    void toggleAbility(boolean enabled);
    void toggleAbility(boolean enabled, ItemStack itemStack, EntityPlayer player);
    void toggleMode(int value);
    void toggleMode(int value, ItemStack itemStack, EntityPlayer player);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);

}
