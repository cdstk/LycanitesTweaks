package lycanitestweaks.mixin.bloodmoon;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.SpawnTrigger;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.bloodmoon.spawner.trigger.BloodMoonTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnTrigger.class)
public abstract class SpawnTrigger_BloodmoonMixin {

    @Unique
    private final static String BLOODMOON_TRIGGER = LycanitesTweaks.MODID + ":" + ModLoadedUtil.BLOODMOON_MODID;

    @Inject(
            method = "createFromJSON",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equalsIgnoreCase(Ljava/lang/String;)Z", ordinal = 0),
            remap = false

    )
    private static void lycanitesTweaks_lycanitesMobsSpawnTrigger_createFromJSONBloodMoonTrigger(JsonObject json, Spawner spawner, CallbackInfoReturnable<SpawnTrigger> cir, @Local String type, @Local LocalRef<SpawnTrigger> spawnerTrigger){
        if(BLOODMOON_TRIGGER.equals(type))
            spawnerTrigger.set(new BloodMoonTrigger(spawner));
    }
}
