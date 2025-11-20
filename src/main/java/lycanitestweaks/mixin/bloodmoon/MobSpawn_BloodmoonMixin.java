package lycanitestweaks.mixin.bloodmoon;

import com.google.gson.JsonObject;
import com.lycanitesmobs.core.spawner.MobSpawn;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawn.class)
public abstract class MobSpawn_BloodmoonMixin {

    @Unique
    private final static String BLOODMOON_SPAWNED = LycanitesTweaks.MODID + ":" + ModLoadedUtil.BLOODMOON_MODID;
    @Unique
    private boolean lycanitesTweaks$bloodmoonSpawned = false;

    @Inject(
            method = "loadFromJSON",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsMobSpawn_loadFromJSONBloodmoonSpawned(JsonObject json, CallbackInfo ci){
        if (json.has(BLOODMOON_SPAWNED)) {
            this.lycanitesTweaks$bloodmoonSpawned = json.get(BLOODMOON_SPAWNED).getAsBoolean();
        }
    }

    @Inject(
            method = "onSpawned",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsMobSpawn_onSpawnedBloodmoonSpawned(EntityLiving entityLiving, EntityPlayer player, CallbackInfo ci){
        if(lycanitesTweaks$bloodmoonSpawned) entityLiving.getEntityData().setBoolean("bloodmoonSpawned", true);
    }
}
