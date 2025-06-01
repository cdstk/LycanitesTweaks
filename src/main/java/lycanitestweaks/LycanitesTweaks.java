package lycanitestweaks;

import com.lycanitesmobs.core.info.AltarInfo;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.capability.EntityStoreCreatureCapabilityHandler;
import lycanitestweaks.capability.PlayerMobLevelCapabilityHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.features.entity.EntityLivingHandler;
import lycanitestweaks.handlers.features.entity.EntityLootHandler;
import lycanitestweaks.handlers.features.effect.CripplingEffectsHandler;
import lycanitestweaks.handlers.features.effect.ItemCuringEffectsHandler;
import lycanitestweaks.compat.RLCombatHandler;
import lycanitestweaks.handlers.features.item.ItemSoulgazerMoreInteractionsHandler;
import lycanitestweaks.handlers.features.item.ItemStaffSummingLevelMapHandler;
import lycanitestweaks.info.altar.AltarInfoBeastiary;
import lycanitestweaks.info.altar.AltarInfoZombieHorse;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.proxy.CommonProxy;

@Mod(modid = LycanitesTweaks.MODID, version = LycanitesTweaks.VERSION, name = LycanitesTweaks.NAME, dependencies = "required-after:fermiumbooter@[1.2.0,);required-after:lycanitesmobs")
public class LycanitesTweaks {
    public static final String MODID = "lycanitestweaks";
    public static final String VERSION = "1.0.6";
    public static final String NAME = "LycanitesTweaks";
    public static final Logger LOGGER = LogManager.getLogger();
	
    @SidedProxy(clientSide = "lycanitestweaks.proxy.ClientProxy", serverSide = "lycanitestweaks.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static LycanitesTweaks instance;

    public static final ModInfo modInfo = new ModInfo(LycanitesTweaks.instance, LycanitesTweaks.NAME, LycanitesTweaks.MODID, 100);

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LycanitesTweaksRegistry.init();
        LycanitesTweaks.PROXY.preInit();

        if(ForgeConfigHandler.majorFeaturesConfig.escConfig.entityStoreCreatureCapability){
            EntityStoreCreatureCapabilityHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(EntityStoreCreatureCapabilityHandler.AttachCapabilityHandler.class);
            MinecraftForge.EVENT_BUS.register(EntityStoreCreatureCapabilityHandler.class);
        }
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability){
            PlayerMobLevelCapabilityHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(PlayerMobLevelCapabilityHandler.AttachCapabilityHandler.class);
            MinecraftForge.EVENT_BUS.register(PlayerMobLevelCapabilityHandler.class);
            MinecraftForge.EVENT_BUS.register(ItemSoulgazerMoreInteractionsHandler.class);
        }

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffLevelMap){
            MinecraftForge.EVENT_BUS.register(ItemStaffSummingLevelMapHandler.class);
        }

        MinecraftForge.EVENT_BUS.register(EntityLootHandler.class);
        MinecraftForge.EVENT_BUS.register(EntityLivingHandler.class);
        MinecraftForge.EVENT_BUS.register(ItemCuringEffectsHandler.class);

        if(ForgeConfigHandler.server.effectsConfig.registerConsumed || ForgeConfigHandler.server.effectsConfig.registerVoided)
            MinecraftForge.EVENT_BUS.register(CripplingEffectsHandler.class);

        if(ForgeConfigHandler.integrationConfig.craftedEquipmentRLCombatSweep && ModLoadedUtil.isRLCombatLoaded())
            MinecraftForge.EVENT_BUS.register(RLCombatHandler.class);

        if(ForgeConfigHandler.server.altarsConfig.beastiaryAltar) AltarInfo.addAltar(new AltarInfoBeastiary("BeastiaryAltar"));
        if(ForgeConfigHandler.server.altarsConfig.zombieHorseAltar) AltarInfo.addAltar(new AltarInfoZombieHorse("ZombieHorseAltar"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        LycanitesTweaks.PROXY.init();
    }
}