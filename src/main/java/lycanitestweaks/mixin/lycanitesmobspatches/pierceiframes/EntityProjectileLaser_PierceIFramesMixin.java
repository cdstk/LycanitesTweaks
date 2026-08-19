package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.EntityProjectileLaser;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityProjectileLaser.class)
public abstract class EntityProjectileLaser_PierceIFramesMixin extends BaseProjectileEntity {

    public EntityProjectileLaser_PierceIFramesMixin(World world) {
        super(world);
    }

    @Definition(id = "pierceDamage", local = @Local(type = double.class, name = "pierceDamage"))
    @Expression("? <= pierceDamage")
    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityProjectileLaser_updateDamageRedirectToSingleAttack(boolean isMaxPierce){
        return true;
    }

    @WrapOperation(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityProjectileLaser_updateDamageRehandlePierceAttack(Entity target, DamageSource source, float amount, Operation<Boolean> original, @Local(name = "pierceDamage") double pierceDamage){
        DamageSource vanillaSource = DamageSource.causeThrownDamage(this, this.getThrower());

        if(pierceDamage > 0) {
            if(!this.isBlockedByEntity(target, this.getPositionVector()))
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
