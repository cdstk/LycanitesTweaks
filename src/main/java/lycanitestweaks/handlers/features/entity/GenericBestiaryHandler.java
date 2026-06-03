package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.util.Helpers;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GenericBestiaryHandler {

    @SubscribeEvent
    public static void onHurtKnowledgeBonus(LivingHurtEvent event){
        if(!ForgeConfigHandler.genericBestiary.maxKnowledgeBonus) return;
        if(!(event.getEntityLiving() instanceof EntityPlayer)) return;
        if(!(event.getSource().getTrueSource() instanceof EntityLiving)) return;
        if(event.getSource().isUnblockable()) return;

        EntityPlayer victim = (EntityPlayer) event.getEntityLiving();
        EntityLiving attacker = (EntityLiving) event.getSource().getTrueSource();

        if(ForgeConfigHandler.genericBestiary.maxKnowledgeBonusSoulgazer && !Helpers.hasSoulgazerEquiped(victim)) return;
        float blockedDamage = 0F;

        if(attacker instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) attacker;
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(victim);
            if(extendedPlayer != null) {
                if(extendedPlayer.getBeastiary().hasKnowledgeRank(creature.creatureInfo.getName(), 2)) {
                    blockedDamage = event.getAmount() * (creature.isBoss()
                            ? ForgeConfigHandler.genericBestiary.maxKnowledgeDefenceBoss
                            : ForgeConfigHandler.genericBestiary.maxKnowledgeDefenceNonBoss
                    );
                }
            }
        }
        else {
            ResourceLocation entityID = EntityList.getKey(attacker);
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(victim);
            if(ltp != null && entityID != null) {
                if(ltp.getBestiary().hasKnowledgeRank(entityID.toString(), 2)) {
                    blockedDamage = event.getAmount() * (attacker.isNonBoss()
                            ? ForgeConfigHandler.genericBestiary.maxKnowledgeDefenceNonBoss
                            : ForgeConfigHandler.genericBestiary.maxKnowledgeDefenceBoss
                    );
                }
            }
        }
        if(blockedDamage == 0F) return;
        event.setAmount(event.getAmount() - blockedDamage);
    }

    @SubscribeEvent
    public static void onGenericStudyKill(LivingDeathEvent event){
        if(event.isCanceled()) return;
        EntityLivingBase target = event.getEntityLiving();
        if(target == null) return;
        if(target.world.isRemote) return;

        EntityPlayer player = null;
        if(event.getSource().getTrueSource() instanceof EntityPlayer) player = (EntityPlayer) event.getSource().getTrueSource();

        if(player == null && target.getCombatTracker().getBestAttacker() instanceof EntityPlayer)
            player = (EntityPlayer) target.getCombatTracker().getBestAttacker();

        if(player != null) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if (ltp != null) {
                ltp.studyEntity(target, CreatureManager.getInstance().config.creatureKillKnowledge, false, false);
            }
        }
    }

    @SubscribeEvent
    public static void onGenericStudyBreeding(BabyEntitySpawnEvent event) {
        EntityLiving parentA = event.getParentA();
        if(parentA.world.isRemote) return;

        for(EntityPlayer player : parentA.getEntityWorld().getPlayers(EntityPlayer.class, player -> player != null && parentA.getDistance(player) <= 5 || player == event.getCausedByPlayer())) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if(ltp != null) {
                ltp.studyEntity(parentA, CreatureManager.getInstance().config.creatureBreedKnowledge, false, true);
            }
        }
    }

    @SubscribeEvent
    public static void onGenericStudyTaming(AnimalTameEvent event) {
        EntityPlayer player = event.getTamer();
        if(player.world.isRemote) return;

        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null) ltp.studyEntity(event.getAnimal(), CreatureManager.getInstance().config.creatureTreatKnowledge, false, true);
    }

    // Redundant check for avoiding unknown message and fallback to Lycanites one
    public static boolean canStudyGenericEntity(Entity target) {
        GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(target.getClass());
        if(entityInfo == null || entityInfo.disableBestiaryEntry) return false;

        if(target instanceof EntityTameable && ((EntityTameable) target).isTamed())
            return false;

        return true;
    }

    public static boolean soulgazeGenericEntity(EntityPlayer player, Entity entity) {
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null) {
            int amount = CreatureManager.getInstance().config.creatureStudyKnowledge;
            if (ltp.studyEntity(entity, amount, true, true)) {
                if(player.getEntityWorld().isRemote) {
                    for(int i = 0; i < 32; ++i) {
                        entity.getEntityWorld().spawnParticle(EnumParticleTypes.VILLAGER_HAPPY,
                                entity.getPosition().getX() + (4.0F * player.getRNG().nextFloat()) - 2.0F,
                                entity.getPosition().getY() + (4.0F * player.getRNG().nextFloat()) - 2.0F,
                                entity.getPosition().getZ() + (4.0F * player.getRNG().nextFloat()) - 2.0F,
                                0.0D, 0.0D, 0.0D);
                    }
                }
                return true;
            }
        }

        return false;
    }
}
