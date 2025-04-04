package lycanitestweaks;

import lycanitestweaks.capability.PlayerMobLevelCapabilityHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.features.boss.DamageLimitCalcHandler;
import lycanitestweaks.handlers.features.boss.RemoveDefaultBossWithLevelsLootHandler;
import lycanitestweaks.handlers.features.effect.ConsumedHandler;
import lycanitestweaks.handlers.features.effect.CleansedHandler;
import lycanitestweaks.handlers.features.effect.VoidedHandler;
import lycanitestweaks.handlers.features.equipment.ItemEquipmentRLCombatSweepHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.proxy.CommonProxy;

@Mod(modid = LycanitesTweaks.MODID, version = LycanitesTweaks.VERSION, name = LycanitesTweaks.NAME, dependencies = "required-after:fermiumbooter")
public class LycanitesTweaks {
    public static final String MODID = "lycanitestweaks";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "LycanitesTweaks";
    public static final Logger LOGGER = LogManager.getLogger();
	
    @SidedProxy(clientSide = "lycanitestweaks.proxy.ClientProxy", serverSide = "lycanitestweaks.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static LycanitesTweaks instance;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LycanitesTweaksRegistry.init();
        LycanitesTweaks.PROXY.preInit();

        if(ForgeConfigHandler.server.pmlConfig.playerMobLevelCapability){
            PlayerMobLevelCapabilityHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(PlayerMobLevelCapabilityHandler.AttachCapabilityHandler.class);
            MinecraftForge.EVENT_BUS.register(PlayerMobLevelCapabilityHandler.class);
        }
        if(ForgeConfigHandler.server.effectsConfig.registerConsumed) MinecraftForge.EVENT_BUS.register(ConsumedHandler.class);
        if(ForgeConfigHandler.server.effectsConfig.registerVoided) MinecraftForge.EVENT_BUS.register(VoidedHandler.class);
        if(!ForgeConfigHandler.server.pmlConfig.registerBossWithLevelsLootTables) MinecraftForge.EVENT_BUS.register(RemoveDefaultBossWithLevelsLootHandler.class);

        if(ForgeConfigHandler.mixinConfig.bossDPSLimitRecalc) MinecraftForge.EVENT_BUS.register(DamageLimitCalcHandler.class);
        if(ForgeConfigHandler.mixinConfig.cleansedEffectList) MinecraftForge.EVENT_BUS.register(CleansedHandler.class);
        if(ForgeConfigHandler.mixinConfig.craftedEquipmentRLCombatSweep) MinecraftForge.EVENT_BUS.register(ItemEquipmentRLCombatSweepHandler.class);
    }
}