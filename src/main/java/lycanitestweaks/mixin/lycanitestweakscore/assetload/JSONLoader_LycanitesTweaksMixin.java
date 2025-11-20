package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.Utilities;
import com.lycanitesmobs.core.JSONLoader;
import com.lycanitesmobs.core.info.ModInfo;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(JSONLoader.class)
public abstract class JSONLoader_LycanitesTweaksMixin {

    // Used to prepend an "!" before every addon file name
    // Literally what I used in the PR
    @Unique
    private static final HashSet<String> lycanitesTweaks$ADDON_PREFIX_JSONS = new HashSet<>();

    @Shadow(remap = false)
    public abstract void loadJsonObjects(Gson gson, Path path, Map<String, JsonObject> jsonObjectMap, String mapKey, String jsonType);

    @Inject(
            method = "loadAllJson",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/JSONLoader;loadJsonObjects(Lcom/google/gson/Gson;Ljava/nio/file/Path;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;)V", ordinal = 1),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsJSONLoader_loadJsonObjectsCustomDefault(ModInfo groupInfo, String name, String assetPath, String mapKey, boolean loadCustom, CallbackInfo ci, @Local Gson gson, @Local(ordinal = 1) Map<String, JsonObject> defaultJsons){
        Set<String> customAssetPath = ForgeConfigProvider.getAssetPathSetFor(assetPath);
        if(customAssetPath != null){
            customAssetPath.forEach((customPath) -> {
                Path path = Utilities.getAssetPath(LycanitesTweaks.class, LycanitesTweaks.MODID, customPath);
                this.loadJsonObjects(gson, path, defaultJsons, mapKey, null);
            });
        }
    }

    @Inject(
            method = "loadJsonObjects",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsJSONLoader_loadJsonObjectsAddonFlagging(Gson gson, Path path, Map<String, JsonObject> jsonObjectMap, String mapKey, String jsonType, CallbackInfo ci, @Local(ordinal = 1) Path filePath, @Local JsonObject json){
        boolean isAddon = true;
        // I recall the Lycanites Mobs path is sometimes prepended with a "."
        for (Path subPath : filePath) {
            if (subPath.startsWith(LycanitesMobs.modid)) {
                isAddon = false;
                break;
            }
            else if(subPath.startsWith(LycanitesTweaks.MODID)) {
                break;
            }
        }

        // Only prepend if custom addition, keep replacement file names the same
        if(isAddon && !jsonObjectMap.containsKey(json.get(mapKey).getAsString())) {
            lycanitesTweaks$ADDON_PREFIX_JSONS.add(json.get(mapKey).getAsString());
        }
    }

    @Inject(
            method = "saveJsonObject",
            at = @At("HEAD"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsJSONLoader_saveJsonObjectAddonFlagging(Gson gson, JsonObject jsonObject, String name, String assetPath, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalRef<String> fileName){
        if(lycanitesTweaks$ADDON_PREFIX_JSONS.contains(name)) fileName.set("!" + name);
    }
}
