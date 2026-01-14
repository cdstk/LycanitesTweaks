package lycanitestweaks.capability.lycanitestweaksplayer;

import com.lycanitesmobs.core.pets.PetEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public interface ILycanitesTweaksPlayerCapability {

    EntityPlayer getPlayer();
    void setPlayer(EntityPlayer player);

    void updateTick();
    void sync();
    void scheduleFullSync();

    void setKeyboundPet(PetEntry petEntry);
    void setKeyboundPetSpawning();
    void setKeyboundPetTeleport();
    UUID getKeyboundPetID();

    byte getSoulgazerAutoToggle();
    void setSoulgazerAutoToggle(byte id);
    void nextSoulgazerAutoToggle();

    boolean getSoulgazerManualToggle();
    void setSoulgazerManualToggle(boolean toggle);
    void nextSoulgazerManualToggle();

    void setSavedMobEvent(String eventName, int duration);
    boolean hasSavedMobEvent(String eventName);
    int getRemainingEventDuration(String eventName);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);
}
