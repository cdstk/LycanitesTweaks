package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.handlers.config.server.AltarsConfig;
import lycanitestweaks.handlers.config.server.EnchantedSoulkeyConfig;
import lycanitestweaks.handlers.config.server.LootConfig;
import lycanitestweaks.handlers.config.server.PotionEffectsConfig;
import net.minecraftforge.common.config.Config;

public class ServerConfig {

    @Config.Name("Additional Altars")
    @MixinConfig.SubInstance
    public final AltarsConfig altarsConfig = new AltarsConfig();

    @Config.Comment("LycanitesTweaks adds two new Potion Effects (Voided and Consumed) that are only intended for the main boss fights. \n" +
            "Voided is used by Rahovart and Asmodeus, while Consumed is used by Amalgalich\n" +
            "Both effects are fully customisable and intended to make the boss fights harder\n" +
            "By default, Voided stops the player from getting new buffs and slightly decreases their health (-10%).\n" +
            "While Consumed not only stops buffs from being added, but also removes all current buffs, decreases the players health to 5% and blocks item use (rightclick).\n" +
            "Additionally, Voided will turn all environmental damages to piercing damage, while Consumed does that with any damage")
    @Config.Name("Additional Effects")
    public final PotionEffectsConfig effectsConfig = new PotionEffectsConfig();

    @Config.Comment("Enchanted Soulkeys are a better version of existing Soulkeys.\n" +
            "They not only allow for storing multiple usages for ease of access\n" +
            "but also allow for increasing the summoned bosses creature level when held in mainhand when the boss spawns.\n" +
            "Usages can be increased by adding Nether Stars and Gem (Diamond/Emerald) Blocks to the key inside the Equipment Station.\n" +
            "Variant Soulkeys will need two Gem Blocks for one usage, while normal soulkeys only need one. Both need one Nether Star per usage.\n" +
            "The Creature Level that this key summons can be increased inside the Equipment Infuser using Lycanites Charges")
    @Config.Name("Enchanted Soulkey")
    @MixinConfig.SubInstance
    public final EnchantedSoulkeyConfig enchSoulkeyConfig = new EnchantedSoulkeyConfig();

    @Config.Comment("Here you could explain that you added vanilla loot tables (accessible via resourcepacks or loottweaker) for all lyca mobs\n" +
            "and what kinda loot tables you added for what kinda mobs")
    @Config.Name("Additional Loot")
    @MixinConfig.SubInstance
    public final LootConfig lootConfig = new LootConfig();

    @Config.Comment("Whether Lycanites Block Protection protects against any Living Entity, not just players")
    @Config.Name("Block Protection Living Event")
    public boolean blockProtectionLivingEvent = true;
}