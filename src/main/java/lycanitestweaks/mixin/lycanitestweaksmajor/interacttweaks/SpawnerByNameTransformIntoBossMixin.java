package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.SpawnTrigger;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.IBaseCreatureEntityTransformIntoBossMixin;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spawner.class)
public abstract class SpawnerByNameTransformIntoBossMixin {

    @Shadow(remap = false)
    public String name;

    @Inject(
            method = "doSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;doSpecialSpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFF)Z"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSpawner_doSpawnTransformIntoBoss(World world, EntityPlayer player, SpawnTrigger spawnTrigger, BlockPos triggerPos, int level, int chain, CallbackInfoReturnable<Boolean> cir, @Local EntityLiving entityLiving){
        if(entityLiving instanceof IBaseCreatureEntityTransformIntoBossMixin){

            boolean isInList = ForgeConfigProvider.getCanTransformIntoBossSpawnerNames().contains(this.name);

            isInList = ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.transformBossSpawnerNameStringsIsBlacklist != isInList;

            if (isInList) {
                ((IBaseCreatureEntityTransformIntoBossMixin) entityLiving).lycanitesTweaks$setCanTransformIntoBoss(true);
                if (ForgeConfigHandler.debug.debugLoggerAutomatic)
                    LycanitesTweaks.LOGGER.log(Level.INFO, "Flagging Boss Transformation: {}", entityLiving);
            }
        }
    }
}
