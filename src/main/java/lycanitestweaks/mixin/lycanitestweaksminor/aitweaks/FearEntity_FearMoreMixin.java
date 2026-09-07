package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.FearEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FearEntity.class)
public abstract class FearEntity_FearMoreMixin extends BaseCreatureEntity {

    public FearEntity_FearMoreMixin(World world) {
        super(world);
    }

    @Inject(
            method = "isFlying",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/FearEntity;pickupEntity:Lnet/minecraft/entity/EntityLivingBase;", ordinal = 4),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsFearEntity_isFlyingNoGravityVictim(CallbackInfoReturnable<Boolean> cir){
        if(this.pickupEntity.hasNoGravity())
            cir.setReturnValue(true);
    }
}
