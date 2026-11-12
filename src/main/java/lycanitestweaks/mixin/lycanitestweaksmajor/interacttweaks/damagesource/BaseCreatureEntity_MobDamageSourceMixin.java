package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.util.DamageSources;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_MobDamageSourceMixin {

    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 1)
            )
    )
    private DamageSource lycanitesTweaks_lycanitesMobsindirectKiller_updateDamageSourcePierce(DamageSource original, @Local(argsOnly = true) EntityThrowable projectile){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource()).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 2)
    )
    private DamageSource lycanitesTweaks_lycanitesMobsindirectKiller_updateDamageSource(DamageSource original, @Local(argsOnly = true) EntityThrowable projectile){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource());
    }
}
