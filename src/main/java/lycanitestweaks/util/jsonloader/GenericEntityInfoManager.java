package lycanitestweaks.util.jsonloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.EBWizardryHandler;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.SRPHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.BeastiaryModInfo;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GenericEntityInfoManager extends ModableJSONLoader {

	// Group Key used by the JSON reader
	private static final String MOD_INFO_KEY = "Mod Info";
	private static final String ENTITY_INFO_KEY = "Generic Entity Info";
	// File directory within default directory config to read/write
	private static final String MOD_INFO_PATH = "!lycanitestweaks_ModInfos";
	private static final String ENTITY_INFO_PATH = "!lycanitestweaks_GenericBestiaryEntities";

	public static GenericEntityInfoManager INSTANCE;
	/** Mod config directory where JSONs are written to and will read custom JSONs from **/
	private ModInfo writeDirectory = LycanitesMobs.modInfo;

	/** A list of mods that have loaded with this Entity Manager, these will read custom JSONs. **/
	public Set<ModInfo> readDirectories = new HashSet<>();

	/** A map of all mod entries by name. **/
	public Map<String, BeastiaryModInfo> modInfos = new HashMap<>();

	/** A map of all entities by name. **/
	public Map<String, GenericEntityInfo> entities = new HashMap<>();

	/** A map of all entities by class. **/
	public Map<Class<?>, GenericEntityInfo> entityClassMap = new HashMap<>();


	/** Returns the main Entity Manager instance or creates it and returns it. **/
	public static GenericEntityInfoManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new GenericEntityInfoManager(LycanitesMobs.modInfo);
		}
		return INSTANCE;
	}

	public static GenericEntityInfoManager getInstance(ModInfo writeDirectory) {
		getInstance().writeDirectory = writeDirectory;
		return getInstance();
	}


	/**
	 * Constructor
	 */
	public GenericEntityInfoManager(ModInfo writeDirectory) {
		this.writeDirectory = writeDirectory;
	}

	@Override
	public ModInfo getDefaultDirectory() {
		return LycanitesTweaks.MOD_INFO;
	}

	@Override
	public ModInfo getWriteDirectory() {
		return this.writeDirectory;
	}

	/**
	 * Called during startup and initially loads everything in this manager.
	 * @param readDirectory The mod loading this manager.
	 */
	public void preInit(ModInfo readDirectory) {
		if(!ForgeConfigHandler.genericBestiary.enable) return;
		this.readDirectories.add(readDirectory);

		// Load From JSON:
		this.loadBestiaryModInfosFromJSON(readDirectory);
		this.loadGenericEntitiesFromJSON(readDirectory);
	}

	/**
	 * Called during post init, loads vanilla spawns.
	 * @param readDirectory The mod loading this manager.
	 */
	public void postInit(ModInfo readDirectory) {
		if(!ForgeConfigHandler.genericBestiary.enable) return;
		this.loadForgeRegistryData();

		// Initialise:
		for(BeastiaryModInfo creatureType : this.modInfos.values()) {
			creatureType.postInit();
		}
		for(GenericEntityInfo creatureInfo : this.entities.values()) {
			creatureInfo.postInit();
		}
	}

	public void loadForgeRegistryData() {
		ForgeRegistries.ENTITIES.getValuesCollection().forEach(entityEntry -> {
			if(EntityLiving.class.isAssignableFrom(entityEntry.getEntityClass())) {
				ResourceLocation resourceLocation = entityEntry.getRegistryName();
				if (resourceLocation != null && !(BaseCreatureEntity.class.isAssignableFrom(entityEntry.getEntityClass()))) {
					BeastiaryModInfo beastiaryModInfo = this.modInfos.computeIfAbsent(resourceLocation.getNamespace(), modID -> {
						BeastiaryModInfo initModInfo = new BeastiaryModInfo(modID);
						this.writeBestiaryModInfoJSON(initModInfo);
						return initModInfo;
					});

					GenericEntityInfo genericEntityInfo = this.entities.computeIfAbsent(resourceLocation.toString(), entityID -> {
						GenericEntityInfo initEntityInfo = new GenericEntityInfo(entityID, entityEntry.getName());
						initEntityInfo.entityClass = (Class<? extends EntityLiving>) entityEntry.getEntityClass();

						this.modifyDefaultEntityInfo(initEntityInfo);

						this.writeGenericEntityJSON(initEntityInfo);
						return initEntityInfo;
					});

					this.entityClassMap.computeIfAbsent(entityEntry.getEntityClass(), clazz -> genericEntityInfo);
					genericEntityInfo.entityClass = (Class<? extends EntityLiving>) entityEntry.getEntityClass();

					beastiaryModInfo.addEntity(genericEntityInfo);
					genericEntityInfo.addModInfo(beastiaryModInfo);
				}
			}
		});
		AbstractEntityModification.clearRunTimeMap();
	}

	private void modifyDefaultEntityInfo(GenericEntityInfo entityInfo) {
		if(!ForgeConfigHandler.genericBestiary.disableModdedSpecific) return;

		if(ModLoadedUtil.ebWizardry.isLoaded()) EBWizardryHandler.modifyGenericEntityJSON(entityInfo);
		if(ModLoadedUtil.iceandfire.isLoaded()) IceAndFireHandler.modifyGenericEntityJSON(entityInfo);
		if(ModLoadedUtil.srp.isLoaded()) SRPHandler.modifyGenericEntityJSON(entityInfo);
	}

	/** Generates a new JSON Mod Info dynamically at runtime. **/
	public void writeBestiaryModInfoJSON(BeastiaryModInfo beastiaryModInfo) {
		try {
			Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
			JsonObject modInfoJson = new JsonObject();
			beastiaryModInfo.generateDefaultJSON(modInfoJson);
			this.saveJsonObject(gson, modInfoJson, beastiaryModInfo.getID(), MOD_INFO_PATH);

			if(ForgeConfigHandler.debug.debugLoggerAutomatic)
				LycanitesTweaks.LOGGER.log(Level.DEBUG, "Successfully generated Beastiary Mod Info JSON for: {}", beastiaryModInfo.getID());
		}
		catch(Exception e) {
			LycanitesTweaks.LOGGER.log(Level.WARN, "Failed to generate Beastiary Mod Info JSON for: {}", beastiaryModInfo.getID());
		}
	}


	/** Loads all JSON Mod Infos. Should be done before entities are loaded so that they can find their mod on load. **/
	public void loadBestiaryModInfosFromJSON(ModInfo readDirectory) {
		try {
			this.loadAllJson(readDirectory, MOD_INFO_KEY, MOD_INFO_PATH, BeastiaryModInfo.MOD_ID_JSON, true);
			if(ForgeConfigHandler.debug.debugLoggerAutomatic)
				LycanitesTweaks.LOGGER.log(Level.DEBUG, "Complete! {} JSON Mod Infos Loaded In Total.", this.modInfos.size());
		}
		catch(Exception e) {
			LycanitesTweaks.LOGGER.log(Level.WARN, "No Mod Infos loaded for: {}", readDirectory.name);
		}
	}

	/** Generates a new JSON Entity Info dynamically at runtime. **/
	public void writeGenericEntityJSON(GenericEntityInfo genericEntityInfo) {
		try {
			Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
			JsonObject entityInfoJson = new JsonObject();
			AbstractEntityModification.assignRuntimeDefaults(genericEntityInfo);
			genericEntityInfo.generateDefaultJSON(entityInfoJson);
			this.saveJsonObject(gson, entityInfoJson, genericEntityInfo.getEntityId(), ENTITY_INFO_PATH);

			if(ForgeConfigHandler.debug.debugLoggerAutomatic)
				LycanitesTweaks.LOGGER.log(Level.DEBUG, "Successfully generated Generic Entity Info JSON for: {}", genericEntityInfo.getEntityId());
		}
		catch(Exception e) {
			LycanitesTweaks.LOGGER.log(Level.WARN, "Failed to generate Generic Entity Info JSON for: {}", genericEntityInfo.getEntityId());
		}
	}


	/** Loads all JSON Entity Infos. Should only initially be done on pre-init and before Mod Info is loaded and can then be done in game on reload. **/
	public void loadGenericEntitiesFromJSON(ModInfo readDirectory) {
		try {
			this.loadAllJson(readDirectory, ENTITY_INFO_KEY, ENTITY_INFO_PATH, GenericEntityInfo.ENTITY_ID_JSON, true);
			if(ForgeConfigHandler.debug.debugLoggerAutomatic)
				LycanitesTweaks.LOGGER.log(Level.DEBUG, "Complete! {} JSON Generic Entities Loaded In Total.", this.entities.size());
		}
		catch(Exception e) {
			LycanitesTweaks.LOGGER.log(Level.WARN, "No Generic Entities loaded for: {}", readDirectory.name);
		}
	}

	private boolean shouldLoadDynamicDefault(ModInfo readDirectory, JsonObject json, boolean hasDefaultFile) {
		if(!hasDefaultFile && readDirectory != this.getDefaultDirectory()) {
			switch (ForgeConfigHandler.loadDefaultOverride){
				case ALL_TRUE:
					return true;
				case ALL_FALSE:
					return false;
				default:
					return !json.has(LOAD_DEFAULT) || json.get(LOAD_DEFAULT).getAsBoolean();
			}
		}
		return false;
	}

	@Override
	public void writeDefaultJSONObjects(Gson gson, Map<String, JsonObject> defaultJSONs, Map<String, JsonObject> customJSONs, Map<String, JsonObject> mixedJSONs, boolean custom, String assetPath) {
		// Don't write defaults whose mod isn't loaded
		Set<String> removeDefaults = new HashSet<>();
		defaultJSONs.forEach((jsonName, defaultJSON) -> {
			boolean modLoaded = true;
			String modID = "";
			if(defaultJSON.has(BeastiaryModInfo.MOD_ID_JSON)) {
				modID = defaultJSON.get(BeastiaryModInfo.MOD_ID_JSON).getAsString();
			}
			else if(defaultJSON.has(GenericEntityInfo.ENTITY_ID_JSON)) {
				modID = defaultJSON.get(GenericEntityInfo.ENTITY_ID_JSON).getAsString().split(":")[0];
			}
			if(!modID.isEmpty())
				modLoaded = Loader.isModLoaded(modID);
			if(!modLoaded)
				removeDefaults.add(jsonName);
		});
		removeDefaults.forEach(defaultJSONs::remove);
		super.writeDefaultJSONObjects(gson, defaultJSONs, customJSONs, mixedJSONs, custom, assetPath);
	}

	@Override
	public void parseJson(ModInfo readDirectory, String loadGroup, JsonObject json, boolean hasDefaultFile) {
		if (loadGroup.equals(ENTITY_INFO_KEY)) {
			if(this.shouldLoadDynamicDefault(readDirectory, json, hasDefaultFile)) {
				return;
			}
			if(!json.has(GenericEntityInfo.ENTITY_ID_JSON)) return;
			if(!json.has(GenericEntityInfo.LANG_NAME_JSON)) return;

			String entityID = json.get(GenericEntityInfo.ENTITY_ID_JSON).getAsString();
			String langName = json.get(GenericEntityInfo.LANG_NAME_JSON).getAsString();
			GenericEntityInfo entityInfo = new GenericEntityInfo(entityID, langName);
			entityInfo.generateDefaultJSON(json);
			entityInfo.loadFromJSON(json);

			if (this.entities.containsKey(entityInfo.getEntityId())) {
				entityInfo = this.entities.get(entityInfo.getEntityId());
				entityInfo.loadFromJSON(json);
			}

			this.entities.put(entityInfo.getEntityId(), entityInfo);
			this.entityClassMap.put(entityInfo.entityClass, entityInfo);
		}
		else if(loadGroup.equals(MOD_INFO_KEY)) {
			if(this.shouldLoadDynamicDefault(readDirectory, json, hasDefaultFile)) {
				return;
			}
			if(!json.has(BeastiaryModInfo.MOD_ID_JSON)) return;
			String modID = json.get(BeastiaryModInfo.MOD_ID_JSON).getAsString();

			BeastiaryModInfo modInfo = new BeastiaryModInfo(modID);
			modInfo.generateDefaultJSON(json);
			modInfo.loadFromJSON(json);
			if(modInfo.modID.equals("invalid") || modInfo.modName.equals("INVALID")) {
				LycanitesTweaks.LOGGER.log(Level.WARN, "Not present mod [{}] found in Mod Info JSONs", modID);
				return;
			}

			if(this.modInfos.containsKey(modInfo.modID)) {
				modInfo = this.modInfos.get(modInfo.modID);
				modInfo.loadFromJSON(json);
			}

			this.modInfos.put(modInfo.modID, modInfo);
		}
	}

	/**
	 * Reloads all JSON.
	 */
	public void reload() {
		this.modInfos.clear();
		this.entities.clear();
		this.entityClassMap.clear();

		if(!ForgeConfigHandler.genericBestiary.enable) return;

		this.readDirectories.forEach(modInfo -> {
			this.loadBestiaryModInfosFromJSON(modInfo);
			this.loadGenericEntitiesFromJSON(modInfo);
		});
		this.loadForgeRegistryData();

		// Initialise:
		for(BeastiaryModInfo creatureType : this.modInfos.values()) {
			creatureType.postInit();
		}
		for(GenericEntityInfo creatureInfo : this.entities.values()) {
			creatureInfo.postInit();
		}
	}


	/**
	 * Gets an entity by ID.
	 * @param entityID The ID of the entity to get.
	 * @return The Entity Info.
	 */
	public GenericEntityInfo getEntityInfo(String entityID) {
		return this.entities.get(entityID);
	}


	/**
	 * Gets a entity by class.
	 * @param entityClass The class of the entity to get.
	 * @return The Entity Info.
	 */
	public GenericEntityInfo getEntityInfo(Class<?> entityClass) {
		return this.entityClassMap.get(entityClass);
	}


	/**
	 * Gets the entity class by ID.
	 * @param entityID The ID of the entity to get.
	 * @return The Entity Class.
	 */
	public Class<? extends EntityLiving> getEntityClass(String entityID) {
		GenericEntityInfo entityInfo = this.getEntityInfo(entityID);
		return entityInfo == null ? null : entityInfo.entityClass;
	}
}
