package lycanitestweaks.mixin.lycanitestweaksminor.spawning;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureSpawn;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreatureSpawn.class)
public abstract class CreatureSpawn_WaterPlacementMixin {

    @Unique
    private boolean lycanitesTweaks$waterMonsterPlacement = false;
    @Unique
    private boolean lycanitesTweaks$waterVanillaPlacement = false;

    @Shadow(remap = false)
    public List<EnumCreatureType> vanillaSpawnerTypes;

    @Inject(
            method = "loadFromJSON",
            at = @At(value = "CONSTANT", args = "stringValue=monster"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsCreatureSpawn_loadFromJSONWaterSpawning(CreatureInfo creatureInfo, JsonObject json, CallbackInfo ci, @Local String spawner){
        if ("watermonster".equalsIgnoreCase(spawner)) {
            this.vanillaSpawnerTypes.add(EnumCreatureType.MONSTER);
            this.lycanitesTweaks$waterMonsterPlacement = true;
        }
        else if ("water".equalsIgnoreCase(spawner) && ForgeConfigHandler.minorFeaturesConfig.waterMonsterSpawningAuto) {
            this.vanillaSpawnerTypes.add(EnumCreatureType.MONSTER);
            this.lycanitesTweaks$waterMonsterPlacement = true;
        }
        else if ("waterplacementreduced".equalsIgnoreCase(spawner)) {
            this.lycanitesTweaks$waterMonsterPlacement = true;
        }
        else if ("waterplacement".equalsIgnoreCase(spawner)) {
            this.lycanitesTweaks$waterVanillaPlacement = true;
        }
    }

    @Inject(
            method = "registerVanillaSpawns",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/LycanitesMobs;logDebug(Ljava/lang/String;Ljava/lang/String;)V", ordinal = 2),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsCreatureSpawn_registerVanillaSpawnsWaterSpawning(CreatureInfo creatureInfo, CallbackInfo ci){
        if(this.lycanitesTweaks$waterMonsterPlacement)
            EntitySpawnPlacementRegistry.setPlacementType(creatureInfo.entityClass, LycanitesEntityUtil.IN_WATER_REDUCED);
        else if(this.lycanitesTweaks$waterVanillaPlacement)
            EntitySpawnPlacementRegistry.setPlacementType(creatureInfo.entityClass, EntityLiving.SpawnPlacementType.IN_WATER);
    }
}
