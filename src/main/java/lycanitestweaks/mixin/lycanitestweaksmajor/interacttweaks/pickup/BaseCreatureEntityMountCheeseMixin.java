package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.pickup;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.IBaseCreatureEntityTransformIntoBossMixin;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityMountCheeseMixin extends EntityLiving {

    @Shadow(remap = false) public boolean isTemporary;

    public BaseCreatureEntityMountCheeseMixin(World world) {
        super(world);
    }

    @Inject(
            method = "canBeRidden",
            at = @At("HEAD"),
            cancellable = true
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canBeRiddenAntiMountCheese(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountCheeseFixTransform){
            if(this instanceof IBaseCreatureEntityTransformIntoBossMixin
                    && ((IBaseCreatureEntityTransformIntoBossMixin) this)
                    .lycanitesTweaks$canTransformIntoBoss())
                ((IBaseCreatureEntityTransformIntoBossMixin) this).lycanitesTweaks$transformIntoBoss();
        }

        if(this.noClip && ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountCheeseFixNoClip) cir.setReturnValue(false);
        if(this.isTemporary && ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountCheeseFixTemporary) cir.setReturnValue(false);
        if(this.isBeingRidden() && ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountCheeseFixMounted) cir.setReturnValue(false);
        if(LycanitesEntityUtil.isPracticallyFlying((BaseCreatureEntity)(Object)this) && ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountCheeseFixFlying)
            cir.setReturnValue(false);
    }
}
