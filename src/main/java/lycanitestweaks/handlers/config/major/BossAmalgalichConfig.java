package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class BossAmalgalichConfig {

    @Config.Comment("Main toggle to enabled this feature and its configs")
    @Config.Name("Add Feature")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebossamalgalich.json")
    public boolean bossTweaksAmalgalich = true;

    @Config.Comment("Projectile to replace main 'spectralbolt' auto attack (targeted/all)")
    @Config.Name("Main Projectile Name")
    @Config.RequiresMcRestart
    public String mainProjectile = "lichspectralbolt";

    @Config.Comment("Replace the 50hp/sec heal with a 2% Max HP/sec heal")
    @Config.Name("Heal Portion When No Players")
    @Config.RequiresMcRestart
    public boolean healPortionNoPlayers = true;

    @Config.Comment("Add AI task for targeted attack to use alongside auto attacks")
    @Config.Name("Targeted Projectile Attack")
    @Config.RequiresMcRestart
    public boolean targetedAttack = true;

    @Config.Comment("Projectile to use for targeted attack")
    @Config.Name("Targeted Projectile Name")
    public String targetedProjectile = "lichshadowfire";

    @Config.Comment("Targeted Projectile Cooldown Ticks")
    @Config.Name("Targeted Projectile Cooldown Ticks")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int targetedProjectileGoalCooldown = 360;

    @Config.Comment("Targeted Projectile Stamina Drain (Uptime = Cooldown/DrainRate)")
    @Config.Name("Targeted Projectile Stamina Drain Rate")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int targetedProjectileStaminaDrainRate = 6;

    @Config.Comment("Consumption in any phases, goal/animation shared across the fight")
    @Config.Name("Consumption All Phases")
    @Config.RequiresMcRestart
    public boolean consumptionAllPhases = true;

    @Config.Comment("Consumption Cooldown Duration Ticks (Lycanites uses 400)")
    @Config.Name("Consumption Cooldown Duration Ticks")
    @Config.RangeInt(min = 0)
    @Config.RequiresMcRestart
    public int consumptionGoalCooldown = 400;

    @Config.Comment("Whether consumption should use LycanitesTweaks Consumption debuff")
    @Config.Name("Consumption Debuff effect")
    @Config.RequiresMcRestart
    public boolean consumptionEffect = true;

    @Config.Comment("Whether consumption should deal damage based on victim's max hp")
    @Config.Name("Consumption Damage Max HP")
    public boolean consumptionDamageMaxHP = true;

    @Config.Comment("Make Consumption more immersive by relying on Consumption debuff to reduce max hp.")
    @Config.Name("Consumption Damages Players")
    public boolean consumptionDamagesPlayers = false;

    @Config.Comment("Portion of HP healed on minion kill, set to 0 to use original 25.0 healing")
    @Config.Name("Consumption Kill Heal")
    public float consumptionKillHeal = 0.005F;

    @Config.Comment("Chance that Amalgalich killing an Epion will Extinguish Shadow Fire")
    @Config.Name("Consumption Kill Epion Extinguish")
    public float consumptionKillEpionChance = 0.8F;

    @Config.Comment("Custom value for shadow fire extinguish width on death (Lycanites uses 10)")
    @Config.Name("Custom Epion Extinguish Width")
    public int customEpionExtinguishWidth = 10;

    @Config.Comment("Replace Lob Darkling with Summon Goal, as a high level Amalgalich spams too much")
    @Config.Name("Lob Darklings Replace")
    @Config.RequiresMcRestart
    public boolean replaceLobDarkling = true;

    @Config.Comment("Should Amalgalich try attacking players hiding in arena walls")
    @Config.Name("Player Xray Target")
    public boolean playerXrayTarget = true;

    @Config.Comment("Epion Summons one Crimson variant instead of 3 normal")
    @Config.Name("Spawns Crimson Epion")
    @Config.RequiresMcRestart
    public boolean crimsonEpion = true;

    @Config.Comment("Should Phase 3 Summon a Lunar Grue")
    @Config.Name("Spawns Lunar Grue")
    @Config.RequiresMcRestart
    public boolean grueSummon = true;

}
