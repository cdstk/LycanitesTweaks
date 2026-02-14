package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class MinorFeaturesConfig {
    /*
     *
     * Features, no plans on porting Vanilla Mixins into Lycanites
     *
     */

    @Config.Comment("Makes all vanilla Entities (and all modded Entities that don't have a specified Creature Attribute) an Undead creature while the Smited effect is active. This will for example allow the Smite enchant to work on them.")
    @Config.Name("Most Smited Are Undead (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = false, earlyMixin = "mixins.lycanitestweaks.vanilla.smitedundeadlivingbase.json")
    public boolean smitedMakesMostUndead = false;

    /*
     *
     * Features that I feel are otherwise out of scope of LycanitesMobs, can change of course
     *
     */

    @Config.Comment("When a player logs out, the current Mob Event and remaining duration is saved to that player.\n" +
            "This will allow Mob Event mobs to keep spawning if the player logs out.\n" +
            "Logging back in will show a message with the remaining duration.\n" +
            "Only one event can be saved at a time.\n" +
            "Changing dimensions will clear the save as the property isn't saved.")
    @Config.Name("Player Logout Saves Mob Event")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.savedmobevents.json")
    public boolean logoutSavesMobEvents = true;

    @Config.Comment("Multiplier on the remaining duration that will be saved.")
    @Config.Name("Player Logout Saves Mob Event - Remaining Duration")
    public float savedMobEventDurationAmount = 0.1F;

    @Config.Comment("Naturally spawned Aegis can pickup and drop Iron Golems when specifically defending them.\n" +
            "Aegis will try to move Iron Golems out of water and very rarely do the same for Villagers.")
    @Config.Name("Aegis Pickup Iron Golems")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.aegispickupgolems.json")
    public boolean aegisPickupGolem = true;

    @Config.Comment("Adds parity with Iron Golem AI for naturally spawned Aegis\n" +
            "\tWill patrol the village at night.\n" +
            "\tWill be hostile to the same mobs as Iron Golems.\n" +
            "\tWill set a home position at the village.\n" +
            "\tWill penalize village reputation on death.")
    @Config.Name("Aegis Iron Golem AI")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.aegisirongolemai.json")
    public boolean aegisIronGolemAI = true;

    @Config.Comment("AI move speed modifier when patroling the village at night. Vanilla uses 0.6 for Iron Golems.")
    @Config.Name("Aegis Iron Golem AI - Village Patrol Speed")
    @Config.RequiresMcRestart
    public float aegisVillagePatrolSpeed = 0.2F;

    @Config.Comment("The Dungeon Boss Room exit will have a single layer of fence/wall blocks")
    @Config.Name("Dungeon Boss Room Exit Wall")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.dungeonbossexitwall.json")
    public boolean dungeonBossExitWall = true;

    @Config.Comment("Make the Melee AI minimum range match the damage range.\n" +
            "Allows mobs with both ranged and melee attacks to swap to melee attacks when their size is large.")
    @Config.Name("Minimum Melee Range For Mixed Attackers")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.hybridmeleerange.json")
    public boolean hybridMeleeRange = true;

    @Config.Comment("Adds additional Pet Commands.\n" +
            "Fight Boss - Whether the pet can target Bosses, Lycanite Bosses generally have a -75% damage reduction from non players.\n" +
            "Grief - Whether pets such as Beholder, Cacodemon, Troll, and Wraith can break blocks.")
    @Config.Name("Additional Pet Commands")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.petflags.json")
    public boolean additionalPetCommands = true;

    @Config.Comment("Bleed damage uses setDamageIsAbsolute ontop of Magic=Armor ignoring, making it ignore Resistance and other potion effects that reduce damage, as well as Protection enchantments.")
    @Config.Name("Bleed Pierces")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurebleedpierces.json")
    public boolean bleedPierces = true;

    @Config.Comment("Set to true to kill associated minions and projectiles when a Lycanites Mobs boss entity dies")
    @Config.Name("Boss Death Kills Minions and Projectiles")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurebossdeathminionprojectiles.json")
    public boolean bossDeathKillMinionProjectile = true;

    @Config.Comment("When calculating Boss damage limits, add a maximum damage check to LivingDamageEvent LOWEST.\n" +
            "This will fix one-shot exploits dropping mob loot early.\n" +
            "Mob abilities will still calculate bonuses based on attackEntityFrom.")
    @Config.Name("Boss DPS Limit Recalc")
    public boolean bossDPSLimitRecalc = true;

    @Config.Comment("Additionally calculates damageTakenThisSec based on LivingDamageEvent LOWEST instead of the earlier LivingAttackEvent.\n" +
            "This prevents most other Forge Event damage bonuses from apply after the DPS cap is calculated.")
    @Config.Name("Boss DPS Limit Recalc - Reapply Limit")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.bossdpsrecalc.json")
    public boolean bossDPSLimitRecalcReapplyLimit = true;

    @Config.Comment("Allows the visual tracking range for Boss Projectile and Portal Sprites to be modified.\n" +
            "Rahovart's Hellfire Barriers are affected the most as ones on the other side of the arena did not render at all.")
    @Config.Name("Boss Projectile Modify Tracking Range")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.features.oldprojectiletracking.json")
    public boolean bossProjectileTracking = true;

    @Config.Comment("Lycanites uses 40")
    @Config.Name("Boss Projectile Modify Tracking Range - Value")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 40)
    public int bossProjectileTrackingRange = 80;

    public static final String ADD_DIAMOND_PAXEL = "Diamond Paxel";
    @Config.Comment("Adds a craftable \"Diamond Paxel\", a tier above the \"Iron Paxel\"")
    @Config.Name(ADD_DIAMOND_PAXEL)
    @Config.RequiresMcRestart
    public boolean addDiamondPaxel = true;

    @Config.Comment("Minimum Level override, can be set above 1 to have crafted Diamond Paxels use LycanitesTweaks equipment enchanting")
    @Config.Name("Diamond Paxel - Level")
    @Config.RangeInt(min = 1)
    public int diamondPaxelLevel = 3;

    @Config.Comment("When reading familiars from URL, Set Spawning Active to false to not automatically spawn them on login")
    @Config.Name("Familiars Inactive On Join")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurefamiliarsinactiveonjoin.json")
    public boolean familiarsInactiveOnJoin = true;

    @Config.Comment("Enable customizable biome list for Arisaurs with the custom name Flowersaur. Flowersaurs have a custom texture that is unused in base LycanitesMobs")
    @Config.Name("Flowersaurs Naturally Spawn")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.flowersaurspawning.json")
    public boolean flowersaurNaturalSpawning = true;

    @Config.Comment("List of biomes (modid:biomename) where custom name Arisaurs will spawn in")
    @Config.Name("Flowersaurs Naturally Spawn - Biomes")
    public String[] flowersaurSpawningBiomes = {
            "minecraft:mutated_forest",
            "biomesoplenty:mystic_grove",
            "twilightforest:enchanted_forest"
    };

    @Config.Comment("Fix explosion damage being reduced to 1 when going through lycanites fire (as if it was a full block). Also use vanilla's fire punch-out handling instead of treating the fire as a full block.")
    @Config.Name("Lycanites Fire Vanilla Like")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, earlyMixin = "mixins.lycanitestweaks.vanillaextinguishmoddedfire.json", lateMixin = "mixins.lycanitestweaks.featurelycanitesfirepassable.json")
    public boolean lycanitesFiresNoBreakCollision = true;

    @Config.Comment("The Lycanites \"fixate target\" property is not used in vanilla, however LycanitesTweaks uses it to have mini bosses focus on players.\n" +
            "This modifies the behavior so mobs with this property can retaliate properly against other attackers.")
    @Config.Name("Lycanites Fixate Target Tweaks")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.fixaterevengetarget.json")
    public boolean lycanitesFixateTargetRevenge = true;

    @Config.Comment("Disable player tames from trying avoid an attacker when damaged to improve responsiveness and feel more controllable.")
    @Config.Name("Disable Tamed Avoid Target AI")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.tamedavoidtarget.json")
    public boolean disableTamedAvoidTarget = true;

    @Config.Comment("Lycanites grants a +2 Explosion Power to explosions caused by Rare variants, increasing damage by around 3x.\n" +
            "This will remove said bonus and no longer grant the large damage bonus as it is far above the intended Rare damage boost.")
    @Config.Name("Remove Projectile Explosion Radius Rare Bonus")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.features.projectileexplosionradius.json")
    public boolean removeBonusExplosionRadius = true;

    @Config.Comment("Adds more parity to Repulsion and Weight, repulsion gains weights benefits. This will make Roas, Spectres and Threshers unable to pull an entity with repulsion, as well as disallowing picking up an entity with Repulsion (Behemophet/Fear)")
    @Config.Name("Repulsion Weight Benefits")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurerepulsionweight.json")
    public boolean repulsionWeight = true;

    @Config.Comment("Whether a Lycanite Mob should be considered undead when the Smited effect is active. This will for example allow the Smite enchant to work on them.")
    @Config.Name("Lycanites Smited Are Undead")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = false, lateMixin = "mixins.lycanitestweaks.featuresmitedundeadbasecreature.json")
    public boolean smitedMakesBaseCreatureUndead = false;

    @Config.Comment("Adds the \"watermonster\" spawner type for JSON configs.\n" +
            "Functions exactly as the \"monster\" spawner type, however it also assigns spawning in Water Blocks.\n" +
            "The vanilla spawner is much faster than the lycanites spawner, however it will interact with other mods such as \"Bloodmoon\" increased spawn rates.\n" +
            "Warning: Overrides the vanilla ground spawning required for the \"monster\" spawner type when both are specified.\n" +
            "This will not automatically update JSONs.")
    @Config.Name("Water Monster Spawning")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.watermonsterspawning.json")
    public boolean waterMonsterSpawning = true;

    @Config.Comment("The Vanilla Lycanites \"Use Surface Light Level\" spawn checks will go up to the Sea Surface instead of only up to 24 blocks up")
    @Config.Name("Water Monster Spawning - Check Sea Level Light")
    public boolean waterMonsterCheckSeaTop = true;

    @Config.Comment("1 in n chance to spawn. 40 is a Vanilla Ocean Drowned.")
    @Config.Name("Water Monster Spawning - Ocean Spawn Rate")
    @Config.RangeInt(min = 1)
    public int waterMonsterSpawnRateOcean = 40;

    @Config.Comment("1 in n chance to spawn. 15 is a Vanilla River Drowned.")
    @Config.Name("Water Monster Spawning - Other Spawn Rate")
    @Config.RangeInt(min = 1)
    public int waterMonsterSpawnRateOther = 15;

    @Config.Comment("Specifying the \"water\" spawner in Creature JSON configs will automatically assign the vanilla water spawning behavior.\n" +
            "This will not automatically disable the original \"water\" JSON spawner.\n" +
            "Warning: This will have issues with any mob with the \"monster\" spawner type also specified.\n" +
            "This is an automatic alternative compared to manually changing configs.")
    @Config.Name("Water Monster Spawning - Modify Loading")
    @Config.RequiresMcRestart
    public boolean waterMonsterSpawningAuto = false;

    public static final String REPLACE_WATER_MONSTER = "Water Monster Spawning - JSON Replacements";
    @Config.Comment("Load default JSON replacements which will swap most usages of the \"water\" spawner type with \"watermonster\".\n" +
            "The usage of the \"waterfloor\" type will not be changed.\n" +
            "If the \"monster\" spawner type is specified, then the mob will keep the original \"water\" spawner type.\n" +
            "This is an automatic alternative compared to manually changing configs.")
    @Config.Name(REPLACE_WATER_MONSTER)
    @Config.RequiresMcRestart
    public boolean waterMonsterSpawningConfigs = true;
}
