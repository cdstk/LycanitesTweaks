package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.PortalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalEntity.class)
public abstract class PortalEntity_LateStatCalcMixin {

    // Earlier than the PML Portal Entity
    @Inject(
            method = "summonCreatures",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/PortalEntity;shootingEntity:Lnet/minecraft/entity/player/EntityPlayer;", ordinal = 3),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsPortalEntity_summonCreatures(CallbackInfoReturnable<Integer> cir, @Local BaseCreatureEntity entityCreature){
        if(entityCreature != null) entityCreature.refreshAttributes();
    }
}
