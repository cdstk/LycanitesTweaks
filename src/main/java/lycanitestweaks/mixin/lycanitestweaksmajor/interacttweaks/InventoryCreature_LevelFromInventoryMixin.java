package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
        if(this.creature instanceof TameableCreatureEntity && LycanitesEntityUtil.shouldLevelFromStack((TameableCreatureEntity) this.creature)){
            LycanitesEntityUtil.attemptLevelingFromStack((TameableCreatureEntity) this.creature, stackIncoming);
        }
    }
}
