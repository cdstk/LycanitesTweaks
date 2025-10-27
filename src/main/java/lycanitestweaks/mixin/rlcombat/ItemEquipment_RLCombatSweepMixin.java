package lycanitestweaks.mixin.rlcombat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_RLCombatSweepMixin extends ItemBase {

    @ModifyExpressionValue(
            method = "hitEntity",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z")
    )
    private boolean lycanitesTweaks_lycanitesItemEquipment_hitEntityOriginalSweep(boolean original){
        return true;
    }
}
