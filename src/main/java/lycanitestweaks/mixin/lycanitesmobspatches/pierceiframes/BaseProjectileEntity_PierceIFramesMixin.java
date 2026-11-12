package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import lycanitestweaks.util.DamageSources;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BaseProjectileEntity.class)
public abstract class BaseProjectileEntity_PierceIFramesMixin extends EntityThrowable {

    public BaseProjectileEntity_PierceIFramesMixin(World world) {
        super(world);
    }

    @Definition(id = "pierceDamage", local = @Local(type = double.class, name = "pierceDamage"))
    @Expression("? <= pierceDamage")
    @ModifyExpressionValue(
            method = "onImpact",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseProjectileEntity_onImpactRedirectToSingleAttack(boolean isMaxPierce){
        return true;
    }

    @ModifyArgs(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsBaseProjectileEntity_onImpactRehandlePierceAttack(Args args, @Local EntityLivingBase target, @Local(name = "pierceDamage") double pierceDamage){
        args.set(0, DamageSources.rehandlePierceAttackThrown(target, (BaseProjectileEntity) (Object) this, args.get(0), args.get(1), pierceDamage));
    }
}
