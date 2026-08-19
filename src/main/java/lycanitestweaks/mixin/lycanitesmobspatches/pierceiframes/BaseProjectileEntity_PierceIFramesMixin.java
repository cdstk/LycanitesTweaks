package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseProjectileEntity.class)
public abstract class BaseProjectileEntity_PierceIFramesMixin extends EntityThrowable {

    @Shadow(remap = false)
    public abstract boolean isBlockedByEntity(Entity targetEntity, Vec3d damagePostition);

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

    @WrapOperation(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseProjectileEntity_onImpactRehandlePierceAttack(EntityLivingBase target, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) RayTraceResult rayTraceResult, @Local(name = "pierceDamage") double pierceDamage){
        DamageSource vanillaSource = DamageSource.causeThrownDamage(this, this.getThrower());

        if(pierceDamage > 0) {
            if(!this.isBlockedByEntity(target, rayTraceResult.hitVec))
                vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                        target,
                        AttributesHandler.causeProjectilePierceDamage(this.getThrower(), this, vanillaSource.getDamageType()),
                        vanillaSource,
                        (float) Math.min(pierceDamage, amount),
                        amount
                );
        }

        return original.call(target, vanillaSource, amount);
    }
}
