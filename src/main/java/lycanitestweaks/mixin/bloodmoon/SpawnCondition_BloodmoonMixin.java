package lycanitestweaks.mixin.bloodmoon;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.core.spawner.condition.SpawnCondition;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.bloodmoon.spawner.condition.BloodMoonSpawnCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnCondition.class)
public abstract class SpawnCondition_BloodmoonMixin {

    @Unique
    private final static String BLOODMOON_CONDITION = LycanitesTweaks.MODID + ":" + ModLoadedUtil.BLOODMOON_MODID;

    @Inject(
            method = "createFromJSON",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z", ordinal = 0),
            remap = false

    )
    private static void lycanitesTweaks_lycanitesMobsSpawnCondition_createFromJSONBloodMoonCondition(JsonObject json, CallbackInfoReturnable<SpawnCondition> cir, @Local String type, @Local LocalRef<SpawnCondition> spawnCondition){
        if(BLOODMOON_CONDITION.equals(type))
            spawnCondition.set(new BloodMoonSpawnCondition());
    }
}
