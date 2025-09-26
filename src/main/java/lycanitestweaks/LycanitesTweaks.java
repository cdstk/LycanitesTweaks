package lycanitestweaks;

import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapabilityHandler;
import lycanitestweaks.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LycanitesTweaks.MODID, version = LycanitesTweaks.VERSION, name = LycanitesTweaks.NAME, dependencies = "required-after:fermiumbooter@[1.3.0,);required-after:lycanitesmobs")
public class LycanitesTweaks {
    public static final String MODID = "lycanitestweaks";
    public static final String VERSION = "1.0.12-LITE";
    public static final String NAME = "LycanitesTweaks";
    public static final Logger LOGGER = LogManager.getLogger();
	
    @SidedProxy(clientSide = "lycanitestweaks.proxy.ClientProxy", serverSide = "lycanitestweaks.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static LycanitesTweaks instance;
	
	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LycanitesTweaks.PROXY.preInit();

        LycanitesTweaksPlayerCapabilityHandler.registerCapability();
        MinecraftForge.EVENT_BUS.register(LycanitesTweaksPlayerCapabilityHandler.AttachCapabilityHandler.class);
        MinecraftForge.EVENT_BUS.register(LycanitesTweaksPlayerCapabilityHandler.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        LycanitesTweaks.PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}