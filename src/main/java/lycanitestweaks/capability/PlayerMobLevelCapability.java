package lycanitestweaks.capability;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureKnowledge;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketPlayerMobLevelsStats;
import lycanitestweaks.util.Helpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PlayerMobLevelCapability implements IPlayerMobLevelCapability {

    private EntityPlayer player;
    public static final int MAINHAND_CHECK_SIZE = 8;

    private int deathCooldown = 0;
    public int[] nonMainLevels = new int[5];
    public Queue<Integer> mainHandLevels = new LinkedList<>();
    public Map<Integer, Integer> petEntryLevels = new HashMap<>();
    private Object[] petEntryLevelsCopy = null;
    public int highestActivePetLevels = 0; // cache value, only used with mixin enabled, clearHighestLevelPetActive flags dirty
    private boolean highestActivePetDirty = true;

    PlayerMobLevelCapability(){}

    PlayerMobLevelCapability(@Nonnull EntityPlayer player){
        this.player = player;
        Arrays.fill(nonMainLevels, 0);
        mainHandLevels.add(0);
    }

    public static IPlayerMobLevelCapability getForPlayer(EntityPlayer player) {
        if (player == null || !ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability) {
            return null;
        }
        IPlayerMobLevelCapability pml = player.getCapability(PlayerMobLevelCapabilityHandler.PLAYER_MOB_LEVEL, null);
        if (pml != null && pml.getPlayer() != player) {
            pml.setPlayer(player);
        }
        return pml;
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void updateTick() {
        if(this.deathCooldown > 0) this.deathCooldown--;
    }

    @Override
    public void sync() {
        PacketPlayerMobLevelsStats packet = new PacketPlayerMobLevelsStats(this);
        if(!this.player.world.isRemote) {
            EntityPlayerMP playerMP = (EntityPlayerMP) this.player;
            PacketHandler.instance.sendTo(packet, playerMP);
        }
    }

    @Override
    public int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category) {
        return this.getTotalLevelsForCategory(category, null);
    }

    @Override
    public int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category, @Nullable BaseCreatureEntity creature) {
        return this.getTotalLevelsForCategory(category, creature, false);
    }

    @Override
    public int getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory category, @Nullable BaseCreatureEntity creature, boolean client) {
        double totalLevels = 0;
        double deathModifier = 0;

        if(PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(category)){
            for(PlayerMobLevelsConfig.Bonus bonus : PlayerMobLevelsConfig.Bonus.values()) {
                double modifier = 0.0D;
                switch (PlayerMobLevelsConfig.getPmlBonusCategories().get(category).getLeft()) {
                    case WILD:
                        if(PlayerMobLevelsConfig.getPmlBonusUsagesWild().containsKey(bonus))
                            modifier = PlayerMobLevelsConfig.getPmlBonusUsagesWild().get(bonus);
                        break;
                    case TAMED:
                        if(PlayerMobLevelsConfig.getPmlBonusUsagesTamed().containsKey(bonus))
                            modifier = PlayerMobLevelsConfig.getPmlBonusUsagesTamed().get(bonus);
                    case ALL:
                        if(bonus == PlayerMobLevelsConfig.Bonus.PlayerDeath && PlayerMobLevelsConfig.getPmlBonusUsagesAll().containsKey(bonus))
                            modifier = PlayerMobLevelsConfig.getPmlBonusUsagesAll().get(bonus);
                }
                if(modifier == 0.0D && bonus != PlayerMobLevelsConfig.Bonus.PlayerDeath && PlayerMobLevelsConfig.getPmlBonusUsagesAll().containsKey(bonus)){
                    modifier = PlayerMobLevelsConfig.getPmlBonusUsagesAll().get(bonus);
                }

                switch (bonus){
                    case ActivePet:
                        totalLevels += this.getHighestLevelPetActive() * modifier;
                        break;
                    case BestiaryCreature:
                        if(!client) totalLevels += this.getCurrentLevelBestiary(creature) * modifier;
                        break;
                    case BestiaryElement:
                        if(creature != null) totalLevels += this.getCurrentLevelBestiary(creature.getElements()) * modifier;
                        break;
                    case Enchantments:
                        totalLevels += this.getTotalEnchantmentLevels() * modifier;
                        break;
                    case PlayerDeath:
                        deathModifier = modifier;
                    default:
                }
            }
            totalLevels *= PlayerMobLevelsConfig.getPmlBonusCategories().get(category).getRight();
        }

        if(this.getDeathCooldown() > 0) {
            totalLevels *= (1D + deathModifier);
        }
        return (int)Math.round(totalLevels);
    }

    @Override
    public int getTotalEnchantmentLevels(){
        int total = 0;

        for(int lvl : nonMainLevels){
            total += lvl;
        }
        total += getHighestMainHandLevels();

        return total;
    }

    @Override
    public int getCurrentLevelBestiary(BaseCreatureEntity creature) {
        if(creature != null) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
            if (extendedPlayer != null && extendedPlayer.getBeastiary().hasKnowledgeRank(creature.creatureInfo.getName(), 0)) {
                CreatureKnowledge knowledge = extendedPlayer.getBeastiary().getCreatureKnowledge(creature.creatureInfo.getName());
                float experience;
                if(knowledge.rank >= 2){
                    experience = 1F;
                }
                else {
                    experience = knowledge.experience / 1000F;
                }
                return Math.round(experience * Helpers.getStartingLevel(creature));
            }
        }
        return 0;
    }

    /**
     * Gets the base level bonus for a list of elements, will only choose the element with the highest bonus
     * WARNING, this does not check if a given ElementInfo is in the Creature-Elements map
     *
     * @param elements List of elements to check
     * @return Base level bonus
     */
    @Override
    public int getCurrentLevelBestiary(List<ElementInfo> elements) {
        int total = 0;
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
        if (extendedPlayer != null) {
            for(ElementInfo elementInfo : elements){
                int elementTotal = 0;
                for(String creatureName : Helpers.getCreatureElementsMap().get(elementInfo.name)){
                    if(extendedPlayer.getBeastiary().hasKnowledgeRank(creatureName, 2)) elementTotal++;
                }
                total = Math.max(total, elementTotal);
            }
        }
        return total;
    }

    @Override
    public int getDeathCooldown() {
        return this.deathCooldown;
    }

    @Override
    public int getHighestLevelPetActive() {
        int total = 0;
        if(!this.highestActivePetDirty) return this.highestActivePetLevels;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
        if (extendedPlayer != null) {
            for(PetEntry petEntry : extendedPlayer.petManager.entries.values()){
                if(ForgeConfigHandler.client.debugLoggerTrigger) LycanitesTweaks.LOGGER.log(Level.INFO, "Total:{} PetEntry:{} Cache:{}", total, petEntry.getLevel(), this.highestActivePetLevels);
                if(petEntry.spawningActive){
                    total = Math.max(total, petEntry.getLevel());
                    if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.petManagerTracksHighestLevelPet) {
                        this.highestActivePetLevels = total;
                    }
                }
            }
        }
        this.highestActivePetDirty = false;
        return total;
    }

    @Override
    public int getHighestLevelPetSoulbound() {
        if(!ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlCalcHighestLevelPet || this.petEntryLevels.isEmpty()) return 0;
        if(this.petEntryLevelsCopy == null) {
            this.petEntryLevelsCopy = petEntryLevels.keySet().toArray(new Integer[0]);
            Arrays.sort(petEntryLevelsCopy, Comparator.comparingInt(a -> (int) a).reversed());
        }
        if(ForgeConfigHandler.client.debugLoggerAutomatic)
            LycanitesTweaks.LOGGER.log(Level.INFO, "Highest: {} Level Map: {}", this.petEntryLevelsCopy[0], this.petEntryLevels);
        return (int)this.petEntryLevelsCopy[0];
    }

    @Override
    public int getHighestMainHandLevels(){
        Object[] mainHandLevelsCopy = mainHandLevels.toArray();
        Arrays.sort(mainHandLevelsCopy, Comparator.comparingInt(a -> (int)a).reversed());
        return (int)mainHandLevelsCopy[0];
    }

    @Override
    public int getItemStackLevels(ItemStack itemStack){
        int levels = 0;
        NBTTagList enchantments = itemStack.getEnchantmentTagList();
        if(enchantments.isEmpty()) return 0;
        for(int i = 0; i < enchantments.tagCount(); i++){
            Enchantment ench = Enchantment.getEnchantmentByID(enchantments.getCompoundTagAt(i).getShort("id"));
            if(ench != null) {
                int enchLvl = enchantments.getCompoundTagAt(i).getShort("lvl");
                int enchantabilityLevels = (int)((ench.getMinEnchantability(ench.getMinLevel())) * ((float)enchLvl / ench.getMaxLevel()));

                switch (ench.getRarity()){
                    case COMMON:
                        enchantabilityLevels /= ForgeConfigHandler.majorFeaturesConfig.pmlConfig.enchRarityDivisors[0];
                        break;
                    case UNCOMMON:
                        enchantabilityLevels /= ForgeConfigHandler.majorFeaturesConfig.pmlConfig.enchRarityDivisors[1];
                        break;
                    case RARE:
                        enchantabilityLevels /= ForgeConfigHandler.majorFeaturesConfig.pmlConfig.enchRarityDivisors[2];
                        break;
                    case VERY_RARE:
                        enchantabilityLevels /= ForgeConfigHandler.majorFeaturesConfig.pmlConfig.enchRarityDivisors[3];
                        break;
                }

                levels += enchantabilityLevels;

                if(ForgeConfigHandler.client.debugLoggerTrigger)
                    LycanitesTweaks.LOGGER.log(Level.INFO, "ENCH: {}, LEVELS: {}", ench.getName(), enchantabilityLevels);
            }
        }
        return levels;
    }

    // Used by PetManager and when a new PetEntry instance is made with an existing mob
    @Override
    public void addNewPetLevels(int levels) {
        if(this.petEntryLevels.containsKey(levels)){
            this.petEntryLevels.put(levels, this.petEntryLevels.get(levels) + 1);
        }
        else{
            this.petEntryLevels.put(levels, 1);
            this.petEntryLevelsCopy = null;
        }
        this.sync();
    }

    // For adding from PetManager NBT
    @Override
    public void addPetEntryLevels(PetEntry entry) {
        this.addNewPetLevels(entry.getLevel());
    }

    @Override
    public void clearHighestLevelPetActive() {
        this.highestActivePetLevels = 0;
        this.highestActivePetDirty = true;
        this.sync();
    }

    // Removal from PetManager is accurate
    @Override
    public void removePetEntryLevels(PetEntry entry) {
        int level = entry.getLevel();
        if(this.petEntryLevels.containsKey(level)){
            if(this.petEntryLevels.get(level) > 1)
                this.petEntryLevels.put(level, this.petEntryLevels.get(level) - 1);
            else{
                this.petEntryLevels.remove(level);
                this.petEntryLevelsCopy = null;
            }
            this.sync();
        }
        else{
            if(ForgeConfigHandler.client.debugLoggerTrigger) LycanitesTweaks.LOGGER.log(Level.INFO, "Warning tried to remove when petEntryLevels did not have key: {}", level);
        }
    }

    @Override
    public void setDeathCooldown(int cooldownTicks) {
        this.deathCooldown = cooldownTicks;
    }

    @Override
    public void setNonMainLevels(ItemStack itemStack, int slotIndex) {
        this.nonMainLevels[slotIndex-1] = getItemStackLevels(itemStack);
        this.sync();
    }

    @Override
    public void setMainHandLevels(ItemStack itemStack){
        this.mainHandLevels.add(getItemStackLevels(itemStack));
        while(this.mainHandLevels.size() > PlayerMobLevelCapability.MAINHAND_CHECK_SIZE) this.mainHandLevels.poll();
        this.sync();
    }
}
