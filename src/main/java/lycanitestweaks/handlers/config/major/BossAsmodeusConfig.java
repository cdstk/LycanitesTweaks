package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class BossAsmodeusConfig {

    @Config.Comment("Main toggle to enable this feature and its configs")
    @Config.Name("0. Enable Asmodeus Modifications")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurebossasmodeustweaks.json")
    public boolean bossTweaksAsmodeus = true;

    @Config.Comment("Replace the 50hp/sec heal with a 2% Max HP/sec heal")
    @Config.Name("Heal Portion When No Nearby Players")
    @Config.RequiresMcRestart
    public boolean healPortionNoPlayers = true;

    @Config.Comment("Player detection range where if there are no players nearby, start healing. (Lycanites uses 64)\n" +
            "96 will cover the entire arena if Asmodeus is in the center.\n" +
            "Setting to -1 will sync with the FindNearbyPlayersGoal, almost always 64 range.\n" +
            "Default Lycanites uses 64, but not synced." +
            "Previously 48, which is within Asmodeus' aggro range.")
    @Config.Name("Heal Portion - Range")
    @Config.RequiresMcRestart
    public int healPortionNoPlayersRange = -1;

    @Config.Comment("If minions have no target and are at least this distance away, teleport to host.\n" +
            "40 allows minions to check behind pillars and still follow Asmodeus when at another node.")
    @Config.Name("Minion Teleport Range")
    @Config.RequiresMcRestart
    public int minionTeleportRange = 40;

    @Config.Comment("Whether Asmodeus targets players behind walls. This fixes the cheese strat of hiding behind a pillar.")
    @Config.Name("Player Xray Target")
    public boolean playerXrayTarget = true;

    @Config.Comment("Fixes double damage and stops hitscan damage from ignoring walls with xRay on")
    @Config.Name("Disable Ranged Hitscan")
    public boolean disableRangedHitscan = true;

    @Config.Comment("Add an AI Ranged auto attack (targeted/all) to use alongside chaingun. Attack is like Rahovart's")
    @Config.Name("Additional Auto Attacks")
    @Config.RequiresMcRestart
    public boolean additionalProjectileAdd = true;

    @Config.Comment("Projectile to use for the single target auto attack. \n" +
            "LycanitesTweaks uses a projectile that noclips through terrain and \n" +
            "is meant to counter hiding being a pillar, requires Xray targeting to remember targets. (JSON configurable!)")
    @Config.Name("Single Target Auto Attack Projectile Name")
    @Config.RequiresMcRestart
    public String additionalProjectileTarget = "demonicchaosorb";

    @Config.Comment("Projectile to use for the all players auto attack. \n" +
            "LycanitesTweaks uses a projectile that noclips through terrain and \n" +
            "is meant to counter hiding being a pillar, requires Xray targeting to remember targets. (JSON configurable!)")
    @Config.Name("All Players Auto Attack Projectile Name")
    @Config.RequiresMcRestart
    public String additionalProjectileAll = "demonicchaosorb";

    @Config.Comment("Base Damage of the Devil Gatling projectile (Lycanites uses 4)")
    @Config.Name("Devil Gatling Base Damage")
    public int devilGatlingBaseDamage = 4;

    @Config.Comment("Damage multiplier the attack does to non-players. Default Lycanites is 10, or 10x damage.")
    @Config.Name("Devil Gatling Pet Damage Modifier")
    public float devilGatlingPetDamage = 10F;

    @Config.Comment("Whether on hit purge removes more than Lycanites defined list")
    @Config.Name("Devil Gatling Purge Any Buff")
    public boolean devilGatlingPurgeAnyBuff = true;

    @Config.Comment("Duration of Voided debuff in seconds, set to 0 to disable")
    @Config.Name("Devil Gatling Voided Time")
    public int devilGatlingVoidedTime = 1;

    @Config.Comment("Projectile to replace Devilstar Stream attack. \n" +
            "LycanitesTweaks replaces it with a projectile to shoots 'devilstar' projectiles while flying and \n" +
            "shoots 'demonicspark' projectiles when it hits terrain. (JSON configurable!)")
    @Config.Name("Devilstar Stream Projectile Name")
    @Config.RequiresMcRestart
    public String devilstarProjectile = "demonicshockspark";

    @Config.Comment("Devilstar Stream projectile firing ticks (Lycanites uses 100)")
    @Config.Name("Devilstar Stream Tick Length")
    @Config.RangeInt(min = 0)
    public int devilstarStreamTickLength = 100;

    @Config.Comment("Devilstar Stream cooldown ticks (Lycanites uses 400, Gatling is 200)")
    @Config.Name("Devilstar Stream Cooldown")
    @Config.RangeInt(min = 0)
    public int devilstarCooldown = 360;

    @Config.Comment("Whether Devilstar Stream can be used outside Phase 1")
    @Config.Name("Devilstar Stream All Phases")
    public boolean devilstarStreamAllPhases = true;

    @Config.Comment("Whether Astaroth Minions are teleported away on spawn. Else they will follow and teleport with Asmodeus.")
    @Config.Name("Astaroths Teleport Adjacent Node")
    public boolean astarothsTeleportAdjacent = false;

    @Config.Comment("Whether Astaroth Minions use Rare/Boss Damage Limit")
    @Config.Name("Astaroths Boss Damage Limit")
    public boolean astarothsUseBossDamageLimit = false;

    @Config.Comment("Whether Astaroth Minions are flagged as SpawnedAsBoss, intended to interact with LycanitesTweaks SpawnedAsBoss Rare Stats feature.")
    @Config.Name("Astaroths Boss SpawnedAsBoss Tag")
    public boolean astarothsSpawnedAsBoss = true;

    @Config.Comment("Astaroth respawn time in seconds (Lycanites uses 30)")
    @Config.Name("Hellshield Astaroths Phase 2 Respawn Time")
    @Config.RangeInt(min = 0)
    public int astarothsRespawnTimePhase2 = 90;

    @Config.Comment("Astaroth summon cap per player (Lycanites uses 2)")
    @Config.Name("Hellshield Astaroths Phase 2 Summon Cap")
    @Config.RangeInt(min = 0)
    public int astarothsSummonCapPhase2 = 2;

    @Config.Comment("Transitioning to Phase 2 will spawn the maximum cap of Astaroths instead of one (Vanilla Lycanites does this)")
    @Config.Name("Hellshield Astaroths Phase 2 Transition Summon All")
    public boolean astarothsSummonAllPhase2 = true;

    @Config.Comment("Hellshield is active whenever an Astaroth is alive instead of only phase 2")
    @Config.Name("Hellshield All Phases")
    public boolean hellshieldAllPhases = true;

    @Config.Comment("Amount of Damage Reduction Hellshield provides. Lycanites uses 100%")
    @Config.Name("Hellshield Damage Reduction")
    @Config.RangeDouble(min = 0, max = 1)
    public float hellshieldDamageReduction = 0.5F;

    @Config.Comment("Astaroth respawn time in seconds (Lycanites uses 40)")
    @Config.Name("Rebuild Astaroths Phase 3 Respawn Time")
    @Config.RangeInt(min = 0)
    public int astarothsRespawnTimePhase3 = 90;

    @Config.Comment("Astaroth Summon Cap per player (Lycanites uses 4)")
    @Config.Name("Rebuild Astaroths Phase 3 Summon Cap")
    @Config.RangeInt(min = 0)
    public int astarothsSummonCapPhase3 = 4;

    @Config.Comment("Transitioning to Phase 3 will spawn the maximum cap of Astaroths instead of one (Vanilla Lycanites only summons one)")
    @Config.Name("Rebuild Astaroths Phase 3 Transition Summon All")
    public boolean astarothsSummonAllPhase3 = true;

    @Config.Comment("Rebuild Healing is active whenever an Astaroth is alive instead of only phase 3")
    @Config.Name("Rebuild Healing All Phases")
    public boolean repairAllPhases = false;

    @Config.Comment("Whether Astaroths heal a % of max hp instead of a flat amount. Lycanites uses a flat 2.0")
    @Config.Name("Rebuild Heal Portion")
    public boolean repairHealPortion = false;

    @Config.Comment("HP healed per phase 3 Astaroth\n" +
            "Flat amounts can be any number while portions should be between 0.0 and 1.0")
    @Config.Name("Rebuild Heal Amount")
    @Config.RangeDouble(min = 0)
    public float repairHeal = 10;

    @Config.Comment("Should Phase 3 Summon a Phosphorescent Chupacabra")
    @Config.Name("Spawns Phosphorescent Chupacabra")
    @Config.RequiresMcRestart
    public boolean chupacabraSummon = true;
}
