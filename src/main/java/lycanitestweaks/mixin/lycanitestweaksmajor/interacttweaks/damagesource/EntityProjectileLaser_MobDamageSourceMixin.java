package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.EntityProjectileLaser;
import lycanitestweaks.util.DamageSources;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(EntityProjectileLaser.class)
public abstract class EntityProjectileLaser_MobDamageSourceMixin extends BaseProjectileEntity {

    public EntityProjectileLaser_MobDamageSourceMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 1)
            )
    )
    private DamageSource lycanitesTweaks_lycanitesMobsEntityProjectileLaser_updateDamageSourcePierce(DamageSource original){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource()).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    @ModifyExpressionValue(
            method = "updateDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;causeThrownDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/DamageSource;", ordinal = 2)
    )
    private DamageSource lycanitesTweaks_lycanitesMobsEntityProjectileLaser_updateDamageSource(DamageSource original){
        return DamageSources.causeMobProjectileDamage(original.getImmediateSource(), original.getTrueSource());
    }
}
