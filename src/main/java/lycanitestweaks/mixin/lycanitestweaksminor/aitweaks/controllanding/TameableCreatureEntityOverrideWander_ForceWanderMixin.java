package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.controllanding;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityGrigori;
import com.lycanitesmobs.core.entity.creature.EntityMorock;
import com.lycanitesmobs.core.entity.creature.EntityQuetzodracl;
import com.lycanitesmobs.core.entity.creature.EntityRaiko;
import com.lycanitesmobs.core.entity.creature.EntityRoc;
import com.lycanitesmobs.core.entity.creature.EntityWisp;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        EntityGrigori.class,
        EntityMorock.class,
        EntityQuetzodracl.class,
        EntityRaiko.class,
        EntityRoc.class,
        EntityWisp.class,
        EntityWraith.class
})
public abstract class TameableCreatureEntityOverrideWander_ForceWanderMixin extends TameableCreatureEntity {

    public TameableCreatureEntityOverrideWander_ForceWanderMixin(World world) {
        super(world);
    }

    @Inject(
            method = "rollWanderChance",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_rollWanderChancePlayerForced(CallbackInfoReturnable<Boolean> cir){
        if(this.isTamed()) {
            cir.setReturnValue(super.rollWanderChance());
        }
    }
}
