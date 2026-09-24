package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Credit https://github.com/Fresh-glitch for reporting
@Mixin(EntityPlayer.class)
public abstract class EntityPlayer_SneakDismountFlagMixin extends EntityLivingBase {

    public EntityPlayer_SneakDismountFlagMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "updateRidden",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isRiding()Z")
    )
    private boolean lycanitesTweaks_vanillaEntityPlayer_updateRiddenLycanitesSneakFlagFix(boolean isRiding){
        if(isRiding) {
            if(LycanitesMobsWrapper.isLycanitesRideableEntity(this.getRidingEntity())) {
                return LycanitesMobsWrapper.getPlayerControlDismounting((EntityPlayer) (Object) this);
            }
        }
        return isRiding;
    }
}
