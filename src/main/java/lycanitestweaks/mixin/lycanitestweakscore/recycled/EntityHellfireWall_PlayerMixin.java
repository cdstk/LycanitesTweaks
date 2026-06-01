package lycanitestweaks.mixin.lycanitestweakscore.recycled;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityHellfireWall.class)
public abstract class EntityHellfireWall_PlayerMixin extends BaseProjectileEntity {

    public EntityHellfireWall_PlayerMixin(World world) {
        super(world);
    }

    @WrapWithCondition(
            method = "onDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setHealth(F)V")
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityHellfireWall_onDamageThrowerPlayer(EntityLivingBase instance, float health){
        return !(this.getThrower() instanceof EntityPlayer);
    }
}
