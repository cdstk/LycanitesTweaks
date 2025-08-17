package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.Pair;
import net.minecraftforge.common.config.Config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@MixinConfig(name = LycanitesTweaks.MODID)
public class PlayerMobLevelsConfig {

    private static List<BonusCategory> pmlBonusCategoryClientRenderOrder = null;

    private static Map<BonusCategory, Pair<BonusUsage, Double>> pmlBonusCategories = null;
    private static Set<BonusCategory> pmlBonusCategorySoulgazer = null;
    private static Map<Bonus, Double> pmlBonusUsagesAll = null;
    private static Map<Bonus, Double> pmlBonusUsagesTamed = null;
    private static Map<Bonus, Double> pmlBonusUsagesWild = null;
    private static Set<String> pmlSpawnerNames = null;

    @Config.Comment("Enable Capability to calculate a Mob Level associated to a player")
    @Config.Name("0. Player Mob Levels")
    @Config.RequiresMcRestart
    public boolean playerMobLevelCapability = false;

    @Config.Comment("The primary opt-out option, allows players to set final total 0-100% modifiers for a single or every creature.\n " +
            "This affects the server-side calculation and removes the client-side Beastiary buttons.")
    @Config.Name("0.a Set Creature Modifiers In Beastiary")
    public boolean setPMLModifiersBeastiary = true;

    @Config.Comment("Format: [categoryName, soulgazer, bonusGroup, multiplier]\n" +
            "\tcategoryName - The case player mob levels is added, do not change from the defaults\n" +
            "\tsoulgazer - If a mainhand/bauble Soulgazer is required to apply level boost\n" +
            "\tbonusGroup - [All,TAMED,WILD], specifies the list of multipliers to use when calculating bonus levels\n" +
            "\tmultiplier - Multiplier to use on the total bonus before it is used\n\n" +
            "Removing an entry fully disables associated features compared to zero'ing the multiplier\n" +
            "\tex. 'SpawnerTrigger' will still flag first-time spawns with 0.0 multiplier")
    @Config.Name("Bonus Categories")
    public String[] pmlCategories = {
            "AltarBossMain, true, WILD, 1.0",
            "AltarBossMini, true, WILD, 0.75",
            "DungeonBoss, true, WILD, 0.75",
            "EncounterEvent, false, WILD, 1.0",
            "SoulboundTame, false, TAMED, 1.0",
            "SpawnerNatural, false, ALL, 0.2",
            "SpawnerTile, false, WILD, 0.3",
            "SpawnerTrigger, false, WILD, 0.2",
            "SummonMinion, false, TAMED, 1.0",
            "SummonMinionInstant, false, TAMED, 0.5"
    };

    @Config.Comment("Specifies multipliers on specific bonus sources. Fall back list for when Tamed/Wild values are not found.\n" +
            "Format: [bonusName, multiplier]\n" +
            "\tbonusName - The Source of the bonus, do not change from the defaults\n" +
            "\tmultiplier - Multiplier to use on the total bonus before it is used")
    @Config.Name("Bonus Source Multipliers - ALL")
    public String[] pmlBonusAll = {
            "ActivePet, 0.75",
            "BestiaryCreature, 1.0",
            "BestiaryElement, 1.0",
            "Enchantments, 1.0",
            "PlayerDeath, -1.0"
    };

    @Config.Comment("Specifies multipliers on specific bonus sources. Used on Bonus Categories that are marked as 'TAMED'\n" +
            "Format: [bonusName,multiplier]\n" +
            "\tbonusName - The Source of the bonus, do not change from the defaults\n" +
            "\tmultiplier - Multiplier to use on the total bonus before it is used")
    @Config.Name("Bonus Source Multipliers - TAMED")
    public String[] pmlBonusTamed = {
            "ActivePet, 0.25",
            "BestiaryCreature, 2.0",
            "BestiaryElement, 2.0",
            "Enchantments, 0.5",
            "PlayerDeath, 0.0"
    };

    @Config.Comment("Specifies multipliers on specific bonus sources. Used on Bonus Categories that are marked as 'WILD'\n" +
            "Format: [bonusName,multiplier]\n" +
            "\tbonusName - The Source of the bonus, do not change from the defaults\n" +
            "\tmultiplier - Multiplier to use on the total bonus before it is used")
    @Config.Name("Bonus Source Multipliers - WILD")
    public String[] pmlBonusWild = {
            "ActivePet, 1.0",
            "BestiaryCreature, 1.0",
            "BestiaryElement, 0.0",
            "Enchantments, 1.0",
            "PlayerDeath, -1.0"
    };

    @Config.Comment("Used to lower bloated Minimum Enchantibility values via Rarity {COMMON, UNCOMMON, RARE, VERY_RARE}")
    @Config.Name("Enchantment Rarity Divisors")
    @Config.RequiresMcRestart
    public int[] enchRarityDivisors = {1, 2, 5, 10};

    @Config.Comment("Lycanites Pet Manager updates Player Mob Level Capability with pet entry information")
    @Config.Name("Pet Manager Tracks Pet Levels")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurepetmanagerpmltrackspetlevels.json")
    public boolean petManagerTracksHighestLevelPet = true;

    @Config.Comment("Whether highest level Pet Entry should try to be calculated, unsorted levels are still stored, false always returns 0.\n" +
            "This is different from the level of the currently active pets.")
    @Config.Name("Calculate Highest Level Pet Entry")
    public boolean pmlCalcHighestLevelPet = true;

    @Config.Comment("Inject handling for Player Mob Levels affecting the main Bosses")
    @Config.Name("Main Boss Bonus")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurebossesplayermoblevels.json")
    public boolean playerMobLevelMainBosses = true;

    @Config.Comment("Inject handling for Player Mob Level affecting JSON Spawners by whitelist")
    @Config.Name("JSON Spawner Bonus")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurejsonspawnerplayermoblevels.json")
    public boolean playerMobLevelJSONSpawner = true;

    @Config.Comment("Flags JSON entities to not be affected by the Natural Spawn Boost whether or not an entity was boosted.\n" +
            "This can make every event provide a smaller boost or make a few events stack a large boost")
    @Config.Name("JSON Spawner Bonus - Calls First Spawn")
    public boolean pmlSpawnerNameFirstSpawn = false;

    @Config.Comment("JSON Spawner Names is a blacklist instead of whitelist")
    @Config.Name("JSON Spawner Bonus - Blacklist")
    public boolean pmlSpawnerNameStringsIsBlacklist = false;

    @Config.Comment("List of Lycanites Spawner Names to attempt to apply Player Mob Levels")
    @Config.Name("JSON Spawner Bonus - Spawner Names")
    public String[] pmlSpawnerNameStrings = {
            "chaos",
            "disruption",
            "sleep"
    };

    @Config.Comment("Inject handling for soulbounds to have limited Player Mob Level bonuses in specified dimensions.\n" +
            "Prone to desyncs without 'Fix Client Pet Stat Desync'\n" +
            "Will fail without 'Fix Properties Set After Stat Calculation'")
    @Config.Name("Soulbounds Weakened In Specific Dimensions")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurelimitedbounddimensions.json")
    public boolean playerMobLevelSoulboundLimitedDimensions = true;

    @Config.Comment("Dimension IDs where soulbounds are level capped at Player Mob Level")
    @Config.Name("Soulbound Weakened Dimensions - IDs")
    public int[] pmlMinionLimitDimIds = {
            111,
            3
    };

    @Config.Comment("Soulbound Weakened Dimensions is Whitelist")
    @Config.Name("Soulbound Weakened Dimensions - Whitelist")
    public boolean pmlMinionLimitDimIdsWhitelist = true;

    @Config.Comment("Whether weakened soulbounds can spawn in dimensions blacklisted by vanilla Lycanites.\n" +
            "Lazy option that can be hot swapped in game.")
    @Config.Name("Soulbound Weakened Dimensions - Overrules Blacklist")
    public boolean pmlMinionLimitDimOverruleBlacklist = true;

    @Config.Comment("Whether weakened soulbound inventory GUIs are unable to be accessed")
    @Config.Name("Soulbound Weakened Dimensions - Disable Inventory")
    public boolean pmlMinionLimitDimNoInventory = true;

    @Config.Comment("Whether weakened soulbounds are unable to be mounted")
    @Config.Name("Soulbound Weakened Dimensions - Disable Mounting")
    public boolean pmlMinionLimitDimNoMount = true;

    @Config.Comment("Whether weakened dimensions prevent soulbound spirit recharge")
    @Config.Name("Soulbound Weakened Dimensions - Disable Spirit Recharge")
    public boolean pmlMinionLimitDimNoSpiritRecharge = true;

    @Config.Comment("Inject handling for Player Mob Level to affect summiniong minions through Equipment melee, Spriggan Heart, and Lob Darklings minions")
    @Config.Name("Player Mob Level Summon Instant Minion")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.summonfreeminionplayermoblevel.json")
    public boolean playerMobLevelSummonInstantMinion = true;

    @Config.Comment("Inject handling for Player Mob Level to affect summon staff minions")
    @Config.Name("Player Mob Level Summon Staff")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.summonstaffplayermoblevel.json")
    public boolean playerMobLevelSummonStaff = true;

    @Config.Comment("Remove treat pacifying and lower reputation gain when taming high leveled creatures")
    @Config.Name("Over Leveled Penalty")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuretameoverlevelpenalty.json")
    public boolean tamedOverLeveledPenalty = true;

    @Config.Comment("Creature level to compare to PML highest pet entry level, set to 0 to disable")
    @Config.Name("Over Leveled Penalty - Start")
    public int pmlTamedOverLevelStartLevel = 20;

    @Config.Comment("Whether treat reputation will be penalized if creature is over leveled.")
    @Config.Name("Over Leveled Penalty - Treat Point Penalty")
    public boolean pmlTamedOverLevelTreatPointPenalty = false;

    @Config.Comment("If creature could be tempted, will it remain able to be tempted if over leveled")
    @Config.Name("Over Leveled Penalty - Treat Tempt")
    public boolean pmlTamedOverLevelTreatTempt = false;

    public static List<PlayerMobLevelsConfig.BonusCategory> getPmlBonusCategoryClientRenderOrder(){
        if(PlayerMobLevelsConfig.pmlBonusCategoryClientRenderOrder == null){
            pmlBonusCategoryClientRenderOrder = Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.pmlBeastiaryOrder)
                    .map(PlayerMobLevelsConfig.BonusCategory::get)
                    .collect(Collectors.toList());
        }
        return PlayerMobLevelsConfig.pmlBonusCategoryClientRenderOrder;
    }

    public static Map<BonusCategory, Pair<BonusUsage, Double>> getPmlBonusCategories(){
        if(pmlBonusCategories == null)
            pmlBonusCategories = Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlCategories)
                    .map(s -> s.split(","))
                    .collect(Collectors.toMap(
                            split -> BonusCategory.get(split[0].trim()),
                            split -> {
                                try {
                                    BonusUsage usage = BonusUsage.valueOf(split[2].trim());
                                    return new Pair<>(usage, Double.valueOf(split[3].trim()));
                                } catch (Exception e){
                                    return new Pair<>(BonusUsage.ALL, Double.valueOf(split[3].trim()));
                                }
                            }
                    ));

        return pmlBonusCategories;
    }

    public static Set<BonusCategory> getPmlBonusCategorySoulgazer(){
        if(pmlBonusCategorySoulgazer == null)
            pmlBonusCategorySoulgazer = Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlCategories)
                    .map(s -> s.split(","))
                    .filter(split -> split[1].trim().equals("true"))
                    .map(split -> BonusCategory.get(split[0].trim()))
                    .collect(Collectors.toSet());

        return pmlBonusCategorySoulgazer;
    }

    public static Map<Bonus, Double> getPmlBonusUsagesAll(){
        if(pmlBonusUsagesAll == null)
            pmlBonusUsagesAll = getPmlBonusUsageMap(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlBonusAll, "Failed to parse {} in pmlBonusAll");

        return pmlBonusUsagesAll;
    }

    public static Map<Bonus, Double> getPmlBonusUsagesTamed(){
        if(pmlBonusUsagesTamed == null)
            pmlBonusUsagesTamed = getPmlBonusUsageMap(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlBonusTamed, "Failed to parse {} in pmlBonusTamed");

        return pmlBonusUsagesTamed;
    }

    public static Map<Bonus, Double> getPmlBonusUsagesWild(){
        if(pmlBonusUsagesWild == null)
            pmlBonusUsagesWild = getPmlBonusUsageMap(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlBonusWild, "Failed to parse {} in pmlBonusWild");

        return pmlBonusUsagesWild;
    }

    private static Map<Bonus, Double> getPmlBonusUsageMap(String[] config, String errormsg) {
        return Arrays
                .stream(config)
                .map(s -> s.split(","))
                .collect(Collectors.toMap(
                        split -> Bonus.valueOf(split[0].trim()), //Key
                        split -> {                               //Value
                            try {
                                return Double.valueOf(split[1].trim());
                            } catch (Exception e) {
                                LycanitesTweaks.LOGGER.error(errormsg, split[1].trim());
                            }
                            return 0.0;
                        }
                ));
    }

    public static Set<String> getPMLSpawnerNames(){
        if(pmlSpawnerNames == null)
            pmlSpawnerNames = Arrays.stream(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameStrings).collect(Collectors.toSet());

        return pmlSpawnerNames;
    }

    public static boolean isDimensionLimitedMinion(int dim){
        boolean found = Arrays
                .stream(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimIds)
                .anyMatch(id -> id == dim);
        return ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimIdsWhitelist == found;
    }

    public enum Bonus {

        ActivePet("ActivePet"),
        BestiaryCreature("BestiaryCreature"),
        BestiaryElement("BestiaryElement"),
        Enchantments("Enchantments"),
        PlayerDeath("PlayerDeath"),
        Dummy("");

        private final String name;

        Bonus(String name){
            this.name = name;
        }

        public static Bonus get(String name) {
            return Arrays.stream(Bonus.values())
                    .filter(bonus -> bonus.name.equals(name))
                    .findFirst().orElse(Dummy);
        }
    }

    public enum BonusCategory {

        AltarBossMain("AltarBossMain"),
        AltarBossMini("AltarBossMini"),
        DungeonBoss("DungeonBoss"),
        EncounterEvent("EncounterEvent"),
        SoulboundTame("SoulboundTame"),
        SpawnerNatural("SpawnerNatural"),
        SpawnerTile("SpawnerTile"),
        SpawnerTrigger("SpawnerTrigger"),
        SummonMinion("SummonMinion"),
        SummonMinionInstant("SummonMinionInstant"),
        Dummy("");

        private final String name;

        BonusCategory(String name){
            this.name = name;
        }

        public static BonusCategory get(String category) {
            return Arrays.stream(BonusCategory.values())
                    .filter(bonus -> bonus.name.equals(category))
                    .findFirst().orElse(Dummy);
        }
    }

    public enum BonusUsage {

        ALL,
        TAMED,
        WILD
    }

    public static void reset(){
        PlayerMobLevelsConfig.pmlBonusCategoryClientRenderOrder = null;

        pmlBonusCategories = null;
        pmlBonusCategorySoulgazer = null;
        pmlBonusUsagesAll = null;
        pmlBonusUsagesTamed = null;
        pmlBonusUsagesWild = null;
        pmlSpawnerNames = null;
    }
}
