package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBeholder;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.entity.creature.EntityEpion;
import com.lycanitesmobs.core.entity.creature.EntityErepede;
import com.lycanitesmobs.core.entity.creature.EntityGrell;
import com.lycanitesmobs.core.info.projectile.ProjectileInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        EntityBeholder.class,
        EntityCacodemon.class,
        EntityEpion.class,
        EntityErepede.class,
        EntityGrell.class
})
public abstract class RideableCreatureEntity_SingleProjectileAbilityScaleMixin extends RideableCreatureEntity {

    public RideableCreatureEntity_SingleProjectileAbilityScaleMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/projectile/ProjectileInfo;createProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;"),
            index = 1,
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsRideableCreatureEntity_mountAbilityLevelScaleSingleProjectile(EntityLivingBase entityLivingBase){
        return this;
    }

    @ModifyExpressionValue(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/projectile/ProjectileInfo;createProjectile(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;"),
            remap = false
    )
    public BaseProjectileEntity lycanitesTweaks_lycanitesMobsRideableCreatureEntity_mountAbilitySingleShootFromPlayer(BaseProjectileEntity projectile, @Local EntityPlayer player, @Local ProjectileInfo projectileInfo){
        projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, (float)projectileInfo.velocity, 1.0F);
        return projectile;
    }
}
