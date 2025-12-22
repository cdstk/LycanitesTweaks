package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.lycanitesmobs.core.spawner.MobSpawn;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MobSpawn.class)
public abstract class MobSpawn_OverrideBossLevelMixin {

    @Shadow(remap = false)
    public boolean dungeonBoss;
    @Shadow(remap = false)
    public int dungeonLevelMin;
    @Shadow(remap = false)
    public int dungeonLevelMax;

    @ModifyArg(
            method = "onSpawned",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;addLevel(I)V"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsMobSpawn_onSpawnedOverrideLevel(int level){
        if(dungeonBoss){
            int bossLevel = Math.max(this.dungeonLevelMin, this.dungeonLevelMax) * ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.dungeonBossLevelPerFloor;
            LycanitesTweaks.LOGGER.log(Level.INFO, "Overriding Dungeon Boss Config Level: {} -> {}. You should not be lazy, go read \"Override Dungeon Boss Config Level\".", level, bossLevel);
            return bossLevel;
        }
        return level;
    }
}
