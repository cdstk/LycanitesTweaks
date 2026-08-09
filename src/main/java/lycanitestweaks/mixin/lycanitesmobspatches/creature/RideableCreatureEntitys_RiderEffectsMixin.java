package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBarghest;
import com.lycanitesmobs.core.entity.creature.EntityBeholder;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.entity.creature.EntityFeradon;
import com.lycanitesmobs.core.entity.creature.EntityMaug;
import com.lycanitesmobs.core.entity.creature.EntityMorock;
import com.lycanitesmobs.core.entity.creature.EntityPinky;
import com.lycanitesmobs.core.entity.creature.EntitySalamander;
import com.lycanitesmobs.core.entity.creature.EntityThresher;
import com.lycanitesmobs.core.entity.creature.EntityVentoraptor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
        EntityBarghest.class,
        EntityBeholder.class,
        EntityCacodemon.class,
        EntityFeradon.class,
        EntityMaug.class,
        EntityMorock.class,
        EntityPinky.class,
        EntitySalamander.class,
        EntityThresher.class,
        EntityVentoraptor.class
})
public abstract class RideableCreatureEntitys_RiderEffectsMixin extends RideableCreatureEntity {

    public RideableCreatureEntitys_RiderEffectsMixin(World world) {
        super(world);
    }

    @Inject(
            method = "riderEffects",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_riderEffectsCallSuper(EntityLivingBase rider, CallbackInfo ci){
        super.riderEffects(rider);
    }
}
