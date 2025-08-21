package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ClientFeaturesConfig {

    @Config.Comment("Dependency for adding new/hiding Beastiary information. Required for server-side to know what Creature players have selected.")
    @Config.Name("0. Modify Beastiary Information")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featureclientbeastiarylt.json")
    public boolean beastiaryGUILT = true;

    @Config.Comment("Adds a tab for Lycanites Altar renders and block counts")
    @Config.Name("0.a Lycanites Mobs Altar Beastiary Tab")
    public boolean beastiaryGUIAltars = true;

    @Config.Comment("Adds a tab to show Player Mob Levels information")
    @Config.Name("0.a LycanitesTweaks Player Mob Levels Beastiary Tab")
    public boolean beastiaryGUIPML = true;

    @Config.Comment("Enables the ability for the Equipment Infuser and Station to display progress bars for additional items\n" +
            "Ex. LycanitesTweaks Enchanted Soulkeys and Modified Summoning Staffs.")
    @Config.Name("1. Infuser and Display Additional Items")
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.client.infuserstationdisplaycustom.json")
    public boolean lycanitesTilesCustomItems = true;

    @Config.Comment("PML Beastiary Render order is determined by the order of this list.\n" +
            "\tcategoryName - Spelling must match 'Bonus Categories' entries else hidden\n" +
            "This will be compared to the existence of 'Bonus Categories' entries in the PML config")
    @Config.Name("Player Mob Levels Category Display Order")
    public String[] pmlBeastiaryOrder = {
            "AltarBossMain",
            "AltarBossMini",
            "DungeonBoss",
            "SpawnerNatural",
            "SpawnerTile",
            "SpawnerTrigger_",
            "EncounterEvent_",
            "SoulboundTame_",
            "SummonMinion",
            "SummonMinionInstant"
    };

    @Config.Comment("Adds stats on Summoning tab that shows Imperfect Summoning information")
    @Config.Name("LycanitesTweaks Imperfect Summoning Tab")
    public boolean beastiaryGUIImperfectSummon = true;

    @Config.Comment("Case sensitive blacklist for hiding any Altar by name. Does not affect gameplay.")
    @Config.Name("Altar Display Blacklist")
    public String[] altarInfoBeastiaryBlacklist = {

    };

    @Config.Comment("Case sensitive blacklist for hiding any Creature by name. Does not affect gameplay.\n" +
            "Used by Lycanites Tweaks to hide easter eggs.")
    @Config.Name("Creature Display Blacklist")
    public String[] creatureInfoBeastiaryBlacklist = {
            "sonofamalgalich"
    };

    @Config.Comment("Case sensitive blacklist for hiding any Creature Subspecies by name. Does not affect gameplay.\n" +
            "Used by Lycanites Tweaks to hide easter eggs.\n" +
            "\tFormat: [creatureName: subspeciesIndex]")
    @Config.Name("Creature Subspecies Display Blacklist")
    public String[] creatureSubspeciesInfoBeastiaryBlacklist = {
            "darkling: 111"
    };

    @Config.Comment("Case sensitive blacklist for hiding any Element by name. Does not affect gameplay.")
    @Config.Name("Element Display Blacklist")
    public String[] elementInfoBeastiaryBlacklist = {
            "nightmare",
            "viral"
    };

    @Config.Comment("Have long tooltips be shortened and require Shift to be held down in order to show the full description.\n" +
            "Equipment Mob Parts - Hides weapon stats and simplifies to xp, element, and slots.\n" +
            "Equipment - Hides individual mob parts, block harvest, and melee effects.")
    @Config.Name("Shorten/Expand Tooltips With Shift")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.client.tooltiphideexpand.json")
    public boolean shortenTooltips = true;

    @Config.Comment("Replace the denial of sleep message mentioning monsters nearby with one that directly mentions Insomnia.\n" +
            "Langkey is: \"tile.bed.lycanites.insomnia\"")
    @Config.Name("Unique Insomnia Message")
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.client.insomniamessage.json")
    public boolean insomniaMessage = true;
}
