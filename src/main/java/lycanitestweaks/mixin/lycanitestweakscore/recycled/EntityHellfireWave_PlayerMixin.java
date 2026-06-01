package lycanitestweaks.mixin.lycanitestweakscore.recycled;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHellfireWave.class)
public abstract class EntityHellfireWave_PlayerMixin extends BaseProjectileEntity {

    @Shadow(remap = false) public EntityHellfireWall[][] hellfireWalls;

    public EntityHellfireWave_PlayerMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void lycanitesTweaks_lycanitesMobsEntityHellfireWave_onUpdateThrowerPlayer(CallbackInfo ci, @Local(name = "row") int row, @Local(name =  "col") int col){
        if(this.getThrower() instanceof EntityPlayer) {
            this.hellfireWalls[row][col].setDamage(this.damage);
            this.hellfireWalls[row][col].pierce = this.pierce;
        }
    }
}
