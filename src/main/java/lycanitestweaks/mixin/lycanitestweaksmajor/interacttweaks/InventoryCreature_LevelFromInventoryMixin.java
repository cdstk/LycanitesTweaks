package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import com.lycanitesmobs.core.item.ChargeItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryCreature.class)
public abstract class InventoryCreature_LevelFromInventoryMixin {

    @Shadow(remap = false) public BaseCreatureEntity creature;

    @Inject(
            method = "setInventorySlotContents",
            at = @At("HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsInventoryCreature_setInventorySlotContentsConsumeCharges(int slotID, ItemStack stackIncoming, CallbackInfo ci){
        if(slotID < this.creature.getNoBagSize() || slotID >= this.creature.getBagSize()) return;
        if(creature.isTamed() && !creature.isTemporary && !creature.isPetType("minion")) {
            this.lycanitesTweaks$attemptLeveling(stackIncoming);
        }
    }


    /**
     * Based on Equipment Infusing, performs leveling if possible.
     */
    @Unique
    public void lycanitesTweaks$attemptLeveling(ItemStack itemStack) {
        if(itemStack == null || itemStack.isEmpty()) return;
        // Equipment Part:
        if(this.creature instanceof TameableCreatureEntity) {
            TameableCreatureEntity tameableCreature = (TameableCreatureEntity) this.creature;

            // Charge Experience:
            if (itemStack.getItem() instanceof ChargeItem) {
                if (tameableCreature.isLevelingChargeItem(itemStack)) {
                    while(!itemStack.isEmpty()) {
                        tameableCreature.addExperience(tameableCreature.getExperienceFromChargeItem(itemStack));
                        itemStack.shrink(1);
                    }
                }
            }
        }
    }
}
