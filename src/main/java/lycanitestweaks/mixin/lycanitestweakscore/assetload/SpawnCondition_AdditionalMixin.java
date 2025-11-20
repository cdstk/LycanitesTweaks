package lycanitestweaks.mixin.lycanitestweakscore.assetload;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.core.spawner.condition.SpawnCondition;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.spawner.condition.ExtendedPlayerSpawnCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnCondition.class)
public abstract class SpawnCondition_AdditionalMixin {

    @Unique
    private final static String EXTENDED_PLAYER_CONDITION = LycanitesTweaks.MODID + ":player";

    @Inject(
            method = "createFromJSON",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z", ordinal = 0),
            remap = false

    )
    private static void lycanitesTweaks_lycanitesMobsSpawnCondition_createFromJSONExtendedPlayerCondition(JsonObject json, CallbackInfoReturnable<SpawnCondition> cir, @Local String type, @Local LocalRef<SpawnCondition> spawnCondition){
        if(EXTENDED_PLAYER_CONDITION.equals(type))
            spawnCondition.set(new ExtendedPlayerSpawnCondition());
    }
}
