package lycanitestweaks.handlers.features.item;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.capability.entitystorecreature.EntityStoreCreatureCapabilityHandler;
import lycanitestweaks.capability.entitystorecreature.IEntityStoreCreatureCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.util.Helpers;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemSoulgazerTweaksHandler {

    @SubscribeEvent
    public static void soulgazeBlockBreak(BlockEvent.HarvestDropsEvent event){
        if(event.getHarvester() == null || event.getWorld().isRemote) return;
        EntityPlayer player = event.getHarvester();

        if(player.getHeldItemMainhand().getItem() instanceof ItemSoulgazer){
            if(event.getState().getBlock() == Blocks.CRAFTING_TABLE){
                event.getWorld().playSound(null, event.getPos(), LycanitesTweaksRegistry.SOULGAZER_CRAFTINGTABLE, SoundCategory.PLAYERS, 1F, 1F);
            }
        }
    }

    @SubscribeEvent
    public static void soulgazeAttackEntity(AttackEntityEvent event){
        if(event.isCanceled()) return;
        if(event.getEntityPlayer() == null) return;
        if(event.getTarget() == null || !(Helpers.hasSoulgazerEquiped(event.getEntityPlayer()))) return;

        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.getEntityPlayer());
        if(ltp != null){
            if(ltp.getSoulgazerAutoToggle() == 2) ItemSoulgazerTweaksHandler.soulgazeAbility(event.getEntityPlayer(), event.getTarget());
        }
    }

    @SubscribeEvent
    public static void soulgazeKillEntity(LivingDeathEvent event){
        if(event.isCanceled()) return;
        if(event.getEntityLiving() == null) return;
        EntityLivingBase target = event.getEntityLiving();

        if(event.getSource().getTrueSource() instanceof EntityPlayer && "player".equals(event.getSource().damageType)){
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if(!(Helpers.hasSoulgazerEquiped(player))) return;

            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if(ltp != null){
                if(ltp.getSoulgazerAutoToggle() == 3) {
                    ItemSoulgazerTweaksHandler.soulgazeAbility(player, target);
                }
                if(!target.world.isRemote) {
                    ltp.studyEntity(target, CreatureManager.getInstance().config.creatureKillKnowledge, false, false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void soulgazeInteractEntity(PlayerInteractEvent.EntityInteractSpecific event){
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        boolean hasSoulgazer = false;
        if(player == null || target == null) return;

        if(event.getItemStack().getItem() instanceof ItemSoulgazer) {
            hasSoulgazer = true;
        }
        else if(event.getHand() == EnumHand.MAIN_HAND && Helpers.hasSoulgazerEquiped(player, true)){
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if(ltp != null && ltp.getSoulgazerManualToggle()) {
                hasSoulgazer = true;
                ItemSoulgazerTweaksHandler.soulgazeAbility(player, target);
            }
        }

        if(!hasSoulgazer || event.getWorld().isRemote) return;

        if(target instanceof BaseCreatureEntity
                && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerBuffFromPet
                && (player.isSneaking() || !ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerBuffFromPetSneak))
            ItemSoulgazerTweaksHandler.obtainBuffFromPet(player, (BaseCreatureEntity) target);

        if(target instanceof EntityPlayer && event.getWorld().rand.nextFloat() < 0.05){
            event.getWorld().playSound(null, event.getPos(), LycanitesTweaksRegistry.SOULGAZER_PLAYER, SoundCategory.PLAYERS, 1F, 1F);
        }
        else{
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
            if (pml != null) {
                if(target instanceof EntityBossSummonCrystal) {
                    IEntityStoreCreatureCapability storeCreature = target.getCapability(EntityStoreCreatureCapabilityHandler.ENTITY_STORE_CREATURE, null);
                    if(storeCreature != null) {
                        int levels = storeCreature.getStoredCreatureEntity().getLevel();
                        int variant = ((EntityBossSummonCrystal) target).getVariantType();

                        if(CreatureManager.getInstance().getCreature(storeCreature.getStoredCreatureEntity().creatureTypeName) == null)
                            player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal.vanilla",
                                    storeCreature.getStoredCreatureEntity().getDisplayName())
                            );
                        else if(variant == 1 && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.AltarBossMini)){
                            levels = levels + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.AltarBossMini,
                                    (BaseCreatureEntity)storeCreature.getStoredCreatureEntity().entity);
                            if(PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.AltarBossMini)){
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal",
                                        storeCreature.getStoredCreatureEntity().getLevel(),
                                        storeCreature.getStoredCreatureEntity().getDisplayName(),
                                        levels)
                                );
                            }
                            else{
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal.nogazer",
                                        levels,
                                        storeCreature.getStoredCreatureEntity().getDisplayName())
                                );
                            }
                        }
                        else if(variant == 2 && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.DungeonBoss)){
                            levels = levels + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.DungeonBoss,
                                    (BaseCreatureEntity)storeCreature.getStoredCreatureEntity().entity);
                            if(PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.DungeonBoss)){
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal",
                                        storeCreature.getStoredCreatureEntity().getLevel(),
                                        storeCreature.getStoredCreatureEntity().getDisplayName(),
                                        levels)
                                );
                            }
                            else{
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal.nogazer",
                                        levels,
                                        storeCreature.getStoredCreatureEntity().getDisplayName())
                                );
                            }
                        }
                        else if(variant == -1 && PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.EncounterEvent)){
                            levels = levels + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.EncounterEvent,
                                            (BaseCreatureEntity)storeCreature.getStoredCreatureEntity().entity);
                            if(PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.EncounterEvent)){
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal",
                                        storeCreature.getStoredCreatureEntity().getLevel(),
                                        storeCreature.getStoredCreatureEntity().getDisplayName(),
                                        levels)
                                );
                            }
                            else{
                                player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal.nogazer",
                                        levels,
                                        storeCreature.getStoredCreatureEntity().getDisplayName())
                                );
                            }
                        }
                        else{
                            player.sendMessage(new TextComponentTranslation("message.soulgazer.playermoblevels.bosscrystal.nogazer",
                                    levels,
                                    storeCreature.getStoredCreatureEntity().getDisplayName())
                            );
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void soulgazePassivePetImmunities(PotionEvent.PotionApplicableEvent event) {
        if(!ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerDebuffImmunity) return;
        if (event.isCanceled() || !(event.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.getEntityWorld().isRemote || !Helpers.hasSoulgazerEquiped(player)) return;

        boolean doesPetProtect = false;
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
        if(extendedPlayer == null) return;

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerDebuffImmunityKeybound) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if(ltp != null){
                PetEntry keyboundPet = extendedPlayer.petManager.getEntry(ltp.getKeyboundPetID());
                doesPetProtect = ItemSoulgazerTweaksHandler.doesPetHaveImmunity(keyboundPet, event.getPotionEffect());
            }
        }
        else {
            for(PetEntry petEntry : extendedPlayer.petManager.entries.values()){
                doesPetProtect = ItemSoulgazerTweaksHandler.doesPetHaveImmunity(petEntry, event.getPotionEffect());
                if(doesPetProtect) break;
            }
        }

        if(doesPetProtect) event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void genericStudyBreeding(BabyEntitySpawnEvent event) {
        EntityLiving parentA = event.getParentA();
        if(parentA.world.isRemote) return;

        for(EntityPlayer player : parentA.getEntityWorld().getPlayers(EntityPlayer.class, player -> player != null && parentA.getDistance(player) <= 5)) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
            if(extendedPlayer != null) {
                extendedPlayer.studyCreature(parentA, CreatureManager.getInstance().config.creatureBreedKnowledge, false, true);
            }
        }
    }

    @SubscribeEvent
    public static void genericStudyTaming(AnimalTameEvent event) {
        EntityPlayer player = event.getTamer();
        if(player.world.isRemote) return;

        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
        if(extendedPlayer != null) extendedPlayer.studyCreature(event.getAnimal(), CreatureManager.getInstance().config.creatureTreatKnowledge, false, true);
    }

    // Copy from ItemSoulgazer
    private static boolean soulgazeAbility(EntityPlayer player, Entity entity) {
        ExtendedPlayer playerExt = ExtendedPlayer.getForPlayer(player);
        if(playerExt != null) {
            int amount = CreatureManager.getInstance().config.creatureStudyKnowledge;
            if (playerExt.studyCreature(entity, amount, true, true)) {
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

        return soulgazeGenericEntity(player, entity);
    }

    public static boolean canStudyGenericEntity(EntityPlayer player, Entity target) {
        GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(target.getClass());
        if(entityInfo == null) return false;

        if(target instanceof EntityTameable && ((EntityTameable) target).isTamed())
            return false;

        return true;
    }

    public static boolean soulgazeGenericEntity(EntityPlayer player, Entity entity) {
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null) {
            int amount = CreatureManager.getInstance().config.creatureStudyKnowledge;
            if (canStudyGenericEntity(player, entity) && ltp.studyEntity(entity, amount, true, true)) {
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

    public static boolean doesPetHaveImmunity(PetEntry petEntry, PotionEffect effect){
        if(petEntry != null && petEntry.spawningActive && !petEntry.isRespawning) {
            if(petEntry.subspeciesIndex == 0) {
                for (ElementInfo petElement : petEntry.getCreatureInfo().elements) {
                    if (!petElement.isEffectApplicable(effect)) return true;
                }
            }
            else if (petEntry.entity instanceof BaseCreatureEntity) {
                BaseCreatureEntity creature = (BaseCreatureEntity) petEntry.entity;
                for (ElementInfo petElement : creature.getElements()) {
                    if (!petElement.isEffectApplicable(effect)) return true;
                }
            }
        }
        return false;
    }

    public static void obtainBuffFromPet(EntityPlayer player, BaseCreatureEntity creature) {
        if(!(creature instanceof TameableCreatureEntity)) return;
        TameableCreatureEntity petEntity = (TameableCreatureEntity) creature;

        if(!petEntity.isTamed() || petEntity.getPlayerOwner() != player) return;
        if(petEntity.creatureStats.getAmplifier() < 0) return;
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);

        if(extendedPlayer == null) return;
        if(extendedPlayer.creatureStudyCooldown > 0) return;
        petEntity.applyBuffs(player, 1, 1);

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerBuffStudyCooldown > 0)
            extendedPlayer.creatureStudyCooldown = ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.soulgazerBuffStudyCooldown;
    }
}
