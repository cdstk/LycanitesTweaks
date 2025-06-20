package lycanitestweaks.potion;

import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.SharedMonsterAttributes;

public class PotionVoided extends PotionCripplingBase {

    public static final PotionVoided INSTANCE = new PotionVoided();
    public String UUID_HEALTH = "957edfae-9e9c-43e7-9910-f2665e2bdb9a";

    public PotionVoided() {
        super("voided", true, 0x000000);
        this.healthModifier = ForgeConfigHandler.server.effectsConfig.voidedHealthModifier;
        this.itemCooldown = ForgeConfigHandler.server.effectsConfig.voidedItemCooldown;
        this.deniesBuffs = ForgeConfigHandler.server.effectsConfig.voidedDeniesBuffs;
        this.removesBuffs = ForgeConfigHandler.server.effectsConfig.voidedRemovesBuffs;
        this.piercingEnvironment = ForgeConfigHandler.server.effectsConfig.voidedPiercingEnvironment;
        this.piercingAll = ForgeConfigHandler.server.effectsConfig.voidedPiercingAll;
        registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, this.UUID_HEALTH, ForgeConfigHandler.server.effectsConfig.voidedHealthModifier, 2);
    }
}