package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class MinorFeaturesConfig {
    /*
     *
     * Features, no plans on porting Vanilla Mixins into Lycanites
     *
     */

    @Config.Comment("Fix Iron Golems attacking tamed mobs")
    @Config.Name("Fix Golems Attacking Tamed Mobs (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.EarlyMixin(name = "mixins.lycanitestweaks.vanillairongolemtargettamed.json")
    public boolean ironGolemsTamedTarget = true;

    @Config.Comment("Fix Withers attacking Tremors")
    @Config.Name("Fix Withers Attacking Tremors (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.EarlyMixin(name = "mixins.lycanitestweaks.vanillawithertargettremor.json")
    public boolean witherTargetTremor = true;

    @Config.Comment("Makes all vanilla Entities (and all modded Entities that don't have a specified Creature Attribute) an Undead creature while the Smited effect is active. This will for example allow the Smite enchant to work on them.")
    @Config.Name("Most Smited Are Undead (Vanilla)")
    @Config.RequiresMcRestart
    @MixinConfig.EarlyMixin(name = "mixins.lycanitestweaks.vanillasmitedundeadlivingbase.json")
    public boolean smitedMakesMostUndead = true;

    /*
     *
     * Features that I feel are otherwise out of scope of LycanitesMobs, can change of course
     *
     */

    @Config.Comment("Bleed damage uses setDamageIsAbsolute ontop of Magic=Armor ignoring, making it ignore Resistance and other potion effects that reduce damage, as well as Protection enchantments.")
    @Config.Name("Bleed Pierces")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebleedpierces.json")
    public boolean bleedPierces = true;

    @Config.Comment("Set to true to kill associated minions and projectiles when a Lycanites Mobs boss entity dies")
    @Config.Name("Boss Death Kills Minions and Projectiles")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebossdeathminionprojectiles.json")
    public boolean bossDeathKillMinionProjectile = true;

    @Config.Comment("Move the Damage Limit DPS calc from attackEntityFrom to the slightly earlier LivingDamageEvent LOWEST, moving it to BEFORE death check. Required to properly limit the dealt dmg.")
    @Config.Name("Boss DPS Limit Recalc")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurebossdamagelimitdpsrecalc.json")
    public boolean bossDPSLimitRecalc = true;

    @Config.Comment("Additionally limits damage amount on LivingDamageEvent LOWEST, this will fix one-shots dropping mob loot early")
    @Config.Name("Boss DPS Limit Recalc Modify - Reduces Amount")
    public boolean bossDamageLimitReducesAmount = true;

    @Config.Comment("When reading familiars from URL, Set Spawning Active to false to not automatically spawn them on login")
    @Config.Name("Familiars Inactive On Join")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurefamiliarsinactiveonjoin.json")
    public boolean familiarsInactiveOnJoin = true;

    @Config.Comment("Enable customizable biome list for Arisaurs with the custom name Flowersaur. Flowersaurs have a custom texture that is unused in base LycanitesMobs")
    @Config.Name("Flowersaurs Naturally Spawn")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuresflowersaurspawning.json")
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
    @MixinConfig.EarlyMixin(name = "mixins.lycanitestweaks.vanillaextinguishmoddedfire.json")
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurelycanitesfirepassable.json")
    public boolean lycanitesFiresNoBreakCollision = true;

    @Config.Comment("Adds more parity to Repulsion and Weight, repulsion gains weights benefits. This will make Roas, Spectres and Threshers unable to pull an entity with repulsion, as well as disallowing picking up an entity with Repulsion (Behemophet/Fear)")
    @Config.Name("Repulsion Weight Benefits")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurerepulsionweight.json")
    public boolean repulsionWeight = true;

    @Config.Comment("Whether a Lycanite Mob should be considered undead when the Smited effect is active. This will for example allow the Smite enchant to work on them.")
    @Config.Name("Lycanites Smited Are Undead")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuresmitedundeadbasecreature.json")
    public boolean smitedMakesBaseCreatureUndead = true;
}
