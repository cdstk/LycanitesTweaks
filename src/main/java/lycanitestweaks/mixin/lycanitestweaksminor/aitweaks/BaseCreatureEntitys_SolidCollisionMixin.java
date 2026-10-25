package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityBeholder;
import com.lycanitesmobs.core.entity.creature.EntityEttin;
import com.lycanitesmobs.core.entity.creature.EntityKhalk;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import com.lycanitesmobs.core.entity.creature.EntityTreant;
import com.lycanitesmobs.core.entity.creature.EntityTroll;
import com.lycanitesmobs.core.entity.creature.EntityVespidQueen;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
        EntityAsmodeus.class,
        EntityBeholder.class,
        EntityEttin.class,
        EntityKhalk.class,
        EntityRahovart.class,
        EntityTreant.class,
        EntityTroll.class,
        EntityVespidQueen.class
})
public abstract class BaseCreatureEntitys_SolidCollisionMixin extends BaseCreatureEntity {

    public BaseCreatureEntitys_SolidCollisionMixin(World world) {
        super(world);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntitys_initSolidCollisionOverride(World world, CallbackInfo ci){
        this.solidCollision = false;
    }
}
