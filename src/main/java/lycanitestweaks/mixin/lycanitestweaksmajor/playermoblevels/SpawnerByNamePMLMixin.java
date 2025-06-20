package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import com.lycanitesmobs.core.spawner.Spawner;
import com.lycanitesmobs.core.spawner.trigger.SpawnTrigger;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
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
public abstract class SpawnerByNamePMLMixin {

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
                boolean isInList = PlayerMobLevelsConfig.getPMLSpawnerNames().contains(this.name);

                isInList = ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlSpawnerNameStringsIsBlacklist != isInList;

                if (isInList) {
                    if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SpawnerTrigger) || player.getHeldItemMainhand().getItem() instanceof ItemSoulgazer) {
                        creature.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SpawnerTrigger, creature));
                        if (ForgeConfigHandler.client.debugLoggerAutomatic)
                            LycanitesTweaks.LOGGER.log(Level.INFO, "JSON Spawning: {}", creature);
                    }
                }
            }
        }
    }
}
