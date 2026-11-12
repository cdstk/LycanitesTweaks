package lycanitestweaks.util;

import net.minecraft.util.DamageSource;

public interface IDamageSource_UnsetPropertiesMixin {

    DamageSource lycanitesTweaks$unsetProjectile();
    DamageSource lycanitesTweaks$unsetExplosion();
    DamageSource lycanitesTweaks$unsetDamageBypassesArmor();
    DamageSource lycanitesTweaks$unsetDamageAllowedInCreativeMode();
    DamageSource lycanitesTweaks$unsetDamageIsAbsolute();
    DamageSource lycanitesTweaks$unsetFireDamage();
    DamageSource lycanitesTweaks$unsetDifficultyScaled();
    DamageSource lycanitesTweaks$unsetMagicDamage();
}
