package lycanitestweaks.util.jsonloader;

import com.google.gson.JsonObject;

public abstract class AbstractInfo {

	// FML
	public void preInit() {}
	public void init() {}
	public void postInit() {}

	// JSON Configs
	public abstract void generateDefaultJSON(JsonObject json);
	public abstract void loadFromJSON(JsonObject json);
}
