package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.RapidFireProjectileEntity;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import com.lycanitesmobs.core.info.projectile.ProjectileInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibus_RapidProjectileAbilityScaleMixin extends RideableCreatureEntity {

    public EntityIgnibus_RapidProjectileAbilityScaleMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/RapidFireProjectileEntity;<init>(Lcom/lycanitesmobs/core/info/projectile/ProjectileInfo;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;II)V", ordinal = 0),
            index = 2,
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsEntityIgnibus_mountAbilityLevelScaleProjectile(EntityLivingBase entityLivingBase){
        return this;
    }

    @ModifyReceiver(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/RapidFireProjectileEntity;setProjectileScale(F)V", ordinal = 6),
            remap = false
    )
    public RapidFireProjectileEntity lycanitesTweaks_lycanitesMobsEntityIgnibus_mountAbilityShootFromPlayer(RapidFireProjectileEntity projectile, float v, @Local EntityPlayer player, @Local ProjectileInfo projectileInfo){
        projectile.posY = this.posY + this.getMountedYOffset() + player.getYOffset() + player.getEyeHeight() - 0.1D;
        projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, (float)projectileInfo.velocity, 1.0F);
        return projectile;
    }
}
