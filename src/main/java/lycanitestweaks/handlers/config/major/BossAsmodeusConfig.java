package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class BossAsmodeusConfig {

    @Config.Comment("Main toggle to enabled this feature and its configs")
    @Config.Name("Add Feature")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebossasmodeustweaks.json")
    public boolean bossTweaksAsmodeus = true;

    @Config.Comment("Replace the 50hp/sec heal with a 2% Max HP/sec heal")
    @Config.Name("Heal Portion When No Players")
    @Config.RequiresMcRestart
    public boolean healPortionNoPlayers = true;

    @Config.Comment("Add AI Ranged auto attacking (targeted/all) for an additional projectile")
    @Config.Name("Additional Projectile Register")
    @Config.RequiresMcRestart
    public boolean additionalProjectileAdd = true;

    @Config.Comment("Projectile to use for auto attacking")
    @Config.Name("Additional Projectile Name")
    @Config.RequiresMcRestart
    public String additionalProjectile = "demonicchaosorb";

    @Config.Comment("Fixes double damage and stop hitscan damage ignoring walls with xRay on")
    @Config.Name("Disable Ranged Hitscan")
    public boolean disableRangedHitscan = true;

    @Config.Comment("Should on hit purge remove more than Lycanites defined list?")
    @Config.Name("Devil Gatling Purge Any Buff")
    public boolean devilGatlingPurgeAnyBuff = true;

    @Config.Comment("Duration of Voided Debuff in seconds, set to 0 to disable")
    @Config.Name("Devil Gatling Voided Time")
    public int devilGatlingVoidedTime = 1;

    @Config.Comment("Projectile to replace Devilstar Stream attack")
    @Config.Name("Devilstar Projectile Name")
    @Config.RequiresMcRestart
    public String devilstarProjectile = "demonicshockspark";

    @Config.Comment("Devilstar Stream projectile firing active ticks (Lycanites uses 100)")
    @Config.Name("Devilstar Stream UpTime")
    @Config.RangeInt(min = 0)
    public int devilstarStreamUpTime = 100;

    @Config.Comment("Devilstar Stream cooldown ticks (Lycanites uses 400, Gatling is 200)")
    @Config.Name("Devilstar Stream Cooldown")
    @Config.RangeInt(min = 0)
    public int devilstarCooldown = 360;

    @Config.Comment("Whether Devilstar Stream can be used outside Phase 1")
    @Config.Name("Devilstar Stream All Phases")
    public boolean devilstarStreamAllPhases = true;

    @Config.Comment("Whether Astaroth Minions are teleported away on spawn")
    @Config.Name("Astaroths Teleport Adjacent Node")
    public boolean astarothsTeleportAdjacent = false;

    @Config.Comment("Whether Astaroth Minions use Rare/Boss Damage Limit")
    @Config.Name("Astaroths Boss Damage Limit")
    public boolean astarothsUseBossDamageLimit = false;

    @Config.Comment("Whether Astaroth Minions are flagged as SpawnedAsBoss to interact with other tweaks that check for this property")
    @Config.Name("Astaroths Boss SpawnedAsBoss Tag")
    public boolean astarothsSpawnedAsBoss = true;

    @Config.Comment("Astaroth respawn time in seconds (2 per Player Max 2 Alive, Lycanites uses 30)")
    @Config.Name("Astaroths Phase 2 Respawn Time")
    @Config.RangeInt(min = 0)
    public int astarothsRespawnTimePhase2 = 30;

    @Config.Comment("Astaroth respawn time in seconds (1 per Player Max 4 Alive per Player, Lycanites uses 40)")
    @Config.Name("Astaroths Phase 3 Respawn Time")
    @Config.RangeInt(min = 0)
    public int astarothsRespawnTimePhase3 = 30;

    @Config.Comment("Hellshield is active whenever an Astaroth is alive instead of only phase 2")
    @Config.Name("Hellshield All Phases")
    public boolean hellshieldAllPhases = true;

    @Config.Comment("Amount of Damage Reduction Hellshield provides")
    @Config.Name("Hellshield Damage Reduction")
    @Config.RangeDouble(min = 0, max = 1)
    public float hellshieldDamageReduction = 0.5F;

    @Config.Comment("Repair is active whenever an Astaroth is alive instead of only phase 3")
    @Config.Name("Repair All Phases")
    public boolean repairAllPhases = false;

    @Config.Comment("Amount of Healing Each Repairing Astaroth provides, set to 0 to use original flat 2.0 Healing")
    @Config.Name("Repair Healing Portion")
    @Config.RangeDouble(min = 0, max = 1)
    public float repairHealingPortion = 0.005F;

    @Config.Comment("Should Asmodeus attempt attacks on players behind arena pillars")
    @Config.Name("Player Xray Target")
    public boolean playerXrayTarget = true;

    @Config.Comment("Should Phase 3 Summon a Phosphorescent Chupacabra")
    @Config.Name("Spawns Phosphorescent Chupacabra")
    @Config.RequiresMcRestart
    public boolean chupacabraSummon = true;
}
