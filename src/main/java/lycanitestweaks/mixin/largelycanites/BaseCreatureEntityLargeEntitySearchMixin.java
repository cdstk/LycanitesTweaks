package lycanitestweaks.mixin.largelycanites;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.handlers.features.entity.LargeEntitySearchOverride;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO Merge Early and Late after making FermiumBooter 1.6.0 Dependency
@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityLargeEntitySearchMixin extends EntityLiving {

    @Shadow(remap = false) public double sizeScale;
    @Shadow(remap = false) public abstract boolean isBossAlways();

    public BaseCreatureEntityLargeEntitySearchMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateAddLarge(CallbackInfo ci){
        if(this.ticksExisted % 100 == 0) {
            if(this.isBossAlways()) {
                LargeEntitySearchOverride.addActiveEntity(this);
            }
            // Should work if instant FirstSpawn is enabled
            else if(this.sizeScale > CreatureManager.getInstance().config.randomSizeMax) {
                LargeEntitySearchOverride.addActiveEntity(this);
            }
        }
    }
}
