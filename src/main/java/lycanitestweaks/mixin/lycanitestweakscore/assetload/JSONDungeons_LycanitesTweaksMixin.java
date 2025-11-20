package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.Utilities;
import com.lycanitesmobs.core.JSONLoader;
import com.lycanitesmobs.core.dungeon.DungeonManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

@Mixin(DungeonManager.class)
public abstract class JSONDungeons_LycanitesTweaksMixin extends JSONLoader {

    @Inject(
            method = "loadDungeonsFromJSON",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/dungeon/DungeonManager;loadJsonObjects(Lcom/google/gson/Gson;Ljava/nio/file/Path;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;)V", ordinal = 1),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsDungeonManager_loadJsonObjects(String type, CallbackInfoReturnable<Map<String, JsonObject>> cir, @Local Gson gson, @Local(ordinal = 1) Map<String, JsonObject> defaultJsons){
        Set<String> customAssetPath = ForgeConfigProvider.getAssetPathSetFor("dungeons/" + type);
        if(customAssetPath != null){
            customAssetPath.forEach((customPath) -> {
                Path path = Utilities.getAssetPath(LycanitesTweaks.class, LycanitesTweaks.MODID, customPath);
                this.loadJsonObjects(gson, path, defaultJsons, "name", null);
            });
        }
    }
}
