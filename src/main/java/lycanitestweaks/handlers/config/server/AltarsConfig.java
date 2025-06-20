package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class AltarsConfig {
    @Config.Comment("Register Beastiary Altar - Select any non boss creature from Beastiary to summon\n" +
            "\tInteract Block is Redstone above beacon layers of Obsidian")
    @Config.Name("Add Feature: Beastiary Altar")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureclientbeastiaryaltar.json")
    public boolean beastiaryAltar = true;

    @Config.Comment("Number of Beacon Style Layers required")
    @Config.Name("Add Feature: Beastiary Altar - Beacon Layers of Obsidian")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 4)
    public int beastiaryAltarObsidian = 2;

    @Config.Comment("Beastiary Altar Minimum Creature Knowledge Rank Required")
    @Config.Name("Add Feature: Beastiary Altar - Minimum Knowledge Rank")
    public int beastiaryAltarKnowledgeRank = 2;

    @Config.Comment("Test example for a future Pull Request")
    @Config.Name("Add Feature: Zombie Horse Altar")
    @Config.RequiresMcRestart
    public boolean zombieHorseAltar = true;
}
