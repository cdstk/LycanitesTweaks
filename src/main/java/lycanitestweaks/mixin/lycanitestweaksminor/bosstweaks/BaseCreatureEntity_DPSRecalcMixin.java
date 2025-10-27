package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_DPSRecalcMixin {

    // Calc at Living Damage Lowest
    @WrapMethod(
            method = "onDamage",
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onDamageRecalc(DamageSource damageSrc, float damage, Operation<Void> original){
        if(!ForgeConfigHandler.minorFeaturesConfig.bossDPSLimitRecalc) original.call(damageSrc, damage);
        // no op
    }
}
