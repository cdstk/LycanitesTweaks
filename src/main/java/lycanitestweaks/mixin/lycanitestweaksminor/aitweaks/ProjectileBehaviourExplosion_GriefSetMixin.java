package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ProjectileBehaviourExplosion.class)
public abstract class ProjectileBehaviourExplosion_GriefSetMixin {

    @ModifyArgs(
            method = "onProjectileImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;newExplosion(Lnet/minecraft/entity/Entity;DDDFZZ)Lnet/minecraft/world/Explosion;")
    )
    public void lycanitesTweaks_lycanitesMobsProjectileBehaviourExplosion_onProjectileImpactGrief(Args args, @Local(argsOnly = true) BaseProjectileEntity projectile){
        if(projectile.getThrower() instanceof TameableCreatureEntity) {
            args.set(5, args.<Boolean>get(5) && ((TameableCreatureEntity) projectile.getThrower()).isPVP());
            args.set(6, args.<Boolean>get(6) && ((TameableCreatureEntity) projectile.getThrower()).isPVP());
        }
    }
}
