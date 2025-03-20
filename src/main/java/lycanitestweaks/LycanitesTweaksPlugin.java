package lycanitestweaks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import fermiumbooter.FermiumRegistryAPI;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LycanitesTweaksPlugin implements IFMLLoadingPlugin {

	private static final Map<String, String> earlyMap = setupEarlyMap();
	private static final Map<String, String> lateMap = setupLateMap();

	private static Map<String, String> setupEarlyMap() {
		Map<String, String> map = new HashMap<>();

		// Features
		map.put("Fix Golems Attacking Tamed Mobs", "mixins.lycanitestweaks.vanillairongolemtargettamed.json");

		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String> setupLateMap() {
		Map<String, String> map = new HashMap<>();

		// Features
		map.put("Boss DPS Limit Recalc", "mixins.lycanitestweaks.featurebossdamagelimitdpsrecalc.json");
		map.put("Familiars Inactive On Join", "mixins.lycanitestweaks.featurefamiliarsinactiveonjoin.json");
		map.put("Level match minions goal", "mixins.lycanitestweaks.featureaiminionhostlevelmatch.json");
		map.put("Level match minions host method", "mixins.lycanitestweaks.featurebasecreatureminionhostlevelmatch.json");
		map.put("Perch Position Modifiable", "mixins.lycanitestweaks.featureperchposition.json");
		map.put("Pickup Checks Distances", "mixins.lycanitestweaks.featureentitypickupfix.json");
		map.put("Treat Sets Persistence", "mixins.lycanitestweaks.featuretameabletreatpersistence.json");
		map.put("Soul Gazer Dismounts", "mixins.lycanitestweaks.featuresoulgazerdismounts.json");
		map.put("Tamed Variant Stat Bonuses", "mixins.lycanitestweaks.featurealltamedvariantstats.json");
		map.put("Summon Progression Rework", "mixins.lycanitestweaks.featuresummonrework.json");
		map.put("Size Change Foods", "mixins.lycanitestweaks.featuretamedsizechangefood.json");
		map.put("Baby Age Potion", "mixins.lycanitestweaks.featuretamedbabypotion.json");
		map.put("Soulkeys Set Variant", "mixins.lycanitestweaks.featuresoulkeyvariantset.json");
		map.put("Crafted Equipment Sword Enchantments", "mixins.lycanitestweaks.featureequipmentswordenchantments.json");
		map.put("Crafted Equipment ReachFix (ReachFix)", "mixins.lycanitestweaks.equipmentreachfix.json");
		map.put("Crafted Equipment RLCombat Sweep (RLCombat)", "mixins.lycanitestweaks.equipmentrlcombatsweep.json");
		map.put("Crafted Equipment Offhand RMB Needs Sneak", "mixins.lycanitestweaks.equipmentrmbneedssneak.json");
		// Patches
		map.put("Disable Soul Bounds Using Portals", "mixins.lycanitestweaks.patchessoulboundnoportal.json");
		map.put("Fix AgeableCreature baby drops", "mixins.lycanitestweaks.patchesageablebabydrops.json");
		map.put("Fix BaseCreature Summon Persistence", "mixins.lycanitestweaks.patchesbasecreatureminionpersistence.json");
		map.put("Fix Ettin grief flag", "mixins.lycanitestweaks.patchesettingriefflag.json");
		map.put("Fix Serpix Blizzard Offset", "mixins.lycanitestweaks.patcheserpixblizzardoffset.json");
		// Mixins

		return Collections.unmodifiableMap(map);
	}

	public LycanitesTweaksPlugin() {
		MixinBootstrap.init();

		LycanitesTweaks.LOGGER.log(Level.INFO, "LycanitesTweaks Early Enqueue Start");
		for(Map.Entry<String, String> entry : earlyMap.entrySet()) {
			LycanitesTweaks.LOGGER.log(Level.INFO, "LycanitesTweaks Early Enqueue: " + entry.getKey());
			FermiumRegistryAPI.enqueueMixin(false, entry.getValue(), () -> ForgeConfigHandler.getBoolean(entry.getKey()));
		}

		LycanitesTweaks.LOGGER.log(Level.INFO, "LycanitesTweaks Late Enqueue Start");
		for(Map.Entry<String, String> entry : lateMap.entrySet()) {
			LycanitesTweaks.LOGGER.log(Level.INFO, "LycanitesTweaks Late Enqueue: " + entry.getKey());
			FermiumRegistryAPI.enqueueMixin(true, entry.getValue(), () -> ForgeConfigHandler.getBoolean(entry.getKey()));
		}
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}