package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ImperfectSummoningConfig {

    @Config.Comment("Main toggle to enabled this feature and its configs")
    @Config.Name("0. Imperfect Summoning")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.summonrework.json")
    public boolean imperfectSummoning = true;

    @Config.Comment("Knowledge Rank to summon normal minions, updates client visuals except for lang files")
    @Config.Name("Summoning Normal Rank")
    @Config.RangeInt(min = 0)
    public int normalSummonRank = 1;

    @Config.Comment("Knowledge Rank to summon variant minions")
    @Config.Name("Summoning Variant Rank")
    @Config.RangeInt(min = 0)
    public int variantSummonRank = 2;

    @Config.Comment("Nerfs minions who are summoned without variant summoning knowledge")
    @Config.Name("Imperfect Minion Nerfs")
    public boolean imperfectMinionNerfs = true;

    @Config.Comment("Chance for an imperfect minion to be hostile to the host. Default is 0.1 or 10% chance.")
    @Config.Name("Imperfect Hostile Summon Base Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public double imperfectHostileBaseChance = 0.1D;

    @Config.Comment("Chance Reduction per point of creature knowledge. Default is 0, or beneficial 0% per +100 points from a Soulgazer.")
    @Config.Name("Imperfect Hostile Summon Chance Modifier")
    @Config.RangeDouble(min = 0.0)
    public double imperfectHostileChanceModifier = 0.0D;

    @Config.Comment("Chance for imperfect minion to spawn with reduced hp or damage. Default is 1.0 or 100% chance.")
    @Config.Name("Imperfect Reduced Stat Summon Base Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public double imperfectStatsBaseChance = 1.0D;

    @Config.Comment("Chance Reduction per point of creature knowledge. Default is 0.001 or beneficial 10% per +100 points from a Soulgazer.")
    @Config.Name("Imperfect Reduced Stat Summon Chance Modifier")
    @Config.RangeDouble(min = 0.0)
    public double imperfectStatsChanceModifier = 0.001D;
}
