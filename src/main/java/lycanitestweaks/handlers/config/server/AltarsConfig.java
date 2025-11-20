package lycanitestweaks.handlers.config.server;

import net.minecraftforge.common.config.Config;

public class AltarsConfig {

    @Config.Comment("Select any non boss creature from Beastiary to summon.\n" +
            "Interact block is Redstone above beacon layers of Obsidian.\n" +
            "Requires 'Modify Beastiary Information' for server-side to recognize a selected creature.")
    @Config.Name("Beastiary Altar")
    @Config.RequiresMcRestart
    public boolean beastiaryAltar = true;

    @Config.Comment("Number of Beacon style layers required")
    @Config.Name("Beastiary Altar - Beacon Layers of Obsidian")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 4)
    public int beastiaryAltarObsidian = 2;

    @Config.Comment("Beastiary Altar minimum Creature Knowledge Rank required")
    @Config.Name("Beastiary Altar - Minimum Knowledge Rank")
    public int beastiaryAltarKnowledgeRank = 2;

    @Config.Comment("Altars for misc entities (ex Zombie Horse and Charged Creeper).")
    @Config.Name("Vanilla Entity Altars")
    @Config.RequiresMcRestart
    public boolean vanillaEntityAltars = true;

    public static final String ADD_WITHERING_HEIGHTS_ALTAR = "Withering Heights Event Altar";
    @Config.Comment("Altar for JSON configurable \"lycanitestweaks_witheringheights\" event. Requires Lycanites JSON loading to work automatically.\n" +
            "The event is considered a boss event, spawning a Wither every 1 second over 30 seconds.")
    @Config.Name(ADD_WITHERING_HEIGHTS_ALTAR)
    @Config.RequiresMcRestart
    public boolean witheringHeightsAltar = true;

    @Config.Comment("Number of Beacon style layers required. Outer rings are Wither Skulls while inner section is Soul Sand. See in-game render for a visual.")
    @Config.Name("Withering Heights Altar - Beacon Layers")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 4)
    public int witheringHeightsAltarObsidian = 4;
}
