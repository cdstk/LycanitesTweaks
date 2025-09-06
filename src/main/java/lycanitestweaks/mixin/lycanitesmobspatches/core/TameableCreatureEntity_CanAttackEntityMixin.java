package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAstaroth;
import com.lycanitesmobs.core.entity.creature.EntityBehemoth;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.entity.creature.EntityConba;
import com.lycanitesmobs.core.entity.creature.EntityEnt;
import com.lycanitesmobs.core.entity.creature.EntityTrite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        EntityAstaroth.class,
        EntityBehemoth.class,
        EntityCacodemon.class,
        EntityConba.class,
        EntityEnt.class,
        EntityTrite.class
})
public abstract class TameableCreatureEntity_CanAttackEntityMixin extends TameableCreatureEntity {

    public TameableCreatureEntity_CanAttackEntityMixin(World world) {
        super(world);
    }

    // Only Belph is proper
    @Inject(
            method = "canAttackEntity",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_canAttackEntityTamed(EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        if(this.isTamed()) cir.setReturnValue(super.canAttackEntity(target));
    }
}
