package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import com.lycanitesmobs.core.entity.creature.EntityTreant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityAmalgalich.class,
        EntityAsmodeus.class,
        EntityRahovart.class,
        EntityTreant.class
})
public abstract class BaseCreatureEntitys_CanAttackEntityMixin extends BaseCreatureEntity {

    public BaseCreatureEntitys_CanAttackEntityMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "canAttackEntity",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_canAttackEntityTamed(boolean canAttack, EntityLivingBase target){
        if(!canAttack){
            return target instanceof TameableCreatureEntity && ((TameableCreatureEntity) target).isTamed() && super.canAttackEntity(target);
        }
        return canAttack;
    }
}
