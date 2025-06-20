package lycanitestweaks.mixin.lycanitestweakscore.enchantedsoulkey;

import com.lycanitesmobs.core.container.EquipmentInfuserChargeSlot;
import com.lycanitesmobs.core.container.EquipmentInfuserContainer;
import com.lycanitesmobs.core.container.EquipmentInfuserPartSlot;
import lycanitestweaks.item.ItemEnchantedSoulkey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentInfuserContainer.class)
public abstract class EquipmentInfuserContainerSoulkeyMixin {

    @Shadow(remap = false)
    EquipmentInfuserChargeSlot chargeSlot;
    @Shadow(remap = false)
    EquipmentInfuserPartSlot partSlot;

    @Shadow(remap = false)
    public abstract void attemptInfusion();

    @Inject(
            method = "attemptInfusion",
            at = @At("HEAD"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEquipmentInfuserContainer_attemptInfusionSoulkey(CallbackInfo ci){
        if (!this.partSlot.getStack().isEmpty() && this.partSlot.getStack().getItem() instanceof ItemEnchantedSoulkey) {
            ItemEnchantedSoulkey equipmentPart = (ItemEnchantedSoulkey)this.partSlot.getStack().getItem();
            if (equipmentPart.isValidLevelingItem(this.chargeSlot.getStack())) {
                if (equipmentPart.getLevel(this.partSlot.getStack()) < equipmentPart.getMaxLevel(this.partSlot.getStack())) {
                    int experienceGained = equipmentPart.getExperienceFromChargeItem(this.chargeSlot.getStack());
                    equipmentPart.addExperience(this.partSlot.getStack(), experienceGained);
                    this.chargeSlot.decrStackSize(1);
                    this.attemptInfusion();
                }
            }
        }
    }
}
