package lycanitestweaks.mixin.lycanitestweakscore.recycled;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireBarrier;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHellfireBarrier.class)
public abstract class EntityHellfireBarrier_PlayerMixin extends BaseProjectileEntity {

    @Shadow(remap = false) public EntityHellfireWall[][] hellfireWalls;

    public EntityHellfireBarrier_PlayerMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void lycanitesTweaks_lycanitesMobsEntityHellfireBarrier_onUpdateThrowerPlayer(CallbackInfo ci, @Local(name = "row") int row, @Local(name =  "col") int col){
        if(this.getThrower() instanceof EntityPlayer) {
            this.hellfireWalls[row][col].setDamage(this.damage);
            this.hellfireWalls[row][col].pierce = this.pierce;
        }
    }
}
