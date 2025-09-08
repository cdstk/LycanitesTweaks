package lycanitestweaks.handlers.config.server;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class ChargeExperienceConfig {

    // https://www.desmos.com/calculator/xfgt2z2xtd
    @Config.Comment("Modify leveling calculation for Vanilla Lycanites, allows the BASE_EXP to be configured for pets and equipment parts.\n" +
            "The defaults will use Vanilla Lycanites between level 1-50, then swap to a slower growing calculation.\n" +
            "Lycanites calc: [BASE * (1 + (lvl - 1) * LINEAR_MULT)]\n" +
            "Optional Calc: [BASE * (1 + ln(lvl) * LOG_MULT)]")
    @Config.Name("0. Modify Charge Experience Calculation")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = false, lateMixin = "mixins.lycanitestweaks.core.modifychargeexperience.json")
    public boolean modifiedExperienceCalc = false;

    @Config.Comment("EXP Value of a single charge. Lycanites default is 50.")
    @Config.Name("Experience Calculation - Charge EXP Value")
    @Config.RangeInt(min = 1)
    public int chargeExperienceValue = 50;

    @Config.Comment("Base EXP Required for pets. Lycanites default is 100.")
    @Config.Name("Experience Calculation - Base for Pets")
    @Config.RangeInt(min = 1)
    public int baseExperiencePets = 100;

    @Config.Comment("Base EXP Required for equipment parts. Lycanites default is 500.")
    @Config.Name("Experience Calculation - Base for Equipment Parts")
    @Config.RangeInt(min = 1)
    public int baseExperienceEquipment = 500;

    @Config.Comment("Linear Multiplier for the Vanilla Lycanites calc. Default is 0.25, +25% of the BASE per level.")
    @Config.Name("Experience Calculation - Linear Multiplier")
    @Config.RangeDouble(min = 0D)
    public float calcLinearMultiplier = 0.25F;

    @Config.Comment("What level the slower growing log calc starts getting used. Set to 0 to disable and only use the vanilla calc.")
    @Config.Name("Experience Calculation - Log Start Level")
    public int calcLogStart = 50;

    @Config.Comment("Log Multiplier for the optional calc. Default values are set to the amount is about equal when the formula swaps.")
    @Config.Name("Experience Calculation - Log Multiplier")
    @Config.RangeDouble(min = 0D)
    public double calcLogMultiplier = 3.15D;
}
