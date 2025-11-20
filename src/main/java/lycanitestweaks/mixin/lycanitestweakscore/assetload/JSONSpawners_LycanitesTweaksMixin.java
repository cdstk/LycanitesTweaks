package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.Utilities;
import com.lycanitesmobs.core.JSONLoader;
import com.lycanitesmobs.core.spawner.SpawnerManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@Mixin(SpawnerManager.class)
public abstract class JSONSpawners_LycanitesTweaksMixin extends JSONLoader {

    @Inject(
            method = "loadAllFromJSON",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/spawner/SpawnerManager;loadJsonObjects(Lcom/google/gson/Gson;Ljava/nio/file/Path;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;)V", ordinal = 3),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsDungeonManager_loadJsonObjects(CallbackInfo ci, @Local Gson gson, @Local(ordinal = 1) Map<String, JsonObject> defaultSpawnerJSONs, @Local(ordinal = 2) Map<String, JsonObject> defaultMobEventsJSONs){
        Set<String> customAssetPath = ForgeConfigProvider.getAssetPathSetFor("spawners");
        if(customAssetPath != null){
            customAssetPath.forEach((customPath) -> {
                Path path = Utilities.getAssetPath(LycanitesTweaks.class, LycanitesTweaks.MODID, customPath);
                this.loadJsonObjects(gson, path, defaultSpawnerJSONs, "name", "spawner");
            });
        }
        customAssetPath = ForgeConfigProvider.getAssetPathSetFor("mobevents_spawners");
        if(customAssetPath != null){
            customAssetPath.forEach((customPath) -> {
                Path path = Utilities.getAssetPath(LycanitesTweaks.class, LycanitesTweaks.MODID, customPath);
                this.loadJsonObjects(gson, path, defaultMobEventsJSONs, "name", "spawner");
            });
        }
    }
}
