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

    @Config.Comment("Fix Iron Golems attacking tamed mobs")
    @Config.Name("Fix Golems Attacking Tamed Mobs (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, earlyMixin = "mixins.lycanitestweaks.vanillairongolemtargettamed.json")
    public boolean ironGolemsTamedTarget = true;

    @Config.Comment("Fix Withers attacking Tremors")
    @Config.Name("Fix Withers Attacking Tremors (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, earlyMixin = "mixins.lycanitestweaks.vanillawithertargettremor.json")
    public boolean witherTargetTremor = true;

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

    @Config.Comment("When reading familiars from URL, Set Spawning Active to false to not automatically spawn them on login")
    @Config.Name("Familiars Inactive On Join")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurefamiliarsinactiveonjoin.json")
    public boolean familiarsInactiveOnJoin = true;

    @Config.Comment("Enable customizable biome list for Arisaurs with the custom name Flowersaur. Flowersaurs have a custom texture that is unused in base LycanitesMobs")
    @Config.Name("Flowersaurs Naturally Spawn")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuresflowersaurspawning.json")
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

    @Config.Comment("Disable player and hostile minions trying avoid an attacker when damaged to improve responsiveness and feel more controllable.")
    @Config.Name("Remove Minion Avoid Target AI")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.minionavoidtarget.json")
    public boolean removeMinionAvoidTarget = true;

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
}
