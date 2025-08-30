package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ItemTweaksConfig {

    @Config.Comment("Make offhand crafted equipment RMB ability require player to be sneaking")
    @Config.Name("Crafted Equipment Offhand RMB Needs Sneak")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featureequipmentrmbneedssneak.json")
    public boolean craftedEquipmentOffhandRMBSneak = true;

    @Config.Comment("Allows Lycanites Equipment to be enchanted.\n" +
            "Allows all WEAPON enchantments except Sweeping Edge.\n" +
            "Allows Efficiency as the only TOOL enchantment.\n" +
            "Allows Unbreaking as the only BREAKABLE enchantment.\n" +
            "Optional toggles to enable all TOOL and BREAKABLE are available as those require special handling.")
    @Config.Name("Crafted Equipment Enchantments")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.equipmentenchantments.json")
    public boolean craftedEquipmentEnchantments = true;

    @Config.Comment("TOOL enchantment general compatibility.\n" +
            "Changes the multi mine behavior from being a natural block break to a player harvest.\n" +
            "Allows block drop modifying enchantments to function, such as silk touch and fortune.")
    @Config.Name("Crafted Equipment Multi-Mine TOOL Enchantments Compatibility")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.equipmentharvestblocks.json")
    public boolean harvestFeaturePlayerHarvest = true;

    @Config.Comment("Allows Lycanites Equipment to be enchanted with TOOL enchantments")
    @Config.Name("Crafted Equipment Enchantments - Allow TOOL Enchantments")
    public boolean craftedEquipEnchDigger = true;

    @Config.Comment("Mending enchantment general compatibility for repairing Equipment Sharpness.\n" +
            "Allows Lycanites Equipment to be enchanted with all BREAKABLE enchantments\n" +
            "Changes the Item properties to become a breakable item instead of an unbreakable one.\n" +
            "Should be safe from fully breaking and automatically works for modded Mending (ex So Many Enchantments).")
    @Config.Name("Crafted Equipment Mending Compatibility")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.equipmentmending.json")
    public boolean mendingForEquipment = true;

    @Config.Comment("Minimum level all parts of equipment must be in order to enchant")
    @Config.Name("Crafted Equipment Enchantments - Minimum Part Level")
    public int craftedEquipmentEnchantmentsMinLevelParts = 3;

    @Config.Comment("If equipment should give a tooltip if it has parts that are not high enough level to be enchanted")
    @Config.Name("Crafted Equipment Enchantments - Minimum Part Level Tooltips")
    public boolean craftedEquipmentEnchantmentsMinLevelTooltips = true;

    @Config.Comment("If equipment should be prevented from being disassembled if it is enchanted. If it is allowed, than a tooltip will warn that it clears enchantments.")
    @Config.Name("Crafted Equipment Enchantments - Prevent Disassembling")
    public boolean craftedEquipEnchPreventsDisassemble = true;

    @Config.Comment("List of enchants to additionally blacklist from being applicable to equipment\n" +
            "Format: [modid: path]")
    @Config.Name("Crafted Equipment Enchantments Blacklist")
    public String[] blacklistedEquipmentEnchants = {
        "minecraft:sweeping"
    };

    @Config.Comment("Enable customizable effect list and handling for the cleansed/immunization effect")
    @Config.Name("Customizable Curing Item")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.features.customcureitemlist.json")
    public boolean customItemCureEffectList = true;

    @Config.Comment("List of potion resource locations cleansed will cure")
    @Config.Name("Customizable Curing Item - Cleansing Crystal")
    public String[] cleansedEffectsToCure = {
            "minecraft:wither",
            "minecraft:unluck",
            "lycanitesmobs:fear",
            "lycanitesmobs:insomnia",
            "lycanitesmobs:decay",
            "srparasites:fear",
            "mod_lavacow:fear"
    };

    @Config.Comment("List of potion resource locations immunization will cure")
    @Config.Name("Customizable Curing Item - Immunizer")
    public String[] immunizationEffectsToCure = {
            "minecraft:poison",
            "minecraft:hunger",
            "minecraft:weakness",
            "minecraft:nausea",
            "lycanitesmobs:paralysis",
            "lycanitesmobs:bleed",
            "defiledlands:bleeding",
            "srparasites:bleed"
    };

    @Config.Comment("Using the Soulgazer on a tamed pet will provide buffs based on its Elemental properties and Creature stats.\n" +
            "Not all pets are capable of providing buffs")
    @Config.Name("Soulgazer Buff From Pet")
    public boolean soulgazerBuffFromPet = true;

    @Config.Comment("If true, then players must be sneaking in order to obtain the buff")
    @Config.Name("Soulgazer Buff From Pet - Requires Sneak")
    public boolean soulgazerBuffFromPetSneak = true;

    @Config.Comment("Cooldown in ticks, shared with the Creature Study Cooldown")
    @Config.Name("Soulgazer Buff From Pet - Study Cooldown")
    @Config.RangeInt(min = 0)
    public int soulgazerBuffStudyCooldown = 200;

    @Config.Comment("Holding a Soulgazer will prevent debuffs based on active Soulbound pets' elemental properties")
    @Config.Name("Soulgazer Soulbound Debuff Immunities")
    public boolean soulgazerDebuffImmunity = true;

    @Config.Comment("If true, than only the Keybound Pet is checked instead of all active. Requires 'Modify Beastiary Information' to set a Keybound Pet.")
    @Config.Name("Soulgazer Soulbound Debuffs Immunities - Keybound Only")
    public boolean soulgazerDebuffImmunityKeybound = true;

    @Config.Comment("Save and use NBT stored Element Level Map to spawn higher level minions")
    @Config.Name("Summon Staff Level Map")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.summonstafflevelmap.json")
    public boolean summonStaffLevelMap = true;

    @Config.Comment("Summon Staffs can use the Equipment Infuser in order to gain experience")
    @Config.Name("Summon Staff Equipment Infuser")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featuresummonstaffequipmenttiles.json")
    public boolean summonStaffLevelMapEquipmentTiles = true;

    @Config.Comment("Base EXP Required to level up, scales with level, Lycanites Charge EXP is 50")
    @Config.Name("Summon Staff - Base Levelup Experience")
    public int summonStaffBaseLevelupExperience = 500;

}
