package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.spawner.MobSpawn;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobSpawn.class)
public abstract class MobSpawn_SpawnedAsBossInitMixin {

    @Shadow(remap = false) public boolean spawnAsBoss;

    @Inject(
            method = "onSpawned",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/spawner/MobSpawn;mobSizeScale:D", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsMobSpawn_onSpawnedSpawnedAsBossInit(EntityLiving entityLiving, EntityPlayer player, CallbackInfo ci, @Local BaseCreatureEntity entityCreature){
        entityCreature.spawnedAsBoss = this.spawnAsBoss;
    }
}
