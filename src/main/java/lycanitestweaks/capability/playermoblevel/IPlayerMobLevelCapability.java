package lycanitestweaks.capability.playermoblevel;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public interface IPlayerMobLevelCapability {

    EntityPlayer getPlayer();
    void setPlayer(EntityPlayer player);

    void updateTick();
    void sync();

    int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category);
    int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category, @Nullable BaseCreatureEntity creature);
    int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category, @Nullable BaseCreatureEntity creature, boolean client);

    float getPMLModifierForCreature(BaseCreatureEntity creature);
    float getPMLModifierForAll();
    void setPMLModifierForCreature(BaseCreatureEntity creature, float modifier);
    void setPMLModifierForAll(float modifier);

    int getTotalEnchantmentLevels();

    int getCurrentLevelBestiary(BaseCreatureEntity creature);
    int getCurrentLevelBestiary(List<ElementInfo> elements);
    int getDeathCooldown();
    int getHighestLevelPetActive();
    int getHighestLevelPetSoulbound();
    int getHighestMainHandLevels();
    int getItemStackLevels(ItemStack itemStack);

    void addNewPetLevels(int levels);
    void addPetEntryLevels(PetEntry entry);
    void clearHighestLevelPetActive();
    void removePetEntryLevels(PetEntry entry);
    void setDeathCooldown(int cooldownTicks);
    void setNonMainLevels(ItemStack itemStack, int slotIndex);
    void setMainHandLevels(ItemStack itemStack);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);
}
