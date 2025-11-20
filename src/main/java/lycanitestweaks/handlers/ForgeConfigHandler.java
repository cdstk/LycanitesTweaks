package lycanitestweaks.handlers;

import com.lycanitesmobs.core.entity.CreatureStats;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.config.ClientConfig;
import lycanitestweaks.handlers.config.ClientFeaturesConfig;
import lycanitestweaks.handlers.config.IntegrationConfig;
import lycanitestweaks.handlers.config.MajorFeaturesConfig;
import lycanitestweaks.handlers.config.MinorFeaturesConfig;
import lycanitestweaks.handlers.config.PatchConfig;
import lycanitestweaks.handlers.config.ServerConfig;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LycanitesTweaks.MODID)
@MixinConfig(name = LycanitesTweaks.MODID)
public class ForgeConfigHandler {

//	@Config.Comment("Modify the sorting behavior of only LycanitesTweaks' Config.\n" +
//			"Makes the order be the read-in order instead of sorting alphabetically.\n" +
//			"Configs are organized with dependencies at the top and modifiers right under.\n" +
//			"Forge can not perfectly fix partial or update old configs and will append new config entries.\n" +
//			"This will not work with Dynamic Surroundings installed.")
//	@Config.Name("Modify LycanitesTweaks Config Order")
//	@Config.RequiresMcRestart
//	@MixinConfig.CompatHandling(modid = ModLoadedUtil.DYNAMICSURROUNDINGS_MODID, desired = false, reason = "Known conflict, early loads mixin target in pre-init")
//	@MixinConfig.MixinToggle(defaultValue = true, earlyMixin = "mixins.lycanitestweaks.forgeconfigsort.json")
//	public static boolean writeForgeConfigUnsorted = !FermiumRegistryAPI.isModPresent(ModLoadedUtil.DYNAMICSURROUNDINGS_MODID);

	/*
	 * Projectile "behaviours"
	 * "lycanitestweaks:advancedFireProjectiles" - lycanitestweaks/info/projectile/behaviours/ProjectileBehaviourAdvancedFireProjectiles.java
	 * "lycanitestweaks:targetedForce" - lycanitestweaks/info/projectile/behaviours/ProjectileBehaviourTargetedForce.java
	 * "lycanitestweaks:drainEffect" - lycanitestweaks/info/projectile/behaviours/ProjectileBehaviorDrainEffect.java
	 *
	 * "mobSpawns" Entries
	 * "lycanitestweaks:setNBT" - Set any NBT like /entitydata or /summon commands
	 * "lycanitestweaks:doInitialSpawn" - Call onInitialSpawn on EntityLiving, useful for mods like Ice And Fire
	 */
	@Config.Comment("LycanitesTweaks has features that rely on being loaded by Lycanites Mobs.\n" +
			"Makes Lycanites Mobs check and load JSON resources from LycanitesTweaks.\n" +
			"JSONs modifications include default config rebalancing and custom additions.\n" +
			"LycanitesTweaks custom additions are prefixed with a '!' in their file names.\n" +
			"Provides additional ProjectileBehaviors and JSON options used by custom additions.")
	@Config.Name("LycanitesTweaks Default JSON")
	@Config.RequiresMcRestart
	@MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.feature.injectdefaultjsonloading.json")
	public static boolean addLycanitesTweaksDefaultJSON = true;

	@Config.Comment("Client-Side Options")
	@Config.Name("Client Options")
	public static final ClientConfig client = new ClientConfig();

	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

	@Config.Comment("Mixins based Client Tweaks")
	@Config.Name("Client Mixins")
	public static final ClientFeaturesConfig clientFeaturesMixinConfig = new ClientFeaturesConfig();

	@Config.Comment("Mixins based Tweaks with highly configurable features")
	@Config.Name("Major Features Mixins")
	public static final MajorFeaturesConfig majorFeaturesConfig = new MajorFeaturesConfig();

	@Config.Comment("Mixins based Tweaks with very basic options")
	@Config.Name("Minor Features Mixins")
	public static final MinorFeaturesConfig minorFeaturesConfig = new MinorFeaturesConfig();

	@Config.Comment("Mod Compatibility\n" +
			"On first load toggle configs are set based on if the required mods are installed.")
	@Config.Name("Mod Compatibility")
	public static final IntegrationConfig integrationConfig = new IntegrationConfig();

	@Config.Comment("Enable/Disable Patches for Lycanites Mobs")
	@Config.Name("Toggle Patches")
	public static final PatchConfig mixinPatchesConfig = new PatchConfig();

	@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(LycanitesTweaks.MODID)) {
				PlayerMobLevelsConfig.reset();
				ForgeConfigProvider.reset();
				ConfigManager.sync(LycanitesTweaks.MODID, Config.Type.INSTANCE);
				if(ForgeConfigHandler.server.chargeExpConfig.modifiedExperienceCalc){
					ItemEquipmentPart.BASE_LEVELUP_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.baseExperienceEquipment;
					CreatureStats.BASE_LEVELUP_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.baseExperiencePets;
					ChargeItem.CHARGE_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.chargeExperienceValue;
				}
			}
		}
	}
}