package lycanitestweaks.info.beastiary;

import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.client.IceAndFireClientHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.jsonloader.AbstractInfo;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import lycanitestweaks.util.jsonloader.ModableJSONLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;

public class BeastiaryModInfo extends AbstractInfo {

	public static final String MOD_ID_JSON = "modID";
	public static final String MOD_NAME_JSON = "modName";
	public static final String DISABLE_ALL_ENTRIES_JSON = "disableAllBestiaryEntries";
	public static final String ENTITY_LANG_PREFIX_JSON = "entityLangKeyPrefix";
	public static final String ENTITY_LANG_SUFFIX_JSON = "entityLangKeySuffix";
	public static final String DESCRIPTION_LANG_PREFIX_JSON = "descriptionLangKeyPrefix";
	public static final String DESCRIPTION_LANG_SUFFIX_JSON = "descriptionLangKeySuffix";

	// Core Info:
	public final String modID;
	public final String modName;
	// Try matching existing vanilla convention name keys
	protected String entityLangPrefix = "entity.";
	protected String entityLangSuffix = ".name";
	// Try matching existing lore, not very common
	protected String descLangPrefix = "";
	protected String descLangSuffix = "";

	/** Disables all related Bestiary Entries **/
	public boolean disableAllBestiaryEntries = false;

	/** A map of all entities of this mod entry by entity ID. **/
	public Map<String, GenericEntityInfo> entityInfoMap = new HashMap<>();

	public BeastiaryModInfo(String modID) {
		ModContainer mod = Loader.instance().getIndexedModList().get(modID);
		if(mod != null) {
			this.modID = mod.getModId();
			this.modName = mod.getName();
		}
		// When reading existing json files
		else {
			this.modID = "invalid";
			this.modName = "INVALID";
		}
	}

    @Override
	public void postInit() {
		if(ForgeConfigHandler.debug.debugLoggerAutomatic)
			LycanitesTweaks.LOGGER.log(Level.DEBUG, "Loaded Bestiary Mod Info: {}", this.getTitle());
	}

	@Override
	public void generateDefaultJSON(JsonObject json) {
		if(!json.has(ModableJSONLoader.LOAD_DEFAULT)) json.addProperty(ModableJSONLoader.LOAD_DEFAULT, true);
		if(!json.has(MOD_ID_JSON)) json.addProperty(MOD_ID_JSON, this.modID);
		if(!json.has(MOD_NAME_JSON)) json.addProperty(MOD_NAME_JSON, this.modName);
		if(!json.has(ENTITY_LANG_PREFIX_JSON)) json.addProperty(ENTITY_LANG_PREFIX_JSON, this.entityLangPrefix);
		if(!json.has(ENTITY_LANG_SUFFIX_JSON)) json.addProperty(ENTITY_LANG_SUFFIX_JSON, this.entityLangSuffix);
		if(!json.has(DESCRIPTION_LANG_PREFIX_JSON)) json.addProperty(DESCRIPTION_LANG_PREFIX_JSON, this.descLangPrefix);
		if(!json.has(DESCRIPTION_LANG_SUFFIX_JSON)) json.addProperty(DESCRIPTION_LANG_SUFFIX_JSON, this.descLangSuffix);
		if(!json.has(DISABLE_ALL_ENTRIES_JSON)) json.addProperty(DISABLE_ALL_ENTRIES_JSON, this.disableAllBestiaryEntries);
	}

	@Override
	public void loadFromJSON(JsonObject json) {
		if(json.has(MOD_ID_JSON) && !json.get(MOD_ID_JSON).getAsString().equals(this.modID))
			LycanitesTweaks.LOGGER.log(Level.WARN, "Mod Info ID does not match JSON. ID: {}, JSON: {}", this.modID, json);
		if(json.has(MOD_NAME_JSON) && !json.get(MOD_NAME_JSON).getAsString().equals(this.modName))
			LycanitesTweaks.LOGGER.log(Level.WARN, "Mod Info Name does not match JSON. Name: {}, JSON: {}", this.modName, json);

		if(json.has(ENTITY_LANG_PREFIX_JSON))
			this.entityLangPrefix = json.get(ENTITY_LANG_PREFIX_JSON).getAsString();
		if(json.has(ENTITY_LANG_SUFFIX_JSON))
			this.entityLangSuffix = json.get(ENTITY_LANG_SUFFIX_JSON).getAsString();
		if(json.has(DESCRIPTION_LANG_PREFIX_JSON))
			this.descLangPrefix = json.get(DESCRIPTION_LANG_PREFIX_JSON).getAsString();
		if(json.has(DESCRIPTION_LANG_SUFFIX_JSON))
			this.descLangSuffix = json.get(DESCRIPTION_LANG_SUFFIX_JSON).getAsString();
		if(json.has(DISABLE_ALL_ENTRIES_JSON))
			this.disableAllBestiaryEntries = json.get(DISABLE_ALL_ENTRIES_JSON).getAsBoolean();
	}

	public String getID() {
		return this.modID;
	}

	public String getTitle() {
		return this.modName;
	}

	public String getEntityLoreKey(String entity) {
		return this.descLangPrefix + entity + this.descLangSuffix;
	}

	public String getModdedLore(GenericEntityInfo entityInfo) {
		String lore = "";
		if(GenericEntityInfoManager.getInstance().isClientSide()) {
			if (ModLoadedUtil.iceandfire.isLoaded() && this.modID.equals(ModLoadedUtil.ICEANDFIRE_MODID)) {
				lore = IceAndFireClientHandler.getModdedLore(entityInfo);
			}
		}

		return lore;
	}

	public void addEntity(GenericEntityInfo entityInfo) {
		if(entityInfo.disableBestiaryEntry) return;
		this.entityInfoMap.putIfAbsent(entityInfo.getEntityId(), entityInfo);
	}
}
