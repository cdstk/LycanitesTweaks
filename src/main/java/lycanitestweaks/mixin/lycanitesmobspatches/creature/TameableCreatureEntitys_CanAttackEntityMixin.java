package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

@Mixin(value = {
        EntityAstaroth.class,
        EntityBehemoth.class,
        EntityCacodemon.class,
        EntityConba.class,
        EntityEnt.class,
        EntityTrite.class
})
public abstract class TameableCreatureEntitys_CanAttackEntityMixin extends TameableCreatureEntity {

    public TameableCreatureEntitys_CanAttackEntityMixin(World world) {
        super(world);
    }

    // Only Belph is proper
    @ModifyReturnValue(
            method = "canAttackEntity",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_canAttackEntityTamed(boolean canAttack, EntityLivingBase target){
        if(!canAttack){
            if(target instanceof TameableCreatureEntity)
                return this.getPlayerOwner() != ((TameableCreatureEntity) target).getPlayerOwner() && super.canAttackEntity(target);
            else
                return this.isTamed() && super.canAttackEntity(target);
        }
        return canAttack;
    }
}
