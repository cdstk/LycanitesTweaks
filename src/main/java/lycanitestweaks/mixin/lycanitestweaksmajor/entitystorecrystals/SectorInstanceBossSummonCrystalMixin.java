package lycanitestweaks.mixin.lycanitestweaksmajor.entitystorecrystals;

import com.lycanitesmobs.core.dungeon.instance.SectorInstance;
import com.lycanitesmobs.core.spawner.MobSpawn;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(SectorInstance.class)
public abstract class SectorInstanceBossSummonCrystalMixin {

    @Shadow(remap = false)
    public abstract Vec3i getRoomSize();

    @Redirect(
            method = "build",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/dungeon/instance/SectorInstance;spawnMob(Lnet/minecraft/world/World;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;Lcom/lycanitesmobs/core/spawner/MobSpawn;Ljava/util/Random;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSectorInstance_buildSpawnCrystal(SectorInstance instance, World world, ChunkPos chunkPos, BlockPos blockPos, MobSpawn mobSpawn, Random random){
        // Restrict To Chunk Position:
        int chunkOffset = 8;
        if(blockPos.getX() < chunkPos.getXStart() + chunkOffset || blockPos.getX() > chunkPos.getXEnd() + chunkOffset) {
            return;
        }
        if(blockPos.getY() <= 0 || blockPos.getY() >= world.getHeight()) {
            return;
        }
        if(blockPos.getZ() < chunkPos.getZStart() + chunkOffset || blockPos.getZ() > chunkPos.getZEnd() + chunkOffset) {
            return;
        }

        // Spawn Mob:
        EntityLiving entityLiving = mobSpawn.createEntity(world);
        entityLiving.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        mobSpawn.onSpawned(entityLiving, null);

        world.spawnEntity(EntityBossSummonCrystal.storeDungeonBoss(world, entityLiving, blockPos, this.getRoomSize()));
    }
}
