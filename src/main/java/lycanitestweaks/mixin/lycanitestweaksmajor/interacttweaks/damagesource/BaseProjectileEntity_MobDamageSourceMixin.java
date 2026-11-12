package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import lycanitestweaks.util.DamageSources;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BaseProjectileEntity.class)
public abstract class BaseProjectileEntity_MobDamageSourceMixin extends EntityThrowable {

    public BaseProjectileEntity_MobDamageSourceMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 1)
            )
    )
    private DamageSource lycanitesTweaks_lycanitesMobsBaseProjectileEntity_updateDamageSourcePierce(DamageSource original){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource()).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    @ModifyExpressionValue(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 2)
    )
    private DamageSource lycanitesTweaks_lycanitesMobsBaseProjectileEntity_updateDamageSource(DamageSource original){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource());
    }
}
