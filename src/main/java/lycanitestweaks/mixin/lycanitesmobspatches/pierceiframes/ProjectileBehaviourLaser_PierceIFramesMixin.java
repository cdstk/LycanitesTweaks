package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourLaser;
import lycanitestweaks.util.DamageSources;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

    @ModifyArgs(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsProjectileBehaviourLaser_updateDamageRehandlePierceAttack(Args args, @Local(argsOnly = true) BaseProjectileEntity projectile, @Local(argsOnly = true) Entity target, @Local(name = "pierceDamage") double pierceDamage){
        args.set(0, DamageSources.rehandlePierceAttackThrown(target, projectile, args.get(0), args.get(1), pierceDamage));
    }
}
