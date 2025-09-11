package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_SetAttackTargetMixin extends AgeableCreatureEntity {

    public TameableCreatureEntity_SetAttackTargetMixin(World world) {
        super(world);
    }

    @Unique
    @Override
    public boolean canBeTargetedBy(EntityLivingBase entity) {
        if(this.isTamed() && entity.getRevengeTarget() != this && (entity instanceof EntityIronGolem || entity instanceof EntitySnowman)) return false;
        return super.canBeTargetedBy(entity);
    }
}
