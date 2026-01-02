package lycanitestweaks.handlers.config.major;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ItemTweaksConfig {

    @Config.Comment("Make offhand crafted equipment RMB ability require player to be sneaking")
    @Config.Name("Equipment Offhand RMB Needs Sneak")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featureequipmentrmbneedssneak.json")
    public boolean equipmentOffhandRMBSneak = true;

    @Config.Comment("Allows Lycanites Equipment to be enchanted based on the parts used.\n" +
            "Feature/Harvest Type -> Enchant Type\n" +
            "damage -> weapon\n" +
            "harvest -> tools\n" +
            "axe/pickaxe/hoe/shovel -> So Many Enchantments Type\n" +
            "Unbreaking is always allowed while Mending can be disabled via config.")
    @Config.Name("Equipment Enchantments")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(
            defaultValue = true,
            earlyMixin = "mixins.lycanitestweaks.feature.equipmentenchantmentsvanilla.json",
            lateMixin = "mixins.lycanitestweaks.feature.equipmentenchantments.json"
    )
    public boolean equipmentEnchantments = true;

    @Config.Comment("Equipment will not be enchanted directly and will instead use Enchantments that are applied to the parts.\n" +
            "True will apply most behaviors of Enchanted Books to Equipment Parts, Equipment can safely be disassembled in this case.\n" +
            "False will not use custom behavior and has enchanting be done on complete Equipment instead")
    @Config.Name("Equipment Enchantments - Parts Store Enchantments")
    public boolean partsStoreEnchants = true;

    @Config.Comment("Allow Equipment Parts to apply illegal enchantment combinations to Equipment")
    @Config.Name("Equipment Enchantments - Parts Apply Illegal Enchantments")
    public boolean partsStoreEnchantsIllegal = false;

    @Config.Comment("Multiplier on the cost in Levels to apply enchanted books through the Anvil")
    @Config.Name("Equipment Enchantments - Anvil Cost Multiplier")
    public float equipmentEnchantingCost = 2.0F;

    @Config.Comment("Enchantability per level of the Equipment Part, setting to -1 will always use 1 regardless of level.\n" +
            "When enchanting a complete Equipment, it will total up the combined levels of each part.\n" +
            "When enchanting an Equipment Part, it will only use the levels of the single part.")
    @Config.Name("Equipment Enchantments - Enchantability")
    public int equipmentEnchantability = 4;

    @Config.Comment("TOOL enchantment general compatibility.\n" +
            "Changes the multi mine behavior from being a natural block break to a player harvest.\n" +
            "Allows block drop modifying enchantments to function, such as silk touch and fortune.")
    @Config.Name("Equipment Multi-Mine TOOL Enchantments Compatibility")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.equipmentharvestblocks.json")
    public boolean equipmentPlayerHarvest = true;

    @Config.Comment("Mending enchantment general compatibility for repairing Equipment Sharpness.\n" +
            "Allows Lycanites Equipment to be enchanted with all BREAKABLE enchantments\n" +
            "Changes the Item properties to become a breakable item instead of an unbreakable one.\n" +
            "Should be safe from fully breaking and automatically works for modded Mending (ex So Many Enchantments).")
    @Config.Name("Equipment Mending Compatibility")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.equipmentmending.json")
    public boolean equipmentMending = true;

    @Config.Comment("Minimum level all parts of equipment must be in order to enchant")
    @Config.Name("Equipment Enchantments - Minimum Part Level")
    public int minimumPartLevelForEnchants = 3;

    @Config.Comment("If equipment should give a tooltip if it has parts that are not high enough level to be enchanted")
    @Config.Name("Equipment Enchantments - Minimum Part Level Tooltips")
    public boolean minimumPartLevelForEnchantsTooltip = true;

    @Config.Comment("If equipment should be prevented from being disassembled if it is enchanted.\n" +
            "If it is allowed, then a tooltip will warn if it will clear enchantments.")
    @Config.Name("Equipment Enchantments - Prevent Disassembling")
    public boolean enchantsPreventDisassemble = false;

    @Config.Comment("List of enchants to additionally blacklist from being applicable to equipment\n" +
            "Format: [modid: path]")
    @Config.Name("Equipment Enchantments Blacklist")
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
    public int summonStaffBaseLevelupExperience = 100;

    @Config.Comment("Wraith Sigil copies variant and level from Rahovart to summon Wraiths with those properties")
    @Config.Name("Wraith Sigil Level Stats")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.wraithsigilcreatureinfo.json")
    public boolean wraithSigilEnhanced = true;

    @Config.Comment("Whether Wraiths should copy all the potion effects of the users.")
    @Config.Name("Wraith Sigil Level Stats - Copy Potions")
    public boolean wraithSigilCopyPotions = true;

    @Config.Comment("Wraiths have 0.5 seconds to melee nearby targets. Lycanites default is 4.0 or 4x one melee hit.")
    @Config.Name("Wraith Sigil Level Stats - Melee Damage Scale")
    public float wraithSigilDamageScale = 4.0F;

    @Config.Comment("Whether Wraiths should apply the debuffs of the \"Voiding\" element")
    @Config.Name("Wraith Sigil Level Stats - Apply Voided")
    public boolean wraithSigilVoiding = true;
}
