package lycanitestweaks.util.jsonloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lycanitesmobs.Utilities;
import com.lycanitesmobs.core.JSONLoader;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class ModableJSONLoader extends JSONLoader {

	public static final String LOAD_DEFAULT = "loadDefault";

	protected boolean isClientSide = false;

	public static String getSafeFileName(String fileName) {
		return fileName.replace(':', '@');
	}

	/** The mod where default JSONs are copied from. **/
	public abstract ModInfo getDefaultDirectory();
	public abstract ModInfo getWriteDirectory();
	public abstract void parseJson(ModInfo readDirectory, String loadGroup, JsonObject json, boolean hasDefaultFile);

	public boolean isClientSide() { return this.isClientSide; }
	public void setClientSide(boolean clientSide) { this.isClientSide = clientSide; }

	@Override
	public void loadAllJson(ModInfo readDirectory, String assetGroup, String assetPath, String jsonKeyID, boolean loadCustom) {
		boolean logDebug = ForgeConfigHandler.debug.debugLoggerAutomatic;
		if(logDebug) LycanitesTweaks.LOGGER.log(Level.INFO, "Loading JSON {} ...", assetGroup);

		Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
		Map<String, JsonObject> jsons = new HashMap<>();

		// Load Default:
		Path path = Utilities.getAssetPath(getDefaultDirectory().getClass(), getDefaultDirectory().modid, assetPath);
		Map<String, JsonObject> defaultJsons = new HashMap<>();

		this.loadJsonObjects(gson, path, defaultJsons, jsonKeyID, null);

		// Load Custom:
		String configPath = new File(".") + "/config/" + readDirectory.modid + "/";
		File customDir = new File(configPath + assetPath);
		path = customDir.toPath();
		Map<String, JsonObject> customJsons = new HashMap<>();
		this.loadJsonObjects(gson, path, customJsons, jsonKeyID, null);

		// Write Defaults:
		this.writeDefaultJSONObjects(gson, defaultJsons, customJsons, jsons, loadCustom, assetPath);

		// Parse Json:
		if (logDebug) LycanitesTweaks.LOGGER.log(Level.INFO, "Loading {} {} ...", jsons.size(), assetGroup);
		for(String jsonName : jsons.keySet()) {
			try {
				JsonObject json = jsons.get(jsonName);
				if (logDebug) LycanitesTweaks.LOGGER.log(Level.INFO, "Loading {} JSON: {}", assetGroup, json);
				this.parseJson(readDirectory, assetGroup, json, defaultJsons.containsKey(jsonName));
			}
			catch (JsonParseException e) {
				LycanitesTweaks.LOGGER.log(Level.WARN, "Parsing error loading JSON {}: {}", assetGroup, jsonName);
				e.printStackTrace();
			}
			catch(Exception e) {
				LycanitesTweaks.LOGGER.log(Level.WARN, "There was a problem loading JSON {}: {}", assetGroup, jsonName);
				e.printStackTrace();
			}
		}
	}

	// Fixes error msg when reading an empty directory
	@Override
	public void loadJsonObjects(Gson gson, Path readPath, Map<String, JsonObject> jsonObjectMap, String jsonKeyID, String jsonType) {
		if(readPath == null || !Files.exists(readPath)) {
			return;
		}
		super.loadJsonObjects(gson, readPath, jsonObjectMap, jsonKeyID, jsonType);
	}

	@Override
	public void parseJson(ModInfo readDirectory, String loadGroup, JsonObject json) {
		this.parseJson(readDirectory, loadGroup, json, true);
	}

	@Override
	public void saveJsonObject(Gson gson, JsonObject jsonObject, String fileName, String assetPath) {
		fileName = getSafeFileName(fileName);
		if(this.getWriteDirectory() == null) super.saveJsonObject(gson, jsonObject, fileName, assetPath);
		else this.saveJsonObject(gson, jsonObject, this.getWriteDirectory(), assetPath, fileName);
	}

	public void saveJsonObject(Gson gson, JsonObject jsonObject, ModInfo writeDirectory, String assetPath, String fileName) {
		String configPath = new File(".") + "/config/" + writeDirectory.modid + "/";
		try {
			File jsonFile = new File(configPath + (!"".equals(assetPath) ? assetPath + "/" : "") + fileName + ".json");
			jsonFile.getParentFile().mkdirs();
			jsonFile.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(jsonFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.append(gson.toJson(jsonObject));
			outputStreamWriter.close();
			outputStream.close();
		}
		catch (Exception e) {
			LycanitesTweaks.LOGGER.log(Level.WARN, "Unable to save JSON into the config folder: /config/{}/{}", writeDirectory.modid, assetPath);
			e.printStackTrace();
		}
	}
}
