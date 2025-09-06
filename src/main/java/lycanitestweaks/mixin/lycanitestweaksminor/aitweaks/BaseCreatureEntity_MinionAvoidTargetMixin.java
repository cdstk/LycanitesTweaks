package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_MinionAvoidTargetMixin extends EntityLiving {

    @Shadow(remap = false)
    public abstract boolean isMinion();

    public BaseCreatureEntity_MinionAvoidTargetMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "getAvoidTarget",
            at = @At("RETURN"),
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getAvoidTargetMinion(EntityLivingBase original){
        return (this.isMinion()) ? null : original;
    }
}
