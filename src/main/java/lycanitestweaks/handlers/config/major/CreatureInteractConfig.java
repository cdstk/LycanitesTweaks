package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraftforge.common.config.Config;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CreatureInteractConfig {

    private static Set<String> transformBossSpawnerNames = null;

    @Config.Comment("Giving an Enchanted Golden Apple to a tamed creature will turn it into a baby")
    @Config.Name("Baby Age Gapple")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuretamedbabygapple.json")
    public boolean babyAgeGapple = true;

    @Config.Comment("Allows non boss and non SpawnedAsBoss creatures to be flagged for SpawnedAsBoss transformations.\n" +
            "This does not automatically enable Persistence, if they could despawn before they still can despawn.")
    @Config.Name("Can Transform Into Boss Flag")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurecantransformbossflag.json")
    public boolean canTransformIntoBossFlag = true;

    @Config.Comment("Sets the Boss Damage Limit upon transformation")
    @Config.Name("Can Transform Into Boss Flag - Boss Damage Limit")
    public boolean canTransformBossDamageLimit = true;

    @Config.Comment("Add Encounter Category Player Mob Levels upon transformation")
    @Config.Name("Can Transform Into Boss Flag - Encounter PML")
    public boolean canTransformBossPML = true;

    @Config.Comment("Inject handling for flagging JSON Can Transform Into Boss JSON Spawners by whitelist")
    @Config.Name("JSON Spawner Flag Transform Boss")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurejsonspawnertransformbossflag.json")
    public boolean canTransformBossJSONSpawner = true;

    @Config.Comment("JSON Spawner Names is a blacklist instead of whitelist")
    @Config.Name("JSON Spawner Flag Transform Boss - Blacklist")
    public boolean transformBossSpawnerNameStringsIsBlacklist = false;

    @Config.Comment("List of Lycanites Spawner Names to attempt to flag Transform Boss\n" +
            "Somewhat Easter Eggs but intended to apply when boat trapping certain mobs\n" +
            "Add 'flower' when using 'Vehicle Anti Cheese - Transform Into Boss' if you want to be evil")
    @Config.Name("JSON Spawner Flag Transform Boss - Spawner Names")
    public String[] transformBossSpawnerNameStrings = {
            "gem",
            "glowstone",
            "mineshaft",
            "mushroom",
            "ore",
            "portal",
            "pumpkin",
            "sleep",
            "tree",
            "village"
    };

    @Config.Comment("Allow mounts to be use vanilla saddles based on levels")
    @Config.Name("Mount with Vanilla Saddles")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featurelimitedvanillasaddle.json")
    public boolean mountVanillaSaddleLimited = true;

    @Config.Comment("In order to use a vanilla saddle, the mount must be at least this level")
    @Config.Name("Mount with Vanilla Saddles - Level Requirement")
    public int vanillaSaddleLevelRequirement = 16;

    @Config.Comment("Allow flying creatures use the vanilla saddle")
    @Config.Name("Mount with Vanilla Saddles - Allow Flying")
    public boolean vanillaSaddleAllowFlying = false;

    @Config.Comment("Allow the pet perch position to be modifiable")
    @Config.Name("Perch Position Modifiable")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureperchposition.json")
    public boolean perchPositionModifiable = true;

    @Config.Comment("Pet Perch Angle In Radians, Default Lycanites is 90")
    @Config.Name("Perch Position Modifiable - Angle Radians")
    @Config.RangeDouble(min = -360, max = 360)
    public double perchAngle = 90.0D;

    @Config.Comment("Pet Perch Distance Scale, based on ridden entity, Default Lycanites is 0.7")
    @Config.Name("Perch Position Modifiable - Distance")
    @Config.RangeDouble(min = 0)
    public double perchDistance = 1.0D;

    @Config.Comment("Modify distance checks to pickup mobs teleporting victims")
    @Config.Name("Pickup Checks Distances")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureentitypickupfix.json")
    public boolean pickupChecksDistance = true;

    @Config.Comment("Additionally have Darklings run the check for latch target")
    @Config.Name("Pickup Checks Distances - Darkling")
    public boolean pickupChecksDarkling = true;

    @Config.Comment("Distance between entities to trigger auto pickup drop, Default Lycanites is 32.")
    @Config.Name("Pickup Checks Distances - Value")
    @Config.RangeDouble(min = 0)
    public double pickUpDistance = 8.0D;

    @Config.Comment("Feeding tamed creatures Burritos and Risottos will increase/decrease size scale")
    @Config.Name("Size Change Foods")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuretamedsizechangefood.json")
    public boolean sizeChangeFoods = true;

    @Config.Comment("Max size change amount based on lycanitesmobs config range, default is 0.1, or 10% of config range")
    @Config.Name("Size Change Foods - Maximum Amount")
    @Config.RangeDouble(min = 0.0)
    public double sizeChangeDegree = 0.1D;

    @Config.Comment("Make Soul Gazing a creature riding an entity dismount and attack the player")
    @Config.Name("Soul Gazer Dismounts")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuresoulgazerdismounts.json")
    public boolean soulGazerDismounts = true;

    @Config.Comment("Enable setting owned creature and animal variant status with Soul Keys")
    @Config.Name("Soulkeys Set Variant")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuresoulkeyvariantset.json")
    public boolean soulkeysSetVariant = true;

    @Config.Comment("Allow creatures to be tamed/studied with their healing foods")
    @Config.Name("Tame Creatures with Diet")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuretamewithhealingfood.json")
    public boolean tameWithHealingFood = true;

    @Config.Comment("Chance for a successful attempt to tame with healing food")
    @Config.Name("Tame Creatures with Diet - Chance")
    @Config.RangeDouble(min = 0.0, max = 1.0)
    public float tameWithFoodChance = 0.3F;

    @Config.Comment("Allow flying creatures to be tamed with healing food")
    @Config.Name("Tame Creatures with Diet - Allow Flying")
    public boolean tamedWithFoodAllowFlying = false;

    @Config.Comment("Feeding Treats will prevent natural despawning, set temporary duration will still despawn")
    @Config.Name("Treat Sets Persistence")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuretameabletreatpersistence.json")
    public boolean treatSetsPersistence = true;

    @Config.Comment("Blacklist automatically entering riding boats and other vehicles when creature has particular properties\n" +
            "Creatures are still capable of entering through data or force option with startRiding method\n" +
            "The default toggled properties targets Sleep Reapers and Mineshaft Banshees\n" +
            "Nymph is still allowed by default")
    @Config.Name("Vehicle Anti Cheese")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featuremountcheesefix.json")
    public boolean mountCheeseFix = true;

    @Config.Comment("Prevent flying creatures")
    @Config.Name("Vehicle Anti Cheese - Flying")
    public boolean mountCheeseFixFlying = false;

    @Config.Comment("Prevent creatures who are mounted")
    @Config.Name("Vehicle Anti Cheese - Mounted")
    public boolean mountCheeseFixMounted = true;

    @Config.Comment("Prevent creatures with No Clip movement")
    @Config.Name("Vehicle Anti Cheese - NoClip")
    public boolean mountCheeseFixNoClip = true;

    @Config.Comment("Transform if Can Transform Into Boss Flag")
    @Config.Name("Vehicle Anti Cheese - Transform Into Boss")
    public boolean mountCheeseFixTransform = true;

    public static Set<String> getCanTransformIntoBossSpawnerNames(){
        if(CreatureInteractConfig.transformBossSpawnerNames == null)
            CreatureInteractConfig.transformBossSpawnerNames = Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.transformBossSpawnerNameStrings)
                    .collect(Collectors.toSet());
        return CreatureInteractConfig.transformBossSpawnerNames;
    }

    public static void reset(){
        CreatureInteractConfig.transformBossSpawnerNames = null;
    }
}
