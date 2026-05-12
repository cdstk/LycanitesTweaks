package lycanitestweaks.capability.lycanitestweaksplayer;

import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.info.beastiary.GenericBestiary;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public interface ILycanitesTweaksPlayerCapability {

    EntityPlayer getPlayer();
    void setPlayer(EntityPlayer player);

    GenericBestiary getBestiary();

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


    /**
     * Studies the provided entity for the provided amount of knowledge.
     * @param entity The entity to study, this is checked to see if it is a valid target.
     * @param experience The amount of knowledge experience to gain.
     * @param useCooldown If true, the creature study cooldown will be checked and reset on studying.
     * @param alwaysShowMessage If true, a rank up status message is always displayed.
     * @return True if new knowledge was gained for the entity.
     */
    boolean studyEntity(Entity entity, int experience, boolean useCooldown, boolean alwaysShowMessage);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);
}
