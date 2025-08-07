package lycanitestweaks.util;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.block.BlockFireBase;
import com.lycanitesmobs.core.entity.creature.EntityTremor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class LycanitesMobsWrapper {

    public static boolean hasSmitedEffect(EntityLivingBase entity){
        if(entity.getActivePotionMap() == null) return false; // Fix null pointer for mods like Special Mobs calling in entityInit
        return entity.isPotionActive(ObjectManager.getEffect("smited"));
    }

    public static boolean isLycanitesFire(IBlockAccess world, BlockPos pos){ return (world.getBlockState(pos).getBlock() instanceof BlockFireBase); }

    public static boolean isTremor(Entity entity) { return entity instanceof EntityTremor; }
}
