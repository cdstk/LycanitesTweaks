package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityEyewig;
import com.lycanitesmobs.core.entity.creature.EntityIoray;
import com.lycanitesmobs.core.info.projectile.ProjectileInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityEyewig.class,
        EntityIoray.class
})
public abstract class RideableCreatureEntity_LaserProjectileAbilityScaleMixin extends RideableCreatureEntity {

    public RideableCreatureEntity_LaserProjectileAbilityScaleMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/projectile/ProjectileInfo;createProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;"),
            remap = false
    )
    public BaseProjectileEntity lycanitesTweaks_lycanitesMobsRideableCreatureEntity_mountAbilityLaserShootFromPlayer(BaseProjectileEntity projectile, @Local(argsOnly = true) Entity rider, @Local ProjectileInfo projectileInfo){
        projectile.shoot(rider, rider.rotationPitch, rider.rotationYaw, 0.0F, (float)projectileInfo.velocity, 1.0F);
        return projectile;
    }
}
