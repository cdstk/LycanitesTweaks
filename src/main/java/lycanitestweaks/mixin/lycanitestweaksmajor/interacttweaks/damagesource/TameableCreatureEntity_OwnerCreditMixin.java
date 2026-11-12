package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_OwnerCreditMixin extends BaseCreatureEntity {

    public TameableCreatureEntity_OwnerCreditMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "getDamageSource",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;isTamed()Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntity_getDamageSourceTameCredit(boolean original){
        return false;
    }
}
