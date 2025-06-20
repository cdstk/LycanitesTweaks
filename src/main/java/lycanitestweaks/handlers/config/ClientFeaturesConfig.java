package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class ClientFeaturesConfig {
    @Config.Comment("Adds LycanitesTweaks Information to Beastiary")
    @Config.Name("Add Feature: LycanitesTweaks Beastiary")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureclientbeastiarylt.json")
    public boolean beastiaryGUILT = true;

    @Config.Comment("Show Imperfect Summoning Information")
    @Config.Name("Add Feature: LycanitesTweaks Beastiary - Imperfect Summoning")
    public boolean beastiaryGUIImperfectSummon = true;

    @Config.Comment("Show Player Mob Levels Information")
    @Config.Name("Add Feature: LycanitesTweaks Beastiary - Player Mob Levels")
    public boolean beastiaryGUIPML = true;

    @Config.Comment("Beastiary Render order is determined by the order of this list\n" +
            "\tcategoryName - Spelling must match 'Bonus Categories' entries else hidden\n" +
            "This will be compared to the existence of 'Bonus Categories' entries in the PML config")
    @Config.Name("Add Feature: LycanitesTweaks Beastiary - PML Category Display Order")
    public String[] pmlBeastiaryOrder = {
            "AltarBossMain",
            "AltarBossMini",
            "DungeonBoss",
            "SpawnerNatural",
            "SpawnerTile",
            "SpawnerTrigger_",
            "EncounterEvent_",
            "SoulboundTame_",
            "SummonMinion"
    };
}
