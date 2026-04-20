package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

// TODO Rename to Items Config in refactor
@MixinConfig(name = LycanitesTweaks.MODID)
public class CustomStaffConfig {

    @Config.Comment({
            "Adds and registers the Fantastical Feast, a passive held item/bauble that boosts Eating and Drinking speeds." +
            "\tStackable consumable are provided a [Stack Count/Max Count] x BOOST% bonus" +
            "\t[1/4], [1/16], and [1/64] provides the minimal boost" +
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
            "Adds and registers the Vile Matter, a toggleable passive held item/bauble that provides a debuff aura user." +
            "\tWhen active, debuff the user, any debuffs on the user will be applied to nearby entities" +
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

    @Config.Comment("Adds and registers the Challenge Soul Staff, a temporary single-use summon staff containing an Altar Mini-Boss.\n" +
            "This is intended to be dropped by modified Mini-Boss Altars which use the extra stats that are defined, but never used in the Vanilla Lycanites config.\n" +
            "A Challenge Soul Staff will be added to the drops of those bosses.")
    @Config.Name("0. Register Challenge Soul Staffs")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.challengesoulstaff.json")
    public boolean registerChallengeSoulStaffs = true;

    @Config.Comment("Only provided a Challenge Staff drop if a Diamond or Emerald Soulkey is used.\n" +
            "Recommended to use with \"Altar Mini Boss Config Bonus Stats\" and not use the default set of bonus stats.")
    @Config.Name("0. Register Challenge Soul Staffs - Diamond and Emerald Key")
    public boolean challengeStaffUncommon = false;

    public static final String REPLACE_EVENTFUL_STAFF_DROPS = "0. Register Eventful Staffs";
    @Config.Comment("Adds and registers the Eventful Staff, summoning staffs that drop from the Seasonal Event mobs to summon them as minions.\n" +
            "Drops are only automatically added if the event JSON configs are loading the default settings.\n" +
            "Drop rate and applicable mobs can be edited, base is 5% drop rate for each custom name mob.")
    @Config.Name(REPLACE_EVENTFUL_STAFF_DROPS)
    @Config.RequiresMcRestart
    public boolean registerEventfulStaffs = true;

    @Config.Comment("Adds and registers the Charge Staff, essentially a Vanilla Bow that uses Lycanites Charges as ammo.\n" +
            "Allows many Bow bonuses and enchantments to apply to charge throwing in a simple manner.")
    @Config.Name("0. Register Charge Staffs")
    @Config.RequiresMcRestart
    public boolean registerChargeStaffs = true;

    @Config.Comment("Summon Duration in seconds for Challenge Soul Staff minions, basic Summoning Staff minions last for 60 seconds.")
    @Config.Name("Challenge Soul Staff - Summon Duration")
    public int challengeSoulStaffDuration = 300;

    @Config.Comment("Charge Staffs momentarily spawn in an arrow so that other mods may modify it in order to apply bonuses to charge projectiles.\n" +
            "This toggle controls whether these arrows can persist to apply effects that charge projectiles are unable to copy, such as knockback properties.")
    @Config.Name("Charge Staffs Arrows")
    public boolean chargeStaffArrowsWorld = false;

    @Config.Comment("Charge Staffs momentarily spawn in an arrow so that other mods may modify it in order to apply bonuses to charge projectiles.\n" +
            "This toggle controls whether these arrows teleport every tick to the charge the arrow is linked to.")
    @Config.Name("Charge Staffs Arrows Follow Charge")
    public boolean chargeStaffArrowsTeleport = false;

    @Config.Comment("Whether Charge Staffs can have enchantments")
    @Config.Name("Charge Staffs Enchantability")
    public boolean chargeStaffEnchantability = true;

    @Config.Comment("List of enchants to blacklist from being applicable to Charge Staffs\n" +
            "Format: [modid: path]")
    @Config.Name("Charge Staff Enchantments Blacklist")
    public String[] blacklistedChargeStaffEnchants = {
            "minecraft:infinity",
            "minecraft:flame",
            "minecraft:punch",
            "mujmajnkraftsbettersurvival:arrowrecovery",
            "mujmajnkraftsbettersurvival:blast",
            "mujmajnkraftsbettersurvival:multishot",
            "somanyenchantments:lesserflame",
            "somanyenchantments:advancedflame",
            "somanyenchantments:supremeflame",
            "somanyenchantments:advancedpunch",
            "somanyenchantments:rune_arrowpiercing",
            "somanyenchantments:splitshot"
    };
}
