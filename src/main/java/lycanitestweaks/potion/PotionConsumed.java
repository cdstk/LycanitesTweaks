package lycanitestweaks.potion;

import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.SharedMonsterAttributes;

public class PotionConsumed extends PotionCripplingBase {

    public static final PotionConsumed INSTANCE = new PotionConsumed();
    public String UUID_HEALTH = "a7c92128-9cd8-4309-9c94-352e3be1029b";

    public PotionConsumed() {
        super("consumed", true, 0xFFFFFF);
        this.healthModifier = ForgeConfigHandler.server.effectsConfig.consumedHealthModifier;
        this.itemCooldown = ForgeConfigHandler.server.effectsConfig.consumedItemCooldown;
        this.deniesBuffs = ForgeConfigHandler.server.effectsConfig.consumedDeniesBuffs;
        this.removesBuffs = ForgeConfigHandler.server.effectsConfig.consumedRemovesBuffs;
        this.piercingEnvironment = ForgeConfigHandler.server.effectsConfig.consumedPiercingEnvironment;
        this.piercingAll = ForgeConfigHandler.server.effectsConfig.consumedPiercingAll;
        registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, this.UUID_HEALTH, this.healthModifier, 2);
    }
}