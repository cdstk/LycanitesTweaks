package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.DamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_PierceIFramesMixin extends EntityLiving {

    public BaseCreatureEntity_PierceIFramesMixin(World world) {
        super(world);
    }

    @Definition(id = "pierceDamage", local = @Local(type = double.class, name = "pierceDamage"))
    @Expression("? <= pierceDamage")
    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageRedirectToSingleAttack(boolean isMaxPierce){
        return true;
    }

    @ModifyArgs(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageRehandlePierceAttack(Args args, @Local(argsOnly = true) Entity target, @Local(argsOnly = true) EntityThrowable projectile, @Local(argsOnly = true) boolean noPierce, @Local(name = "pierceDamage") double pierceDamage){
        args.set(0, DamageSources.rehandlePierceAttackThrown(target, projectile, args.get(0), args.get(1), pierceDamage));
    }

    @Definition(id = "pierceDamage", local = @Local(type = double.class, name = "pierceDamage"))
    @Expression("? <= pierceDamage")
    @ModifyExpressionValue(
            method = "attackEntityAsMob",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobRedirectToSingleAttack(boolean isMaxPierce){
        return true;
    }

    @ModifyArgs(
            method = "attackEntityAsMob",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobRehandlePierceAttack(Args args, @Local(argsOnly = true) Entity target, @Local(name = "pierceDamage") double pierceDamage){
        args.set(0, DamageSources.rehandlePierceAttackMob(target, (BaseCreatureEntity) (Object) this, args.get(0), args.get(1), pierceDamage));
    }
}
