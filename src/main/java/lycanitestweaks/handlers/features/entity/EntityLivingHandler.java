package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.ExtendedWorld;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.FearEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.entity.item.EntityEncounterSummonCrystal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Set;

public class EntityLivingHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFearTargeted(LivingSetAttackTargetEvent event) {
        if(!ForgeConfigHandler.server.fearAttackTargetEvent) return;
        if(event.getTarget() instanceof FearEntity && event.getEntityLiving() instanceof EntityLiving){
            ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        if(!ForgeConfigHandler.server.chargeExpConfig.vanillaKillExperience) return;
        if (event.isCanceled()
                || event.getEntityLiving() == null
                || event.getEntityLiving().getEntityWorld().isRemote) {
            return;
        }

        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(event.getAttackingPlayer());
        if(extendedPlayer == null) return;
        if(ForgeConfigHandler.server.chargeExpConfig.killXPSoulgazer && !Helpers.hasSoulgazerEquiped(extendedPlayer.getPlayer())) return;

        int totalXP = ForgeConfigHandler.server.chargeExpConfig.killXPBonus ? event.getDroppedExperience() : event.getOriginalExperience();
        totalXP *= (int) ForgeConfigHandler.server.chargeExpConfig.killXPModifier;
        if(totalXP <= 0) return;

        EntityLivingBase target = event.getEntityLiving();
        World world = target.getEntityWorld();
        Set<BaseCreatureEntity> pets = new HashSet<>();

        pets.addAll(world.getEntitiesWithinAABB(
                TameableCreatureEntity.class,
                target.getEntityBoundingBox().grow(ForgeConfigHandler.server.chargeExpConfig.killXPRange),
                creature -> creature.isTamed() && creature.getPlayerOwner() == extendedPlayer.getPlayer())
        );

        pets.addAll(world.getEntitiesWithinAABB(
                TameableCreatureEntity.class,
                extendedPlayer.getPlayer().getEntityBoundingBox().grow(ForgeConfigHandler.server.chargeExpConfig.killXPRange),
                creature -> creature.isTamed() && creature.getPlayerOwner() == extendedPlayer.getPlayer())
        );

        // Summons and Soulbounds are both minions
        pets.removeIf(creature -> creature.isTemporary || creature.isPetType("familiar"));
        if(!ForgeConfigHandler.server.chargeExpConfig.killXPSoulbound) pets.removeIf(BaseCreatureEntity::isBoundPet);
        if(pets.isEmpty()) return;

        int tameCount = pets.size();
        if(ForgeConfigHandler.server.chargeExpConfig.killXPCountPlayer) tameCount++;
        int splitXP = Math.max(1, totalXP / tameCount);
        pets.forEach(pet -> {
            pet.addExperience(splitXP);
        });

    }

    @SubscribeEvent
    public static void onBlockBreak(LivingDestroyBlockEvent event) {
        if (!ForgeConfigHandler.server.blockProtectionLivingEvent) return;
        if (event.getState() == null
                || event.isCanceled()
                || event.getEntityLiving() == null
                || event.getEntityLiving().getEntityWorld().isRemote) {
            return;
        }

        ExtendedWorld extendedWorld = ExtendedWorld.getForWorld(event.getEntity().getEntityWorld());
        if (extendedWorld.isBossNearby(new Vec3d(event.getPos()))) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            if (ForgeConfigHandler.debug.debugLoggerTick)
                LycanitesTweaks.LOGGER.log(Level.INFO, "Boss prevented block at {}, from being broke by {}", event.getPos(), event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void onCreatureSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
        if(event.isCanceled()) return;
        if(event.getWorld().isRemote) return;
        if(!(event.getEntityLiving() instanceof BaseCreatureEntity)) return;
        BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntityLiving();

        // Random SpawnedAsBoss
        if(event.getWorld().rand.nextFloat() < ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalSpawnChance && event.getSpawner() == null) {
            byte minLight = ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalMinimumLight;
            if(minLight == -1 || minLight >= creature.testLightLevel()) {
                if (!creature.isBossAlways() && !creature.isTamed() && !creature.isMinion() && !CreatureManager.getInstance().creatureGroups.get("animal").hasEntity(creature)) {
                    creature.onFirstSpawn();
                    if (ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalSpawnCrystal) {
                        if (EntityEncounterSummonCrystal.trySpawnEncounterCrystal(event.getWorld(), creature)) {
                            creature.setDead(); // Remove Original Entity, not preferred but catches all check spawn results
                            event.setCanceled(true); // Despite the docs, this does not cancel the spawn
                            return;
                        }
                    } else {
                        creature.spawnedAsBoss = true;
                        creature.damageLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
                        creature.damageMax = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
                        creature.refreshAttributes();
                    }
                }
            }
        }

        // Player Mob Levels
        PlayerMobLevelsConfig.BonusCategory category = null;
        if(event.getSpawner() == null && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.SpawnerNatural)){
            category = PlayerMobLevelsConfig.BonusCategory.SpawnerNatural;
        }
        else if(event.getSpawner() != null && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.SpawnerTile)){
            category = PlayerMobLevelsConfig.BonusCategory.SpawnerTile;
        }
        if(category == null) return;

        EntityPlayer player = event.getWorld().getClosestPlayerToEntity(event.getEntityLiving(), 128);
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);

        if(pml == null || !creature.firstSpawn) return;
        if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(category) || Helpers.hasSoulgazerEquiped(player)){
            creature.onFirstSpawn();
            creature.addLevel(pml.getTotalLevelsForCategory(category, creature));
            if(ForgeConfigHandler.debug.debugLoggerTick) LycanitesTweaks.LOGGER.log(Level.INFO, "{} Spawning: {}", category.name(), creature);
        }
    }
}