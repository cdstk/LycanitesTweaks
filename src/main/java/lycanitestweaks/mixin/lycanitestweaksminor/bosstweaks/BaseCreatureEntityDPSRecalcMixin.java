package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntityDPSRecalcMixin {

    // Calc at Living Damage Lowest
    @WrapWithCondition(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;onDamage(Lnet/minecraft/util/DamageSource;F)V", remap = false)
    )
    private boolean lycanitesTweaks_lycanitesBaseCreatureEntity_attackEntityFrom(BaseCreatureEntity instance, DamageSource damageSrc, float damage){
        // no op
        return false;
    }
}
