package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireBarrier;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHellfireBarrier.class)
public abstract class EntityHellfireBarrierTweaksMixin extends BaseProjectileEntity {

    @Shadow(remap = false)
    public EntityHellfireWall[][] hellfireWalls;
    @Shadow(remap = false)
    public boolean wall;

    public EntityHellfireBarrierTweaksMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/projectile/EntityHellfireWall;projectileLife:I", ordinal = 1, remap = false)
    )
    public void lycanitesTweaks_lycanitesMobsEntityHellfireBarrier_onUpdate(CallbackInfo ci, @Local(ordinal = 0) int row, @Local(ordinal = 1) int col, @Local(ordinal = 0) double rotationRadians){
        if(this.getThrower() instanceof EntityPlayer) return;

        if((this.wall && col < ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireWallDisplacement) ||
                (!this.wall && col < ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireBarrierDisplacement)){
            this.hellfireWalls[row][col].posX = this.hellfireWalls[row][col+1].posX;
            this.hellfireWalls[row][col].posZ = this.hellfireWalls[row][col+1].posZ;
        }
    }
}
