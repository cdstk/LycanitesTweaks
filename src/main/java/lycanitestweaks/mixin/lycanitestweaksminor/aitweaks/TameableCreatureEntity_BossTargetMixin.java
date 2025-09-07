package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_BossTargetMixin extends AgeableCreatureEntity {

    @Shadow(remap = false) public abstract boolean isPVP();

    public TameableCreatureEntity_BossTargetMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "canAttackEntity",
            at = @At(value = "RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntity_canAttackEntityBoss(boolean original, EntityLivingBase targetEntity){
        if(!this.getEntityWorld().isRemote
                && targetEntity instanceof BaseCreatureEntity
                && (((BaseCreatureEntity) targetEntity).isBoss() || ((BaseCreatureEntity) targetEntity).isRareVariant())){
            return this.isPVP();
        }
        return original;
    }
}
