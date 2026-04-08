package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.JSONLoader;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JSONLoader.class)
public abstract class JSONLoader_LoadDefaultOverrideMixin {

    @ModifyExpressionValue(
            method = "writeDefaultJSONObjects",
            at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonElement;getAsBoolean()Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsJSONLoader_writeDefaultJSONObjectsOverrideLoadDefault(boolean original){
        switch (ForgeConfigHandler.loadDefaultOverride){
            case ALL_TRUE:
                return true;
            case ALL_FALSE:
                return false;
        }
        return original;
    }
}
