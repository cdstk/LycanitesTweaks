package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class EnchantedSoulkeyConfig {
    @Config.Comment("Holding the key in mainhand will add Creature Levels to Altar Mini Bosses")
    @Config.Name("Add Feature: Works for Altar Mini Bosses")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureenchantedsoulkeyaltarminiboss.json")
    public boolean altarMiniBoss = true;

    @Config.Comment("Holding the key in mainhand will add Creature Levels to Altar Main Bosses")
    @Config.Name("Add Feature: Works for Altar Main Bosses")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureenchantedsoulkeymainboss.json")
    public boolean altarMainBoss = true;

    @Config.Comment("Allow keys to be put inside Equipment Infuser to level up, and inside Equipment Station to recharge")
    @Config.Name("Add Feature: Allow on Equipment Station and Infuser")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureenchantedsoulkeyequipmenttiles.json")
    public boolean allowStationAndInfuser = true;

    @Config.Comment("Base charge experience required to level up the key. Increases by 25% per level of the key until the max is reached. Each Lycanites Charge gives 50 experience")
    @Config.Name("Base Levelup Experience")
    public int baseLevelupExperience = 500;

    @Config.Comment("Leveling up the key will never cost more than this amount of charge experience.")
    @Config.Name("Max Levelup Experience")
    public int maxLevelupExperience = 2500;

    @Config.Comment("Default Maximum Creature Level for mobs that can be summoned with the key. Can be overriden with NBT")
    @Config.Name("Max Creature Level")
    public int defaultMaxLevel = 100;

    @Config.Comment("Default Usages when crafted. The default value reflects the amount of nether stars and gem blocks that were used in the default crafting recipe for the key.")
    @Config.Name("Usages On Craft")
    public int usagesOnCraft = 8;

    @Config.Comment("Enchanted Soulkeys will not be able to store more than this amount of nether stars / gem blocks as usages.")
    @Config.Name("Max Usages")
    public int maxUsages = 1000;
}
