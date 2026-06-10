package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

// TODO Rename to Items Config in refactor
@MixinConfig(name = LycanitesTweaks.MODID)
public class CustomStaffConfig {

    @Config.Comment({
            "Some LycanitesTweaks items use the Lycanites Creature Stats system",
            "\thealth - The base health bonus",
            "\tdefense - The base flat damage reduction",
            "\tarmor - The base armor bonus",
            "\tspeed - The base ground speed bonus",
            "\tdamage - The base attack damage, 100% for melee 50% for projectiles",
            "\tattackSpeed - The base melee attack speed, hits per second",
            "\trangedSpeed - The base item use speed, uses per second",
            "\teffect - Not used currently",
            "\tamplifier - Not used currently",
            "\tpierce - The base piercing damage amount",
            "Affected by item level"
    })
    @Config.Name("Custom Item Stats")
    public String[] customItemStats = {
            "lycanitestweaks:hellshield, health: 1, defense: 4",
            "lycanitestweaks:devilgatlinggun, damage: 4, pierce: 4, attackSpeed: 0.2, rangedSpeed: 2",
            "lycanitestweaks:hellfirecannon, damage: 8, pierce: 8, attackSpeed: 0.2, rangedSpeed: 0.1"
    };

    @Config.Comment({
        "Optional overrides of the Vanilla Lycanites \"mob level multipliers\" for Custom Item Stats"
    })
    @Config.Name("Custom Item Stats - Mob Level Multiplier Overrides")
    public String[] customItemLevelMultipliers = {
            "sight=0.0"
    };

    @Config.Comment("The level bonus will be restricted by any creature stat caps set in the Creature Stats Config")
    @Config.Name("Custom Item Stats - Use Creature Stats Caps")
    public boolean customItemStatsCap = true;

    @Config.Comment({
            "Some LycanitesTweaks items have configured Vanilla Equipment Slots and Bauble Types",
            "\tEquipmentSlot - [mainhand, offhand, feet, legs, chest, head]",
            "\t\tA empty entry specifies NULL",
            "\tBaubleType - [0, 1, 2, 3, 4, 5, 6]",
            "\t\tNegative values indicate no bauble slot Attributes from Custom Item Stats",
            "\t\t0 - Amulet",
            "\t\t1 - Ring",
            "\t\t2 - Belt",
            "\t\t3 - Trinket",
            "\t\t4 - Head",
            "\t\t5 - Body",
            "\t\t6 - Charm",
            "\tBaubleLimit - Number of duplicates allowed, set to 0 to disable equipping",
            "Equipment Slot species the slot that Item Stats will be provided as Attributes if possible"
    })
    @Config.Name("Custom Item Slots")
    public String[] customItemSlots = {
            "lycanitestweaks:hellshield, EquipmentSlot:offhand, BaubleType: 3, BaubleLimit: 1",
            "lycanitestweaks:devilgatlinggun, EquipmentSlot:mainhand, BaubleType: 3, BaubleLimit: 1",
            "lycanitestweaks:hellfirecannon, EquipmentSlot:mainhand, BaubleType: 3, BaubleLimit: 1",
            "lycanitestweaks:fantasticalfeast, BaubleType: 3",
            "lycanitestweaks:vilematter, BaubleType: 3"
    };

    @Config.Comment({
            "Adds and registers various items based on the main Lycanites bosses.",
            "\tHellfire Cannon - Dropped by Rahovart. Charges a Hellfire Wave or Barrier attack",
            "\tHellshield - Dropped by Asmodeus. Passively provides defence, can turn on a Life Link shield to split damage between Soulbounds",
            "\tGatling Gun - Dropped by Asmodeus. Takes charges as ammo and shoots them"
    })
    @Config.Name("Register Special Boss Drops")
    @Config.RequiresMcRestart
    public boolean registerSpecialBossDrops = true;

    @Config.Comment({
            "Lycanite projectile names that can not be shot from the Gatling Gun",
            "Various behaviors are very laggy in large quantities",
            "\tCausing Explosion",
            "\tSpawning Mobs",
            "\tSpawning Projectiles"
    })
    @Config.Name("Register Special Boss Drops - Gatling Ammo Blacklist")
    public String[] gatlingAmmoBlacklist = {
            "arcanelaserstorm",
            "demonicblast",
            "lobdarklings"
    };

    @Config.Comment({
            "Adds the special drops to the Vanilla Loot tables of the bosses",
            "Requires \"Vanilla Lootables for Lycanites Mobs\" to be enabled",
    })
    @Config.Name("Register Special Boss Drops - Vanilla Loot Tables")
    @Config.RequiresMcRestart
    public boolean vanillaLootSpecialBossDrops = true;

    public static final String REPLACE_SPECIAL_BOSS_DROPS = "Register Special Boss Drops - JSON Replacements";
    @Config.Comment("Load replacement creature JSON configs that directly adds the special drops.")
    @Config.Name(REPLACE_SPECIAL_BOSS_DROPS)
    @Config.RequiresMcRestart
    public boolean replaceJsonBossDrops = false;

    @Config.Comment({
            "Adds and registers the Fantastical Feast, a passive held item/bauble that boosts Eating and Drinking speeds.",
            "\tStackable consumable are provided a [Stack Count/Max Count] x BOOST% bonus",
            "\t[1/4], [1/16], and [1/64] provides the minimal boost",
            "\t[4/4], [16/16, and [64/64] provides the maximum boost"
    })
    @Config.Name("Register Fantastical Feast")
    @Config.RequiresMcRestart
    public boolean registerFantasticalFeast = true;

    @Config.Comment("Sets Golden Apples and Chorus Fruits as repair materials")
    @Config.Name("Fantastical Feast - Default Repair Items")
    public boolean fantasticalFeastRepairables = true;

    @Config.Comment("Maximum Boost when holding a full stack.")
    @Config.Name("Feeding Frenzy - Eating and Drinking Boost")
    public float feedingFrenzyBoost = 3F;

    @Config.Comment({
            "Adds and registers the Vile Matter, a toggleable passive held item/bauble that provides a debuff aura user.",
            "\tWhen active, debuff the user, any debuffs on the user will be applied to nearby entities",
            "\tThe debuffs are from any selected Beastiary creature which is summonable/tambeable and has Rank 2 Knowledge"
    })
    @Config.Name("Register Vile Matter")
    @Config.RequiresMcRestart
    public boolean registerVileMatter = true;

    @Config.Comment("Sets any item in Lycanites's \"Equipment Mana Items Medium\" config as repair materials")
    @Config.Name("Vile Matter - Default Repair Items")
    public boolean vileMatterRepairables = true;

    @Config.Comment("Tick Rate of the ability")
    @Config.Name("Vile Aura - Tick Rate")
    public int vileAuraTickRate = 20;

    @Config.Comment("Elemental Debuff duration, uses Lycanites Element Info config scaling/modifiers")
    @Config.Name("Vile Aura - Element Duration")
    public int vileAuraDuration = 10;

    @Config.Comment("A multiplier on the Player's Reach Attribute, the range will automatically adjust other mods changes")
    @Config.Name("Vile Aura - Player User Range")
    public float vileAuraRangePlayer = 2F;

    @Config.Comment("A constant range for any user that is not a Player")
    @Config.Name("Vile Aura - Other User Range")
    public float vileAuraRangeOther = 10F;

    @Config.Comment({
            "Adds and registers the Challenge Soul Staff, a temporary single-use summon staff containing an Altar Mini-Boss.",
            "This is intended to be dropped by modified Mini-Boss Altars which use the extra stats that are defined, but never used in the Vanilla Lycanites config.",
            "A Challenge Soul Staff will be added to the drops of those bosses."
    })
    @Config.Name("0. Register Challenge Soul Staffs")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.challengesoulstaff.json")
    public boolean registerChallengeSoulStaffs = true;

    @Config.Comment({
            "Only provided a Challenge Staff drop if a Diamond or Emerald Soulkey is used.",
            "Recommended to use with \"Altar Mini Boss Config Bonus Stats\" and not use the default set of bonus stats."
    })
    @Config.Name("0. Register Challenge Soul Staffs - Diamond and Emerald Key")
    public boolean challengeStaffUncommon = false;

    public static final String REPLACE_EVENTFUL_STAFF_DROPS = "0. Register Eventful Staffs";
    @Config.Comment({
            "Adds and registers the Eventful Staff, summoning staffs that drop from the Seasonal Event mobs to summon them as minions.",
            "Drops are only automatically added if the event JSON configs are loading the default settings.",
            "Drop rate and applicable mobs can be edited, base is 5% drop rate for each custom name mob."
    })
    @Config.Name(REPLACE_EVENTFUL_STAFF_DROPS)
    @Config.RequiresMcRestart
    public boolean registerEventfulStaffs = true;

    @Config.Comment({
            "Adds and registers the Charge Staff, essentially a Vanilla Bow that uses Lycanites Charges as ammo.",
            "Allows many Bow bonuses and enchantments to apply to charge throwing in a simple manner."
    })
    @Config.Name("0. Register Charge Staffs")
    @Config.RequiresMcRestart
    public boolean registerChargeStaffs = true;

    @Config.Comment("Summon Duration in seconds for Challenge Soul Staff minions, basic Summoning Staff minions last for 60 seconds.")
    @Config.Name("Challenge Soul Staff - Summon Duration")
    public int challengeSoulStaffDuration = 300;

    @Config.Comment("Whether Charge Staffs can have enchantments")
    @Config.Name("Charge Staffs Enchantability")
    public boolean chargeStaffEnchantability = true;

    @Config.Comment({
            "List of enchants to blacklist from being applicable to Charge Staffs",
            "Format: [modid: path]"
    })
    @Config.Name("Charge Staff Enchantments Blacklist")
    public String[] blacklistedChargeStaffEnchants = {
            "minecraft:infinity",
            "mujmajnkraftsbettersurvival:arrowrecovery",
            "mujmajnkraftsbettersurvival:blast",
            "mujmajnkraftsbettersurvival:multishot",
            "somanyenchantments:splitshot"
    };
}
