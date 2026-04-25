package lycanitestweaks.mixin.lycanitestweaksminor.spawning;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureSpawn;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.SpawnerManager;
import com.lycanitesmobs.core.spawner.location.SpawnLocation;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreatureSpawn.class)
public abstract class CreatureSpawn_SkyPlacementMixin {

    @Unique
    private String lycanitesTweaks$addSkySpawn = "";

    @Shadow(remap = false)
    public List<EnumCreatureType> vanillaSpawnerTypes;

    @Inject(
            method = "loadFromJSON",
            at = @At(value = "CONSTANT", args = "stringValue=monster"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsCreatureSpawn_loadFromJSONSkySpawning(CreatureInfo creatureInfo, JsonObject json, CallbackInfo ci, @Local String spawner){
        if(this.lycanitesTweaks$addSkySpawn.isEmpty() && spawner.toLowerCase().contains("sky") && spawner.toLowerCase().contains("monster")) {
            this.vanillaSpawnerTypes.add(EnumCreatureType.MONSTER);
            this.lycanitesTweaks$addSkySpawn = spawner;
        }
    }

    @Inject(
            method = "registerVanillaSpawns",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/LycanitesMobs;logDebug(Ljava/lang/String;Ljava/lang/String;)V", ordinal = 2),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsCreatureSpawn_registerVanillaSpawnsSkySpawning(CreatureInfo creatureInfo, CallbackInfo ci){
        if(!this.lycanitesTweaks$addSkySpawn.isEmpty()) {
            String spawnerName = this.lycanitesTweaks$addSkySpawn;
            Spawner spawner = SpawnerManager.getInstance().spawners.get(spawnerName);
            if(spawner == null) spawner = SpawnerManager.getInstance().spawners.get(spawnerName.replaceAll("monster", ""));
            if(spawner != null) {
                int yMin = -1;
                int yMax = -1;
                for(SpawnLocation location : spawner.locations) {
                    if(yMin == -1 || location.yMin > yMin) yMin = location.yMin;
                    if(yMax == -1 || location.yMax < yMax) yMax = location.yMax;
                }
                EntitySpawnPlacementRegistry.setPlacementType(creatureInfo.entityClass,
                        LycanitesEntityUtil.getOrCreateSkyPlacement(
                                LycanitesTweaks.MODID + ":" + spawnerName,
                                spawner
                        ));
            }
            else {
                EntitySpawnPlacementRegistry.setPlacementType(creatureInfo.entityClass,
                        LycanitesEntityUtil.getOrCreateSkyPlacement(
                                LycanitesTweaks.MODID + ":" + spawnerName,
                                -1,
                                -1
                        ));
                LycanitesTweaks.LOGGER.log(Level.INFO, "{} given sky spawn placement without an associated spawner", creatureInfo.getName());
            }
        }
    }
}
