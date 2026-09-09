package lycanitestweaks.mixin.lycanitesmobspatches.core.assetmanager;

import com.lycanitesmobs.core.item.equipment.EquipmentPartManager;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentPartManager.class)
public abstract class EquipmentPartManager_ReloadFixMixin {

    @Inject(
            method = "reload",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEquipmentPartManager_reloadClearExisting(CallbackInfo ci){
        ItemEquipmentPart.MOB_PART_DROPS.clear();
    }
}
