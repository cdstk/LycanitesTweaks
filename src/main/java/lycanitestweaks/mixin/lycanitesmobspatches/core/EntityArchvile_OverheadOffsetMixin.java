package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.creature.EntityArchvile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityArchvile.class)
public abstract class EntityArchvile_OverheadOffsetMixin {

    @ModifyArgs(
            method = "attackRanged",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseProjectileEntity;setPosition(DDD)V")
    )
    public void lycanitesTweaks_lycanitesMobsEntityArchvile_attackRanged(Args args){
        args.set(0, args.<Double>get(0) + 0.5);
        args.set(2, args.<Double>get(2) + 0.5);
    }
}
