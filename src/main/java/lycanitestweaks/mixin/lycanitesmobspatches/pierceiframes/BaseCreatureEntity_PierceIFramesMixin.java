package lycanitestweaks.mixin.lycanitesmobspatches.pierceiframes;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.features.entity.AttributesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_PierceIFramesMixin extends EntityLiving {

    @Shadow(remap = false)
    public abstract DamageSource getDamageSource(EntityDamageSource nestedDamageSource, boolean playerCredit);

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

    @WrapOperation(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageRehandlePierceAttack(Entity target, DamageSource source, float amount, Operation<Boolean> original, @Local(argsOnly = true) EntityThrowable projectile, @Local(argsOnly = true) boolean noPierce, @Local(name = "pierceDamage") double pierceDamage){
        DamageSource vanillaSource = DamageSource.causeThrownDamage(projectile, this);

        if(pierceDamage > 0) {
            vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                    target,
                    AttributesHandler.causeProjectilePierceDamage(this, projectile, source.getDamageType()),
                    vanillaSource,
                    (float) Math.min(pierceDamage, amount),
                    amount
            );
        }

        return original.call(target, vanillaSource, amount);
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

    @WrapOperation(
            method = "attackEntityAsMob",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", ordinal = 0)
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobRehandlePierceAttack(Entity target, DamageSource source, float amount, Operation<Boolean> original, @Local(name = "pierceDamage") double pierceDamage){
        DamageSource vanillaSource = this.getDamageSource(null, false);

        if(pierceDamage > 0) {
            vanillaSource = AttributesHandler.doPreemptivePierceAttack(
                    target,
                    AttributesHandler.causeMeleePierceDamage(this, vanillaSource.getDamageType()),
                    vanillaSource,
                    (float) Math.min(pierceDamage, amount),
                    amount
            );
        }
        return original.call(target, vanillaSource, amount);
    }
}
