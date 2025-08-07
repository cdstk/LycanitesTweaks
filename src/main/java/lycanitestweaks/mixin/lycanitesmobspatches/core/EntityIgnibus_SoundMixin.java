package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibus_SoundMixin extends RideableCreatureEntity {

    public EntityIgnibus_SoundMixin(World world) {
        super(world);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityIgnibus_initAttackSound(World world, CallbackInfo ci){
        this.hasAttackSound = true;
    }
}
