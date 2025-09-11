package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.SpawnTrigger;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
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
public abstract class Spawner_ByNamePMLMixin {

    @Shadow(remap = false)
    public String name;

    @Inject(
            method = "doSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;doSpecialSpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFF)Z"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSpawner_doSpawnPML(World world, EntityPlayer player, SpawnTrigger spawnTrigger, BlockPos triggerPos, int level, int chain, CallbackInfoReturnable<Boolean> cir, @Local EntityLiving entityLiving){
        if(entityLiving instanceof BaseCreatureEntity && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.SpawnerTrigger)){
            BaseCreatureEntity creature = (BaseCreatureEntity)entityLiving;

            if (creature.firstSpawn && ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameFirstSpawn) creature.onFirstSpawn();
            if (player == null) player = world.getClosestPlayerToEntity(creature, 128);

            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
            if (pml != null) {
                boolean isInList = creature.spawnEventType.isEmpty()
                        ? PlayerMobLevelsConfig.getPMLSpawnerNames().contains(this.name) != ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameStringsIsBlacklist
                        : ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameMobEvents;

                if (isInList) {
                    if (!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SpawnerTrigger) || Helpers.hasSoulgazerEquiped(player)) {
                        creature.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SpawnerTrigger, creature));
                        if (ForgeConfigHandler.client.debugLoggerAutomatic)
                            LycanitesTweaks.LOGGER.log(Level.INFO, "JSON Spawning: {}", creature);
                    }
                }

                // Mob Event + Natural Stack Behavior
                if(!creature.spawnEventType.isEmpty() && !ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameFirstSpawn) {
                    if (!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SpawnerNatural) || Helpers.hasSoulgazerEquiped(player)) {
                        creature.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SpawnerNatural, creature));
                        if (ForgeConfigHandler.client.debugLoggerAutomatic)
                            LycanitesTweaks.LOGGER.log(Level.INFO, "JSON Mob Event Spawning: {}", creature);
                    }
                }
            }
        }
    }
}
