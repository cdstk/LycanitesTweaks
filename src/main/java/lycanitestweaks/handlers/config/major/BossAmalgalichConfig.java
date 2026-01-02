package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class BossAmalgalichConfig {

    @Config.Comment("Main toggle to enable this feature and its configs")
    @Config.Name("0. Enable Amalgalich Modifications")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurebossamalgalich.json")
    public boolean bossTweaksAmalgalich = true;

    @Config.Comment("Replace the 50hp/sec heal with a 2% Max HP/sec heal")
    @Config.Name("Heal Portion When No Nearby Players")
    @Config.RequiresMcRestart
    public boolean healPortionNoPlayers = true;

    @Config.Comment("Player detection range where if there are no players nearby, start healing. (Lycanites uses 64)\n" +
            "Setting to -1 will sync with the FindNearbyPlayersGoal, almost always 64 range.\n" +
            "Default Lycanites uses 64, but not synced." +
            "Previously 48, which is inside the arena.")
    @Config.Name("Heal Portion - Range")
    @Config.RequiresMcRestart
    public int healPortionNoPlayersRange = -1;

    @Config.Comment("If minions have no target and are at least this distance away, teleport to host.")
    @Config.Name("Minion Teleport Range")
    @Config.RequiresMcRestart
    public int minionTeleportRange = 40;

    @Config.Comment("Whether Amalgalich attacks players hiding in arena walls")
    @Config.Name("Player Xray Target")
    public boolean playerXrayTarget = true;

    @Config.Comment("Projectile to replace targeted 'spectralbolt' auto attack. \n" +
            "LycanitesTweaks replaces it with a projectile that noclips through terrain, \n" +
            "places temporary Shadow Fire, \n" +
            "and has a 10% chance to remove one buff. (JSON configurable!)")
    @Config.Name("Main Targeted Auto Attack Projectile Name")
    @Config.RequiresMcRestart
    public String mainProjectileTarget = "lichspectralbolt";

    @Config.Comment("Projectile to replace all players 'spectralbolt' auto attack. \n" +
            "LycanitesTweaks replaces it with a projectile that noclips through terrain, \n" +
            "places temporary Shadow Fire, \n" +
            "and has a 10% chance to remove one buff. (JSON configurable!)")
    @Config.Name("Main All Players Auto Attack Projectile Name")
    @Config.RequiresMcRestart
    public String mainProjectileAll = "lichspectralbolt";

    @Config.Comment("Add a manual single target attack to use alongside auto attacks. Attack is like Asmodeus' except it creates less projectiles.")
    @Config.Name("Additional Single Target Manual Attack")
    @Config.RequiresMcRestart
    public boolean targetedAttack = true;

    @Config.Comment("Projectile to use for the single target manual attack. \n" +
            "LycanitesTweaks uses a projectile that noclips through terrain, \n" +
            "places temporary Shadow Fire, \n" +
            "and has a 50% chance to remove one buff. (JSON configurable!)")
    @Config.Name("Single Target Manual Attack Projectile Name")
    public String targetedProjectile = "lichshadowfire";

    @Config.Comment("Single target manual attack projectile cooldown ticks")
    @Config.Name("Single Target Attack Cooldown Ticks")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int targetedProjectileGoalCooldown = 360;

    @Config.Comment("Single target manual attack projectile stamina drain (Uptime = Cooldown/DrainRate)")
    @Config.Name("Single Target Attack Stamina Drain Rate")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int targetedProjectileStaminaDrainRate = 6;

    @Config.Comment("Consumption in all phases, goal/animation shared across the fight")
    @Config.Name("Consumption In All Phases")
    @Config.RequiresMcRestart
    public boolean consumptionAllPhases = true;

    @Config.Comment("Consumption cooldown ticks (Lycanites uses 400)")
    @Config.Name("Consumption Cooldown Ticks")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int consumptionGoalCooldown = 400;

    @Config.Comment("Whether Consumption should use \"lycanitestweaks:consumed\" debuff")
    @Config.Name("Consumption Debuff Effect")
    @Config.RequiresMcRestart
    public boolean consumptionEffect = true;

    @Config.Comment("Whether Consumption should deal damage based on victim's max hp")
    @Config.Name("Consumption Damage Max HP")
    public boolean consumptionDamageMaxHP = true;

    @Config.Comment("Makes Consumption more immersive by relying on Consumption debuff to reduce max hp.")
    @Config.Name("Consumption Damages Players")
    public boolean consumptionDamagesPlayers = false;

    @Config.Comment("Whether minion kill should heal a % of max hp instead of a flat amount. Lycanites uses a flat 25.0")
    @Config.Name("Consumption Kill Heal Portion")
    public boolean consumptionKillHealPortion = false;

    @Config.Comment("HP healed on minion kill\n" +
            "Flat amounts can be any number while portions should be between 0.0 and 1.0")
    @Config.Name("Consumption Kill Heal Amount")
    public float consumptionKillHeal = 25F;

    @Config.Comment("Summons one Crimson variant instead of 3 normal")
    @Config.Name("Spawns Crimson Epion")
    @Config.RequiresMcRestart
    public boolean crimsonEpion = true;

    @Config.Comment("Chance that Amalgalich killing an Epion will extinguish Shadow Fire. Player kill is always 100%")
    @Config.Name("Consumption Kill Epion Extinguish Chance")
    @Config.RangeDouble(min = 0, max = 1)
    public float consumptionKillEpionChance = 0.25F;

    @Config.Comment("Custom value for Shadow Fire extinguish width on death (Lycanites uses 10)")
    @Config.Name("Epion Extinguish Width")
    public int customEpionExtinguishWidth = 16;

    @Config.Comment("Projectile to replace 'lobdarklings' phase 3 auto attack.\n" +
            "LycanitesTweaks replaces it with a projectile that summons level and variant matching Darklings,\n" +
            "applies voided + 100% chance to remove one buff on contact,\n" +
            "and counts as a minion for Consumption kill healing. (JSON configurable!)")
    @Config.Name("Lob Darklings Replacement Projectile Name")
    @Config.RequiresMcRestart
    public String lobDarklingsReplacement = "lichlobdarklings";

    @Config.Comment("Replace Lob Darklings with an attack similar to Asmodeus.\n" +
            "This style of attack allows fire rate/minion count to be limited instead of level scaled.\n" +
            "Throwing arc can also be configured instead of being a full 360 degrees.")
    @Config.Name("Lob Darklings Modified Attack")
    @Config.RequiresMcRestart
    public boolean lobDarklingsModify = true;

    @Config.Comment("Lob Darklings throwing tick duration or instant count (Original shoots 3 instantly)")
    @Config.Name("Lob Darklings Modified Tick Length")
    @Config.RangeInt(min = 0)
    public int lobDarklingsTickLength = 3;

    @Config.Comment("One projectile is thrown per this tick rate (Original shoots 3 instantly)\n" +
            "0 can be set to instantly throw as many projectiles as set Tick Length.")
    @Config.Name("Lob Darklings Modified Throw Rate")
    @Config.RangeInt(min = 0)
    public int lobDarklingsTickRate = 0;

    @Config.Comment("Lob Darklings throwing arc in degrees with a target (Lycanites uses 360)\n" +
            "With no target, arc is always 360 degrees.")
    @Config.Name("Lob Darklings Modified Throw Arc")
    @Config.RangeInt(min = 0, max = 360)
    public int lobDarklingsArc = 45;

    @Config.Comment("Lob Darklings cooldown ticks (Lycanites uses 200)")
    @Config.Name("Lob Darklings Modified Cooldown")
    @Config.RangeInt(min = 0)
    public int lobDarklingsCooldown = 190;

    @Config.Comment("Should Phase 3 Summon a Lunar Grue")
    @Config.Name("Spawns Lunar Grue")
    @Config.RequiresMcRestart
    public boolean grueSummon = true;

}
