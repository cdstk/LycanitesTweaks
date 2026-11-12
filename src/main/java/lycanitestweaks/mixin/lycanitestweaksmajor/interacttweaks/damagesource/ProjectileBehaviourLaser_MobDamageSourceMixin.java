package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourLaser;
import lycanitestweaks.util.DamageSources;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ProjectileBehaviourLaser.class)
public abstract class ProjectileBehaviourLaser_MobDamageSourceMixin {

    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 1)
            )
    )
    private DamageSource lycanitesTweaks_lycanitesMobsProjectileBehaviourLaser_updateDamageSourcePierce(DamageSource original, BaseProjectileEntity projectile){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource()).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 2)
    )
    private DamageSource lycanitesTweaks_lycanitesMobsProjectileBehaviourLaser_updateDamageSource(DamageSource original, BaseProjectileEntity projectile){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource());
    }
}
