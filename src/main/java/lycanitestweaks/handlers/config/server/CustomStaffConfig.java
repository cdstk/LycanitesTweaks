package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class CustomStaffConfig {

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
