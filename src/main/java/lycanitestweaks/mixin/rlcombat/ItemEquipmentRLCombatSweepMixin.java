package lycanitestweaks.mixin.rlcombat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipmentRLCombatSweepMixin extends ItemBase {

    @ModifyExpressionValue(
            method = "hitEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;getDamageSweep(Lnet/minecraft/item/ItemStack;)D", remap = false)
    )
    private double lycanitesTweaks_lycanitesItemEquipment_hitEntity(double original){
        return 0.0D;
    }
}
