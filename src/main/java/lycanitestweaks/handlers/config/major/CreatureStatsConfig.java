package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraftforge.common.config.Config;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CreatureStatsConfig {

    private static Map<String, Integer> effectsApplyScaleLevelLimited = null;
    private static Map<String, Integer> elementsApplyScaleLevelLimitedDebuffs = null;

    @Config.Comment("Rahovart/Asmodeus mechanic based minions match the boss' levels")
    @Config.Name("0. Minion Level Matches Host - Boss Mechanics")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebasecreatureminionhostlevelmatch.json")
    public boolean levelMatchMinionsHostMethod = true;

    @Config.Comment("Summon minion goal matches levels (AI Goal/Most Mobs). Amalgalich minions use this.")
    @Config.Name("0. Minion Level Matches Host - Entity Summon Goal")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureaiminionhostlevelmatch.json")
    public boolean levelMatchMinionsGoal = true;

    @Config.Comment("Grant all lycanites tagged as SpawnedAsBoss the Rare variant stat multipliers instead of the Common/Uncommon.\n" +
            "This will automatically attempt to rebalance Dungeon Bosses that try to load default configs.\n" +
            "Default Lycanites distributes Bosses between level 10-250. This will result in 10 levels per config dungeonLevel, between 20-50")
    @Config.Name("0. Spawned As Boss Tagged Uses Rare Stats")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurespawnedasbossrareboost.json")
    public boolean spawnedAsBossRareBoost = true;

    @Config.Comment("Dependency for usings caps on bonus stats per level. Does not affect variant/NBT bonuses.")
    @Config.Name("1. Cap Specific Stats")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurecreaturestatbonuscap.json")
    public boolean capSpecificStats = true;

    @Config.Comment("Ratio of max bonus defense, set to 0 to disable the cap")
    @Config.Name("1.a Defense Ratio")
    @Config.RangeDouble(min = 0)
    public double capDefenseRatio = 4.0D;

    @Config.Comment("Ratio of max bonus effect duration, set to 0 to disable the cap")
    @Config.Name("1.a Effect Duration Ratio")
    @Config.RangeDouble(min = 0)
    public double capEffectDurationRatio = 5.0D;

    @Config.Comment("Ratio of max bonus movement speed, set to 0 to disable the cap")
    @Config.Name("1.a Movement Speed Ratio")
    @Config.RangeDouble(min = 0)
    public double capSpeedRatio = 3.0D;

    @Config.Comment("Ratio of max bonus pierce, set to 0 to disable the cap")
    @Config.Name("1.a Pierce Ratio")
    @Config.RangeDouble(min = 0)
    public double capPierceRatio = 3.0D;

    @Config.Comment("List of elements whose Debuffs will have capped level scaling.\n" +
            "Format:[elementName, maxScaleLevel]\n" +
            "\telementName - Name of the element to limit, must be all lowercase\n" +
            "\tmaxScaleLevel - Final Level before duration and amplifier stop increasing")
    @Config.Name("1.b Elements' Debuffs Level Limit")
    public String[] elementsLevelLimitedDebuffs = {
            "arcane, 15",
            "chaos, 15",
            "lightning, 15",
            "phase, 15"
    };

    /*
     * 	EffectAuraGoal - Amalgalich Auto Decay, Archvile Demon Buffs
     * 	barghest - Leap weight
     * 	cockatrice - Mount Ability paralysis, aphagia
     *  eechetik - Auto plauge
     * 	quetzodracl - Pickup Drop weight
     * 	raiko - Pickup Drop weight
     * 	shade - Auto fear
     * 	strider - Pickup water breathing, penetration
     * 	warg - Auto paralysis
     */
    @Config.Comment("List of various Lycanites that apply effects and toggle-able level scaling cap.\n" +
            "Format:[thing, maxScaleLevel, enable]\n" +
            "\tthing - Do not change from defaults\n" +
            "\tmaxScaleLevel - Final Level before duration and amplifier stop increasing\n" +
            "\tenable - 'true' Will use the level limit")
    @Config.Name("1.b Misc Effects Level Limit")
    public String[] effectsLevelLimited = {
            "barghest, 15, false",
            "cockatrice, 15, true",
            "eechetik, 15, false",
            "quetzodracl, 15, false",
            "raiko, 15, false",
            "shade, 15, true",
            "strider, 15, false",
            "warg, 15, true",
            "EffectAuraGoal, 15, true"
    };

    @Config.Comment("Dependency for toggles. Only affects per level bonus, does not modify variant or nbt bonuses.")
    @Config.Name("2. Swap Health & Damage Per Level Bonus")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureswapdamagehealthscale.json")
    public boolean swapHealthDamageLevelBonus = true;

    @Config.Comment("Any Hostile Minions, such as those of the main bosses and rare variants")
    @Config.Name("2.a Hostile Minions")
    public boolean swapHealthDamageHostileMinion = true;

    @Config.Comment("Rahovart, Asmodeus, and Amalgalich")
    @Config.Name("2.a Main Bosses")
    public boolean swapHealthDamageMainBoss = true;

    @Config.Comment("Anything tagged with SpawnedAsBoss such as Dungeon Bosses")
    @Config.Name("2.a Tagged Boss")
    public boolean swapHealthDamageSpawnedAsBoss = true;

    @Config.Comment("Any Tamed Mobs. Intended for all players' mobs to feel more impactful but still trade 1 for 1.")
    @Config.Name("2.a Tamed Mobs")
    public boolean swapHealthDamageTamed = true;

    @Config.Comment("Dependency for modifying. Only affects per level bonus, does not modify variant or nbt bonuses.")
    @Config.Name("3. Modify Total Boss Health Per Level Bonus")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebossbonushealthmodifier.json")
    public boolean bossLowerHealthScale = true;

    /*
        There are three categories that may overlap but all use the BOSS_DAMAGE_LIMIT mechanic
        1. Always Bosses (Boss Group)
        2. Spawned As Bosses (NBT Boss, Never by vanilla Lycanites)
        3. Rare Variants (Natural Boss)

        Lower Health Bonus Options exists so BOSS_DAMAGE_LIMIT does not create mobs with insane time to kill

        A natural rare variant can have different stat modifiers from a SpawnedAsBoss version,
        LycanitesTweaks turns all altar bosses in SpawnedAsBoss mobs for example
     */

    @Config.Comment("Rahovart, Asmodeus, and Amalgalich. Intended to balance the Boss Damage Limit.")
    @Config.Name("3.a Main Boss Total Ratio")
    @Config.RangeDouble(min = 0)
    public double bossHealthBonusRatio = 0.25D;

    @Config.Comment("Anything tagged with SpawnedAsBoss such as Dungeon Bosses. Intended to balance when the Boss Damage Limit is applied.")
    @Config.Name("3.a Tagged Boss Excluding Rare Total Ratio")
    @Config.RangeDouble(min = 0)
    public double spawnedAsBossHealthBonusRatio = 0.5D;

    @Config.Comment("Intended when Dungeon Bosses only have the Boss Damage Limit when Rare.")
    @Config.Name("3.a Tagged Boss Exclusively Rare")
    @Config.RangeDouble(min = 0)
    public double spawnedAsBossRareHealthBonusRatio = 0.5D;

    @Config.Comment("Dependency for toggles. Vanilla Lycanites erroneously allowed Soulbounds via oversight.")
    @Config.Name("4. Variant/NBT Stat Bonus Receivers")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurealltamedvariantstats.json")
    public boolean variantStatReceivers = true;

    @Config.Comment("Any mob summoned via staff or pedestal")
    @Config.Name("4.a Player Summoned")
    public boolean variantStatsSummoned = true;

    @Config.Comment("Any mob given a Soulstone or from one")
    @Config.Name("4.a Soulbounded")
    public boolean variantStatsSoulbound = true;

    @Config.Comment("Any mob tamed with treats")
    @Config.Name("4.a Tamed")
    public boolean variantStatsTamed = true;

    public static Map<String, Integer> getLevelLimitedEffects() {
        if (CreatureStatsConfig.effectsApplyScaleLevelLimited == null)
            CreatureStatsConfig.effectsApplyScaleLevelLimited = Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.effectsLevelLimited)
                    .map(s -> s.split(","))
                    .filter(split -> split[2].equals("true"))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(), //Key
                            split -> {                //Value
                                try {
                                    return Integer.valueOf(split[1].trim());
                                } catch (Exception e) {
                                    LycanitesTweaks.LOGGER.error("Failed to parse {} in effectsLevelLimited", split[1].trim());
                                }
                                return 0;
                            }
                    ));
        return CreatureStatsConfig.effectsApplyScaleLevelLimited;
    }

    public static Map<String, Integer> getLevelLimitedElementDebuffs(){
        if(CreatureStatsConfig.elementsApplyScaleLevelLimitedDebuffs == null)
            CreatureStatsConfig.elementsApplyScaleLevelLimitedDebuffs = Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.elementsLevelLimitedDebuffs)
                    .map(s -> s.split(","))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(), //Key
                            split -> {                //Value
                                try {
                                    return Integer.valueOf(split[1].trim());
                                } catch (Exception e) {
                                    LycanitesTweaks.LOGGER.error("Failed to parse {} in elementsLevelLimitedDebuffs", split[1].trim());
                                }
                                return 0;
                            }
                    ));
        return CreatureStatsConfig.elementsApplyScaleLevelLimitedDebuffs;
    }

    public static void reset(){
        CreatureStatsConfig.effectsApplyScaleLevelLimited = null;
        CreatureStatsConfig.elementsApplyScaleLevelLimitedDebuffs = null;
    }
}
