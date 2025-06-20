package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class EntityStoreCreatureConfig {

    @Config.Comment("Enable Capability to replicate LycanitesMobs PetEntry for non players\n" +
            "Intended to be used by Ender Crystal Reskins")
    @Config.Name("Add Capability: Entity Store Creature")
    @Config.RequiresMcRestart
    public boolean entityStoreCreatureCapability = true;

    @Config.Comment("Store Altar Mini Bosses in a Summon Crystal Entity")
    @Config.Name("Add Feature: Altar Mini Boss Spawn Summon Crystal")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurealtarminibosscrystal.json")
    public boolean altarMiniBossSpawnCrystal = true;

    @Config.Comment("Store Dungeon Bosses in a Summon Crystal Entity")
    @Config.Name("Add Feature: Dungeon Boss Spawn Summon Crystal")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuredungeonbosscrystal.json")
    public boolean dungeonBossSpawnCrystal = true;

    @Config.Comment("Randomly store some Mob Event spawns in an Encounter Crystal, will flag entity as SpawnedAsBoss")
    @Config.Name("Add Feature: Encounter Crystal Mob Event")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuremobeventencountercrystal.json")
    public boolean encounterCrystalMobEvent = true;

    @Config.Comment("1/n chance store a Mob Event spawned entity inside an Encounter Crystal")
    @Config.Name("Add Feature: Encounter Crystal Mob Event - Spawn Chance")
    @Config.RangeInt(min = 1)
    public int encounterCrystalSpawnChance = 50;

    @Config.Comment("Apply the Rare/Boss Damage Limit only if the stored creature is flagged as SpawnedAsBoss" +
            "\nThis grants partial consistency for non Rare Dungeon bosses who did not have this property in vanilla Lycanites" +
            "\nThis option does not grant the Rare stat boost (Creature stats config controls that option)")
    @Config.Name("Boss Crystal SpawnedAsBoss Damage Limit")
    public boolean bossCrystalSpawnedAsBossDamageLimit = true;

    @Config.Comment("Enable logic to automatically release stored Entity (Checks every second)")
    @Config.Name("Boss Crystal Tick Checks")
    public boolean bossCrystalTickChecks = true;

    @Config.Comment("1/n Chance to despawn per second, set to 0 to disable random despawning (Forced Despawning happens when further than 128 blocks)")
    @Config.Name("Encounter Crystal Despawn Chance")
    @Config.RangeInt(min = 0)
    public int encounterCrystalDespawnChance = 0;
}
