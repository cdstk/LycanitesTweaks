package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class ImperfectSummoningConfig {

    @Config.Comment("Main toggle to enabled this feature and its configs")
    @Config.Name("Add Feature")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuresummonrework.json")
    public boolean summonProgressionRework = true;

    @Config.Comment("Nerfs minions who are summoned without variant summoning knowledge")
    @Config.Name("Imperfect Summoning")
    public boolean imperfectSummoning = true;

    @Config.Comment("Chance for an imperfect minion to be hostile to the host")
    @Config.Name("Imperfect Hostile Summon Base Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public double imperfectHostileBaseChance = 0.1D;

    @Config.Comment("Chance Reduction per point of creature knowledge")
    @Config.Name("Imperfect Hostile Summon Chance Modifier")
    @Config.RangeDouble(min = 0.0)
    public double imperfectHostileChanceModifier = 0.0D;

    @Config.Comment("Chance for imperfect minion to spawn with reduced defenses or offenses")
    @Config.Name("Imperfect Reduced Stat Summon Base Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public double imperfectStatsBaseChance = 1.0D;

    @Config.Comment("Chance Reduction per point of creature knowledge")
    @Config.Name("Imperfect Reduced Stat Summon Chance Modifier")
    @Config.RangeDouble(min = 0.0)
    public double imperfectStatsChanceModifier = 0.001D;

    @Config.Comment("Knowledge Rank to summon normal minions, updates client visuals except for lang files")
    @Config.Name("Summoning Normal Rank")
    @Config.RangeInt(min = 0)
    public int normalSummonRank = 1;

    @Config.Comment("Knowledge Rank to summon variant minions")
    @Config.Name("Summoning Variant Rank")
    @Config.RangeInt(min = 0)
    public int variantSummonRank = 2;
}
