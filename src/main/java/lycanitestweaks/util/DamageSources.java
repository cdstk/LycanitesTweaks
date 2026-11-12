package lycanitestweaks.util;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import lycanitestweaks.util.damagesource.BaseProjectileDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;

public class DamageSources {

    public static DamageSource causeMobProjectileDamage(Entity source, Entity indirectEntityIn) {
        return new BaseProjectileDamageSource("mob", source, indirectEntityIn).setProjectile();
    }

    public static DamageSource makeMobProjectileDamage(DamageSource damageSource) {
        return new BaseProjectileDamageSource(damageSource.damageType, damageSource.getImmediateSource(), damageSource.getTrueSource()).setProjectile();
    }

    public static  DamageSource rehandlePierceAttackMob(Entity target, BaseCreatureEntity attacker, DamageSource piercingSource, float amount, double pierceDamage){
        float pierce = (float) Math.min(pierceDamage, amount);

        if(piercingSource instanceof IDamageSource_UnsetPropertiesMixin) {
            IDamageSource_UnsetPropertiesMixin settable = (IDamageSource_UnsetPropertiesMixin) piercingSource;

            if(pierceDamage > 0 && pierce != amount) {
                target.attackEntityFrom(piercingSource, pierce);
                settable.lycanitesTweaks$unsetDamageIsAbsolute();
                settable.lycanitesTweaks$unsetDamageBypassesArmor();
            }

            return piercingSource;
        }
        else {
            DamageSource vanillaSource = attacker.getDamageSource(null, false);
            if(pierceDamage > 0 ) {
                vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                        target,
                        AttributesHandler.causeMeleePierceDamage(attacker, vanillaSource.getDamageType()),
                        vanillaSource,
                        (float) Math.min(pierceDamage, amount),
                        amount
                );
            }
            return vanillaSource;
        }
    }

    public static DamageSource rehandlePierceAttackThrown(Entity target, EntityThrowable projectile, DamageSource piercingSource, float amount, double pierceDamage){
        float pierce = (float) Math.min(pierceDamage, amount);
        boolean isBlocked = projectile instanceof BaseProjectileEntity && ((BaseProjectileEntity) projectile).isBlockedByEntity(target, projectile.getPositionVector());

        if(piercingSource instanceof IDamageSource_UnsetPropertiesMixin) {
            IDamageSource_UnsetPropertiesMixin settable = (IDamageSource_UnsetPropertiesMixin) piercingSource;

            if(pierceDamage > 0 && pierce != amount && !isBlocked) {
                target.attackEntityFrom(piercingSource, pierce);
                settable.lycanitesTweaks$unsetDamageIsAbsolute();
                settable.lycanitesTweaks$unsetDamageBypassesArmor();
            }

            return piercingSource;
        }
        else {
            DamageSource vanillaSource = DamageSource.causeThrownDamage(projectile, projectile.getThrower());;
            if(pierceDamage > 0 && !isBlocked) {
                vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                        target,
                        AttributesHandler.causeProjectilePierceDamage(projectile.getThrower(), projectile, vanillaSource.getDamageType()),
                        vanillaSource,
                        pierce,
                        amount
                );
            }
            return vanillaSource;
        }
    }
}
