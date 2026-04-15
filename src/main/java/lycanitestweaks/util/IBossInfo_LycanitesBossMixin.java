package lycanitestweaks.util;

import net.minecraft.entity.EntityLiving;

public interface IBossInfo_LycanitesBossMixin {

    boolean lycanitesTweaks$isLycanitesBoss();
    void lycanitesTweaks$setLycanitesBoss(EntityLiving entity);
    EntityLiving lycanitesTweaks$getLycanitesEntity();
}
