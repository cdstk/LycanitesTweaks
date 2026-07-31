package lycanitestweaks.util;

import com.google.common.base.Predicate;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.block.BlockFireBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityTremor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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

    public static boolean isLycanitesEntity(EntityLivingBase entity){
        return entity instanceof BaseCreatureEntity;
    }

    public static boolean hasSmitedEffect(EntityLivingBase entity){
        if(entity.getActivePotionMap() == null) return false; // Fix null pointer for mods like Special Mobs calling in entityInit
        return entity.isPotionActive(ObjectManager.getEffect("smited"));
    }

    public static boolean isLycanitesFire(IBlockAccess world, BlockPos pos){ return (world.getBlockState(pos).getBlock() instanceof BlockFireBase); }

    public static boolean isTremor(Entity entity) { return entity instanceof EntityTremor; }

    public static void setInstantDespawn(EntityLivingBase entityLivingBase) {
        if(entityLivingBase instanceof BaseCreatureEntity)
            ((BaseCreatureEntity) entityLivingBase).setTemporary(0);
    }
}
