package lycanitestweaks.handlers;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.config.*;
import lycanitestweaks.handlers.config.major.CreatureInteractConfig;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.handlers.config.major.ItemTweaksConfig;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LycanitesTweaks.MODID)
public class ForgeConfigHandler {

	@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();

	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	@MixinConfig.SubInstance
	public static final ServerConfig server = new ServerConfig();

	@Config.Comment("Mixins based Client Tweaks")
	@Config.Name("Toggle Client Mixins")
	@MixinConfig.SubInstance
	public static final ClientFeaturesConfig clientFeaturesMixinConfig = new ClientFeaturesConfig();

	@Config.Comment("Mixins based Tweaks with highly configurable features")
	@Config.Name("Toggle Major Features Mixins")
	@MixinConfig.SubInstance
	public static final MajorFeaturesConfig majorFeaturesConfig = new MajorFeaturesConfig();

	@Config.Comment("Mixins based Tweaks with very basic options")
	@Config.Name("Toggle Minor Features Mixins")
	@MixinConfig.SubInstance
	public static final MinorFeaturesConfig minorFeaturesConfig = new MinorFeaturesConfig();

	@Config.Comment("Mod Compatibility\n" +
			"Toggles are enabled by default and will flag Fermium Booter errors, disable when associated mod is not installed")
	@Config.Name("Mod Compatibility")
	public static final IntegrationConfig integrationConfig = new IntegrationConfig();

	@Config.Comment("Enable/Disable Patches for Lycanites Mobs")
	@Config.Name("Toggle Patches")
	@MixinConfig.SubInstance
	public static final PatchConfig mixinPatchesConfig = new PatchConfig();

	@Config.Comment("Dependency for various features when using default config values that use LycanitesTweaks resources\n" +
			"Makes Lycanites Mobs check and load resources from LycanitesTweaks\n" +
			"If disabled, there are no automatic replacements for resources provided by LycanitesTweaks")
	@Config.Name("Add Feature: LycanitesTweaks default JSON")
	@Config.RequiresMcRestart
	@MixinConfig.LateMixin(name = "mixins.lycanitestweaks.featureinjectdefaultjsonloading.json")
	public static boolean addLycanitesTweaksDefaultJSON = true;

	@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(LycanitesTweaks.MODID)) {
				ItemTweaksConfig.reset();
				CreatureInteractConfig.reset();
				CreatureStatsConfig.reset();
				PlayerMobLevelsConfig.reset();
				ForgeConfigProvider.reset();
				ConfigManager.sync(LycanitesTweaks.MODID, Config.Type.INSTANCE);
			}
		}
	}
}