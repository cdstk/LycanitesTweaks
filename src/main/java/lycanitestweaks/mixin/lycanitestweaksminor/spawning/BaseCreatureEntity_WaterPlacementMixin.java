package lycanitestweaks.mixin.lycanitestweaksminor.spawning;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_WaterPlacementMixin extends EntityLiving {

    public BaseCreatureEntity_WaterPlacementMixin(World world) {
        super(world);
    }

    @Inject(
            method = "getWaterSurfaceY",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getWaterSurfaceYSeaLevel(BlockPos pos, CallbackInfoReturnable<Integer> cir){
        if(ForgeConfigHandler.minorFeaturesConfig.waterMonsterCheckSeaTop) {
            if (pos.getY() <= this.getEntityWorld().getSeaLevel() && this.getEntityWorld().canBlockSeeSky(pos))
                cir.setReturnValue(this.getEntityWorld().getSeaLevel());
        }
    }
}
