package lycanitestweaks.capability;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
}
