package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.targeting.TargetingGoal;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(TargetingGoal.class)
public abstract class TargetingGoal_FlagMixin {

    @Shadow(remap = false)
    protected BaseCreatureEntity host;

    @ModifyReturnValue(
            method = "getPossibleTargets",
            at = @At("RETURN"),
            remap = false
    )
    public List<EntityLivingBase> lycanitesTweaks_lycanitesMobsTargetingGoal_getPossibleTargetsBoss(List<EntityLivingBase> original){
        if(this.host instanceof ITameableCreatureEntity_TargetFlagMixin && !((ITameableCreatureEntity_TargetFlagMixin) this.host).lycanitesTweaks$shouldTargetBoss()) {
            return original.stream().filter(entityLivingBase -> (
                    !(entityLivingBase instanceof BaseCreatureEntity
                    && (((BaseCreatureEntity) entityLivingBase).isBoss()
                        || ((BaseCreatureEntity) entityLivingBase).isRareVariant()))))
                    .collect(Collectors.toList());
        }
        return original;
    }
}
