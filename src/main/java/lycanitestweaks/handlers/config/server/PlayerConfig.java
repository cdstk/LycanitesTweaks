package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class PlayerConfig {

    @Config.Comment("Use an entity attribute to fully override the vanilla Lycanites \"Spirit Recharge\" and \"Summoning Focus Recharge\" config settings.\n" +
            "If the attribute can not be found on the entity, it will fallback to the original config value.")
    @Config.Name("0. Summoning Attributes")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.playermagicattributes.json")
    public boolean summoningAttributes = true;

    @Config.Comment("Name of the attribute that will be used for Spirit Recharging")
    @Config.Name("Summoning Attributes - Spirit Regen Name")
    public String overrideSpiritRegen = "xat.entityMagic.regen";

    @Config.Comment("A flat value added along with the attribute's value.")
    @Config.Name("Summoning Attributes - Spirit Regen Base")
    public int overrideSpiritRegenBase = -1;

    @Config.Comment("Name of the attribute that will be used for Summoning Focus Recharging")
    @Config.Name("Summoning Attributes - Focus Regen Name")
    public String overrideFocusRegen = "xat.entityMagic.regen";

    @Config.Comment("A flat value added along with the attribute's value.")
    @Config.Name("Summoning Attributes - Focus Regen Base")
    public int overrideFocusRegenBase = -1;
}
