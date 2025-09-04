package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.config.server.AltarsConfig;
import lycanitestweaks.handlers.config.server.ChargeExperienceConfig;
import lycanitestweaks.handlers.config.server.CustomStaffConfig;
import lycanitestweaks.handlers.config.server.EnchantedSoulkeyConfig;
import lycanitestweaks.handlers.config.server.LootConfig;
import lycanitestweaks.handlers.config.server.PotionEffectsConfig;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ServerConfig {

    @Config.Comment("Manage various Soulkey Altars provided by LycanitesTweaks. They are functional examples of custom Altars with rendering in the Beastiary.")
    @Config.Name("Additional Altars")
    public final AltarsConfig altarsConfig = new AltarsConfig();

    @Config.Comment("LycanitesTweaks adds two new Potion Effects (Voided and Consumed) that are only intended for the main boss fights. \n" +
            "Voided is used by Rahovart and Asmodeus, while Consumed is used by Amalgalich\n" +
            "Both effects are fully customisable and intended to make the boss fights harder\n" +
            "By default, Voided stops the player from getting new buffs and slightly decreases their health (-10%).\n" +
            "While Consumed not only stops buffs from being added, but also removes all current buffs, decreases the players health to 5% and blocks item use (rightclick).\n" +
            "Additionally, Voided will turn all environmental damages to piercing damage, while Consumed does that with any damage")
    @Config.Name("Additional Effects")
    public final PotionEffectsConfig effectsConfig = new PotionEffectsConfig();

    @Config.Comment("Manage the leveling calculation for anything that uses Charges to level up.\n" +
            "Pets and Equipment configs are here as Vanilla Lycanites does not provide them.\n" +
            "LycanitesTweaks feature configs are provided in their respective config group.")
    @Config.Name("Charge Leveling Experience")
    public final ChargeExperienceConfig chargeExpConfig = new ChargeExperienceConfig();

    @Config.Comment("Various staffs based on the current Summon Staffs and the older Scepters.\n" +
            "Charge Staff - Essentially a bow that shoots Lycanites charges.\n" +
            "Challenge Soul Staff - Dropped from Altar bosses, summons them as a temporary minion.\n" +
            "Eventful Staff - Seasonal drop, basic Summoning Staff that always summons a specific minion.")
    @Config.Name("Custom Staffs")
    public final CustomStaffConfig customStaffConfig = new CustomStaffConfig();

    @Config.Comment("Enchanted Soulkeys are a better version of existing Soulkeys.\n" +
            "They not only allow for storing multiple usages for ease of access\n" +
            "but also allow for increasing the summoned bosses creature level when held in mainhand when the boss spawns.\n" +
            "Usages can be increased by adding Nether Stars and Gem (Diamond/Emerald) Blocks to the key inside the Equipment Station.\n" +
            "Variant Soulkeys will need two Gem Blocks for one usage, while normal soulkeys only need one. Both need one Nether Star per usage.\n" +
            "The Creature Level that this key summons can be increased inside the Equipment Infuser using Lycanites Charges")
    @Config.Name("Enchanted Soulkey")
    public final EnchantedSoulkeyConfig enchSoulkeyConfig = new EnchantedSoulkeyConfig();

    @Config.Comment("Manage the ability to use vanilla loot tables (accessible via resource packs or loottweaker) for Lycanite entities.\n" +
            "Toggles the use of JSON loot tables for Bosses meant to provide Emeralds, XP Bottles, and Enchanted Book that reflect Boss' Levels.\n" +
            "Adjust a dynamic loot table that allows mobs to drop any charge from the entities' set of element properties.")
    @Config.Name("Additional Loot")
    public final LootConfig lootConfig = new LootConfig();

    @Config.Comment("Whether Lycanites Block Protection protects against any Living Entity, not just players")
    @Config.Name("Block Protection Living Event")
    public boolean blockProtectionLivingEvent = true;
}