package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_RareSaddleMixin extends TameableCreatureEntity {

    public RideableCreatureEntity_RareSaddleMixin(World world) {
        super(world);
    }

    // TODO sync temporary status to client for this logic and to display
    @ModifyReturnValue(
            method = "hasSaddle",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsRideableCreatureEntity_hasSaddleRare(boolean original){
        return original || (this.isRareVariant() && this.isMinion() && this.isTamed());
    }
}
