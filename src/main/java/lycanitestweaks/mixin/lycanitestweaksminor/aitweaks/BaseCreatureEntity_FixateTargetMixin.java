package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_FixateTargetMixin extends EntityLiving {

    public BaseCreatureEntity_FixateTargetMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "getFixateTarget",
            at = @At("RETURN"),
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getFixateTargetRevenge(EntityLivingBase original){
        if(this.getRevengeTarget() != null) return this.getRevengeTarget();
        return original;
    }
}
