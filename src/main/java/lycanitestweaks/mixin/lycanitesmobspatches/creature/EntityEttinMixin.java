package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(com.lycanitesmobs.core.entity.creature.EntityEttin.class)
public abstract class EntityEttinMixin {

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityEttin;griefing:Z", remap = false)
    )
    public boolean lycanitestweaks_lycanitesEntityEttin_onLivingUpdate(boolean original){
        return !original;
    }
}
