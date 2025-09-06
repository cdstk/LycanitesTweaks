package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.goals.actions.abilities.FireProjectilesGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FireProjectilesGoal.class)
public abstract class FireProjectilesGoal_OverheadOffsetMixin {

    @ModifyArgs(
            method = "fireProjectileOverhead",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;setPosition(DDD)V")
    )
    public void lycanitesTweaks_lycanitesMobsFireProjectilesGoal_fireProjectileOverhead(Args args){
        args.set(0, args.<Double>get(0) + 0.5);
        args.set(2, args.<Double>get(2) + 0.5);
    }
}
