package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class CreatureInteractConfig {

    @Config.Comment("Enables all four armor slots for pets and replace the horse/pet armor slot.\n" +
            "This is an enhanced version of the original incomplete feature as main and off hands are implemented.\n" +
            "All pets will receive this capability.\n" +
            "Warning, any items left in the new slots when this is disabled will be deleted.")
    @Config.Name("Pets Have Full Set of Equipment")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.advancedarmor.json")
    public boolean petFullSetEquipment = true;

    @Config.Comment("Giving an Enchanted Golden Apple to a tamed creature will turn it into a baby")
    @Config.Name("Enchanted Golden Apple Turns Lycanites Pet Into a Baby")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuretamedbabygapple.json")
    public boolean babyAgeGapple = true;

    @Config.Comment("Allows non-boss and non-SpawnedAsBoss Lycanites to be flagged for SpawnedAsBoss transformations.\n" +
            "This does not automatically enable Persistence, if they could despawn before they still can despawn.\n" +
            "Will always trigger a transformation if struck by Lightning.\n")
    @Config.Name("Transform Into Boss NBT Flag")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurecantransformbossflag.json")
    public boolean canTransformIntoBossFlag = true;

    @Config.Comment("Load replacement 'flower' and 'village' JSON spawners that add a rare chance to apply this tag.\n" +
            "1/9 chance for the tag, see generated file as a \"lycanitestweaks:setNBT\" example usage.")
    @Config.Name("Transform Into Boss - Examples")
    @Config.RequiresMcRestart
    public boolean canTransformIntoBossFlagExamples = true;

    @Config.Comment("Sets the Boss Damage Limit upon transformation")
    @Config.Name("Transform Flag Boss - Damage Limit")
    public boolean canTransformBossDamageLimit = true;

    @Config.Comment("Adds Encounter Category Player Mob Levels upon transformation")
    @Config.Name("Transform Flag Boss - PML Encounter Bonus")
    public boolean canTransformBossPML = false;

    @Config.Comment("Inject handling for ALWAYS flagging Can Transform Into Boss for JSON Spawners by whitelist")
    @Config.Name("Transform Flag Boss - JSON Spawner")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurejsonspawnertransformbossflag.json")
    public boolean canTransformBossJSONSpawner = true;

    @Config.Comment("JSON Spawner Names is a blacklist instead of whitelist")
    @Config.Name("Transform Flag Boss - JSON Spawner - Blacklist")
    public boolean transformBossSpawnerNameStringsIsBlacklist = false;

    @Config.Comment("List of Lycanites Spawner Names to attempt to flag Transform Boss\n" +
            "Somewhat Easter Eggs but intended to apply when boat trapping certain mobs")
    @Config.Name("Transform Flag Boss - JSON Spawner - Spawner Names")
    public String[] transformBossSpawnerNameStrings = {
            "mineshaft",
            "sleep"
    };

    @Config.Comment("Allows flying, amphibious, and lava mount movement to have horizontal speed scale to level.\n" +
            "Normally Lycanites only scales mounted vertical speed and grounded horizontal speed.\n")
    @Config.Name("Flying Mount Level Boosted Horizontal Speed")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.mountscaleflyspeed.json")
    public boolean mountFlySpeedScaled = true;

    @Config.Comment("Multiplier on total level scaled speed. Lycanites uses 1.25 for vertical movement.")
    @Config.Name("Flying Mount Level Boosted Horizontal Speed - Modifier")
    public float mountFlySpeedScaledModifier = 1.25F;

    @Config.Comment("Maximum blocks per second. Vanilla Lycanites uses 10.\n" +
            "It is better to set the speed stat cap instead of relying on this.\n" +
            "Set to 10 to disable this extra check and use Lycanites original handling.")
    @Config.Name("Flying Mount Level Boosted Horizontal Speed - Maximum")
    @Config.RangeDouble(min = 0, max = 10)
    public double mountFlySpeedScaledMaxmimum = 10;

    @Config.Comment("Half of the projectile shooting mount abilities required a key press per shot.\n" +
            "This will allow all projectile mount abilities to fire as long as the key is held down.")
    @Config.Name("Mounted Ability Hold to Fire Projectiles")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.rapidmountability.json")
    public boolean mountAbilityRapidFire = true;

    @Config.Comment("Half of the projectile shooting mount abilities were not level scaled unlike their AI attack.\n" +
            "This will allow all projectile mount ability damage to deal more damage with higher levels.")
    @Config.Name("Mounted Ability Projectiles Scaled to Levels")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.scalemountprojectile.json")
    public boolean mountAbilityLevelScaleProjectile = true;

    @Config.Comment("Allow mounts to be use vanilla saddles based on levels")
    @Config.Name("Mount with Vanilla Saddles")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurelimitedvanillasaddle.json")
    public boolean mountVanillaSaddleLimited = true;

    @Config.Comment("In order to use a vanilla saddle, the mount must be at least this level\n" +
            "10 - About half a stack of charges, current default with restricted mount ability.\n" +
            "16 - About a stack and a half of charges, previously used default.")
    @Config.Name("Mount with Vanilla Saddles - Level Requirement")
    public int vanillaSaddleLevelRequirement = 10;

    @Config.Comment("Creatures with the vanilla saddle can use their abilities")
    @Config.Name("Mount with Vanilla Saddles - Allow Abilities")
    public boolean vanillaSaddleAllowAbilities = false;

    @Config.Comment("Allow flying creatures use the vanilla saddle")
    @Config.Name("Mount with Vanilla Saddles - Allow Flying")
    public boolean vanillaSaddleAllowFlying = false;

    @Config.Comment("Allow the pet perch position to be modifiable. Intended to fix large perching pets blocking mouse actions.")
    @Config.Name("Perch Position Modifiable")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featureperchposition.json")
    public boolean perchPositionModifiable = true;

    @Config.Comment("Pet Perch Angle In Radians, Default Lycanites is 90")
    @Config.Name("Perch Position Modifiable - Angle Radians")
    @Config.RangeDouble(min = -360, max = 360)
    public double perchAngle = 90.0D;

    @Config.Comment("Pet Perch Distance Scale, based on ridden entity, Default Lycanites is 0.7")
    @Config.Name("Perch Position Modifiable - Distance")
    @Config.RangeDouble(min = 0)
    public double perchDistance = 1.0D;

    @Config.Comment("Modify distance checks of pickup mobs teleporting victims")
    @Config.Name("Pickup Checks Distances")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featureentitypickupfix.json")
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
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuretamedsizechangefood.json")
    public boolean sizeChangeFoods = true;

    @Config.Comment("Max size change amount based on lycanitesmobs config range, default is 0.1, or 10% of config range")
    @Config.Name("Size Change Foods - Maximum Amount")
    @Config.RangeDouble(min = 0.0)
    public double sizeChangeDegree = 0.1D;

    @Config.Comment("Make Soul Gazing a creature riding an entity dismount and attack the player. Counters boat trapping a mob to gaze it.")
    @Config.Name("Soul Gazer Dismounts")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuresoulgazerdismounts.json")
    public boolean soulGazerDismounts = true;

    @Config.Comment("Enable setting owned creature and animal variant status with Soul Keys")
    @Config.Name("Soulkeys Set Variant")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuresoulkeyvariantset.json")
    public boolean soulkeysSetVariant = true;

    @Config.Comment("Allow creatures to be tamed/studied with their healing foods described in the Beastiary/set in JSONs")
    @Config.Name("Tame Creatures with Beastiary Diet")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuretamewithhealingfood.json")
    public boolean tameWithHealingFood = true;

    @Config.Comment("Beastiary Knowledge gained is based on (Hunger x Saturation), a value generally between 1 and 7.\n" +
            "Vanilla Lycanites uses 100 per treat, or (14/20) per item.\n" +
            "This is a multiplier used on the final value.\n" +
            "The default is 2.0, so about 125 items for 1000 Knowledge using the average food value of 4.")
    @Config.Name("Tame Creatures with Diet - Knowledge Gain Multiplier")
    @Config.RangeDouble(min = 0)
    public float tameWithFoodKnowledgeMult = 2F;

    @Config.Comment("Creature Reputation gained is based on (Hunger x Saturation), a value generally between 1 and 7.\n" +
            "Vanilla Lycanites uses [50-100] per treat and diet food items will use a random number between 1x and 2x the food value.\n" +
            "This is a multiplier used on the final value.\n" +
            "The default is 1.5, so about 112 items for 1000 Reputation using the average food value of 4.")
    @Config.Name("Tame Creatures with Diet - Reputation Gain Multiplier")
    @Config.RangeDouble(min = 0)
    public float tameWithFoodReputationMult = 1.5F;

    @Config.Comment("Allow flying creatures to be tamed with healing food")
    @Config.Name("Tame Creatures with Diet - Allow Flying")
    public boolean tamedWithFoodAllowFlying = false;

    @Config.Comment("Feeding Treats will prevent natural despawning, set temporary duration will still despawn")
    @Config.Name("Treat Sets Persistence")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuretameabletreatpersistence.json")
    public boolean treatSetsPersistence = true;

    @Config.Comment("Blacklist automatically entering riding boats and other vehicles when creature has particular properties.\n" +
            "Creatures are still capable of entering through data or force option with startRiding method.\n" +
            "The default toggled properties targets Sleep Reapers and Mineshaft Banshees.\n" +
            "Nymph is still allowed by default.")
    @Config.Name("Vehicle Anti Cheese")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuremountcheesefix.json")
    public boolean mountCheeseFix = true;

    @Config.Comment("Prevent flying creatures")
    @Config.Name("Vehicle Anti Cheese - Flying")
    public boolean mountCheeseFixFlying = false;

    @Config.Comment("Prevent creatures who are mounted. Fixes player mounts entering another vehicle.")
    @Config.Name("Vehicle Anti Cheese - Mounted")
    public boolean mountCheeseFixMounted = true;

    @Config.Comment("Prevent creatures with No Clip movement")
    @Config.Name("Vehicle Anti Cheese - NoClip")
    public boolean mountCheeseFixNoClip = true;

    @Config.Comment("Transform if Can Transform Into Boss Flag")
    @Config.Name("Vehicle Anti Cheese - Transform Into Boss")
    public boolean mountCheeseFixTransform = true;

}
