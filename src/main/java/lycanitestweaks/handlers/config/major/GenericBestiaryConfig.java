package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class GenericBestiaryConfig {

    @Config.Comment({
            "Dependency for functionality: A Bestiary for everything non-Lycanites within the Lycanites Beastiary",
            "\tFunctions similarly to the Lycanites Beastiary with Soulgazer usage and automatic knowledge gain.",
            "\tAutomatic knowledge gain respects Lycanites config and uses Forge events to check breeding, taming, and killing.",
            "\tEvery mod with entities will have a config in config/lycanitesmobs/!lycanitestweaks_ModInfos",
            "\tEvery mod's entity will have a config in config/lycanitesmobs/!lycanitestweaks_GenericBestiaryEntities",
            "\tJSONs should match server and client to prevent desyncs",
            "\tVarious clientside only JSON fields will not be generated/read on server",
            "Command [/lycanitestweaks beastiary]"
    })
    @Config.Name("Mixin Toggle: Enable Generic Bestiary")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.genericbestiary.json")
    public boolean enable = true;

    @Config.Comment({
            "Skips the hard coded JSON writing of default Bestiary Modifications",
            "\tThese are assigned automatically based on vanilla properties and behaviors",
            "Disable to skip this process and NOT write to JSONs"
    })
    @Config.Name("JSON Loading - Disable Default Bestiary Modifications")
    public boolean disableDefaultEntityMods = false;

    @Config.Comment({
            "Skips the hard coded filtering of default Bestiary Modifications from modded entities",
            "\tThe filter helps when a vanilla behavior is override, such as Ice and Fire entity ages",
            "Disable to skip this process and write to JSONs regardless"
    })
    @Config.Name("JSON Loading - Disable Default Modded Conflicts")
    public boolean disableModdedConflicts = false;

    @Config.Comment({
            "Skips the hard coded filtering that removes the bestiary entry of various modded entities",
            "Examples:",
            "\tElectroblob's Wizardry: Exclusively Summoned Mobs",
            "\tIce and Fire: Placeable Mob Eggs, Placeable Mob Skulls, and Stone Statues",
            "\tScape and Run Parasites: Indev Entities, Projectiles, Interactable Mob Gore",
            "Disable to skip this process and make their bestiary entries visible"
    })
    @Config.Name("JSON Loading - Disable Certain Modded Entities")
    public boolean disableModdedSpecific = true;

    @Config.Comment({
            "The time in ticks it takes to be able to use a Soulgazer for knowledge again. Default is 200 (10 seconds).",
            "\tThis is separate from the one used Lycanites entities",
            "Set to -1 to use the Lycanites entity value"
    })
    @Config.Name("Soulgazer - Generic Study Cooldown")
    public int soulgazerStudyCooldown = 200;

    @Config.Comment({
            "The time in ticks it takes to be able to use a Soulgazer for knowledge again. Default is 200 (10 seconds).",
            "\tThis is separate from the one used Lycanites entities",
            "Set to -1 to use the Lycanites entity value"
    })
    @Config.Name("Max Knowledge Bonus")
    public boolean maxKnowledgeBonus = true;

    @Config.Comment({
    })
    @Config.Name("Max Knowledge Bonus - Soulgazer")
    public boolean maxKnowledgeBonusSoulgazer = true;

    @Config.Comment({
    })
    @Config.Name("Max Knowledge Bonus - Boss Damage Reduction")
    public float maxKnowledgeDefenceBoss = 0.03F;

    @Config.Comment({
    })
    @Config.Name("Max Knowledge Bonus - Non Boss Damage Reduction")
    public float maxKnowledgeDefenceNonBoss = 0.01F;
}
