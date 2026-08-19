package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourLaser;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ProjectileBehaviourLaser.class)
public abstract class ProjectileBehaviourLaser_PierceIFramesMixin {

    @Definition(id = "pierceDamage", local = @Local(type = double.class, name = "pierceDamage"))
    @Expression("? <= pierceDamage")
    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsProjectileBehaviourLaser_updateDamageRedirectToSingleAttack(boolean isMaxPierce){
        return true;
    }

    @WrapOperation(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsProjectileBehaviourLaser_updateDamageRehandlePierceAttack(Entity target, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) BaseProjectileEntity projectile, @Local(name = "pierceDamage") double pierceDamage){
        DamageSource vanillaSource = DamageSource.causeThrownDamage(projectile, projectile.getThrower());

        if(pierceDamage > 0) {
            if(!projectile.isBlockedByEntity(target, projectile.getPositionVector()))
                    vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                        target,
                        AttributesHandler.causeProjectilePierceDamage(projectile.getThrower(), projectile, vanillaSource.getDamageType()),
                        vanillaSource,
                        (float) Math.min(pierceDamage, amount),
                        amount
                );
        }

        return original.call(target, vanillaSource, amount);
    }
}
