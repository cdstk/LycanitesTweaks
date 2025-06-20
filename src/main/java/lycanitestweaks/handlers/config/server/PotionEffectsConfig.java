package lycanitestweaks.handlers.config.server;

import net.minecraftforge.common.config.Config;

public class PotionEffectsConfig {
    @Config.Comment("Register Consumed Potion Effect")
    @Config.Name("Register Consumed")
    @Config.RequiresMcRestart
    public boolean registerConsumed = true;

    @Config.Comment("Consumed denies buffs being applied")
    @Config.Name("Consumed Buffs Denies")
    public boolean consumedDeniesBuffs = true;

    @Config.Comment("Consumed removes buffs when applied")
    @Config.Name("Consumed Buffs Removes")
    public boolean consumedRemovesBuffs = true;

    @Config.Comment("Consumed Max Health multiplied total to reduce (-0.95 means -95% health or being left over with 5% health)")
    @Config.Name("Consumed Health Modifier")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = -1D, max = 0)
    public double consumedHealthModifier = -0.95D;

    @Config.Comment("Consumed applies item cooldown")
    @Config.Name("Consumed Item Cooldown Ticks")
    @Config.RangeInt(min = 0)
    public int consumedItemCooldown = 10;

    @Config.Comment("Consumed makes all damage piercing")
    @Config.Name("Consumed Piercing All")
    public boolean consumedPiercingAll = true;

    @Config.Comment("Consumed makes environmental damage piercing")
    @Config.Name("Consumed Piercing Environment")
    public boolean consumedPiercingEnvironment = false;

    @Config.Comment("Register Voided Potion Effect")
    @Config.Name("Register Voided")
    @Config.RequiresMcRestart
    public boolean registerVoided = true;

    @Config.Comment("Voided denies buffs being applied")
    @Config.Name("Voided Buffs Denies")
    public boolean voidedDeniesBuffs = true;

    @Config.Comment("Voided removes buffs when applied")
    @Config.Name("Voided Buffs Removes")
    public boolean voidedRemovesBuffs = false;

    @Config.Comment("Voided Max Health multiplied total to reduce (-0.1 means -10% health or being left over with 90% health)")
    @Config.Name("Voided Health Modifier")
    @Config.RequiresMcRestart
    @Config.RangeDouble(min = -1D, max = 0)
    public double voidedHealthModifier = -0.1D;

    @Config.Comment("Voided applies item cooldown")
    @Config.Name("Voided Item Cooldown Ticks")
    @Config.RangeInt(min = 0)
    public int voidedItemCooldown = 0;

    @Config.Comment("Voided makes all damage piercing")
    @Config.Name("Voided Piercing All")
    public boolean voidedPiercingAll = false;

    @Config.Comment("Voided makes environmental damage piercing")
    @Config.Name("Voided Piercing Environment")
    public boolean voidedPiercingEnvironment = true;
}
