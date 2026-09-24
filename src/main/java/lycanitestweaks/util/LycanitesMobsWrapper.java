package lycanitestweaks.util;

import com.google.common.base.Predicate;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.block.BlockFireBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityTremor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class LycanitesMobsWrapper {

    public static final Predicate<Entity> TEMPORARY_EVENT_MOB = entity -> {
        if(entity instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) entity;
            return creature.isTemporary && !"".equals(creature.spawnEventType);
        }
        return false;
    };

    public static boolean isLycanitesEntity(Entity entity){
        return entity instanceof BaseCreatureEntity;
    }

    public static boolean isLycanitesRideableEntity(Entity entity){
        return entity instanceof RideableCreatureEntity;
    }

    public static void dropPickedUpBy(Entity victim) {
        if(victim instanceof EntityLivingBase) {
            ExtendedEntity extendedEntity = ExtendedEntity.getForEntity((EntityLivingBase) victim);
            if(extendedEntity != null && extendedEntity.isPickedUp()){
                if(extendedEntity.pickedUpByEntity instanceof BaseCreatureEntity) {
                    BaseCreatureEntity creature = (BaseCreatureEntity) extendedEntity.pickedUpByEntity;
                    creature.dropPickupEntity();

                    if(creature.creatureInfo.dummy) // Respawn Fear
                        creature.setDead();
                }
                else extendedEntity.setPickedUpByEntity(null);
            }
        }
    }

    public static boolean hasSmitedEffect(EntityLivingBase entity){
        if(entity.getActivePotionMap() == null) return false; // Fix null pointer for mods like Special Mobs calling in entityInit
        return entity.isPotionActive(ObjectManager.getEffect("smited"));
    }

    public static boolean isLycanitesFire(IBlockAccess world, BlockPos pos){ return (world.getBlockState(pos).getBlock() instanceof BlockFireBase); }

    public static boolean isTremor(Entity entity) { return entity instanceof EntityTremor; }

    public static boolean getPlayerControlDismounting(EntityPlayer player) {
        if(LycanitesMobs.config.getBool("Extras", "Disable Sneak Dismount", true)) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
            if(extendedPlayer != null) {
                return extendedPlayer.isControlActive(ExtendedPlayer.CONTROL_ID.MOUNT_DISMOUNT);
            }
        }
        return false;
    }

    public static boolean setInstantDespawn(EntityLivingBase entityLivingBase) {
        if(entityLivingBase instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) entityLivingBase;
            boolean alreadyDespawning = creature.temporaryDuration == 0;
            creature.setTemporary(0);
            return !alreadyDespawning;
        }
        return false;
    }
}
