package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.summonstafflevelmap;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.item.temp.ItemStaffSavage;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStaffSavage.class)
public abstract class ItemStaffSavage_LevelMapItemMixin extends ItemStaffSummoning {

    public ItemStaffSavage_LevelMapItemMixin(String itemName, String textureName) {
        super(itemName, textureName);
    }

    // Call the IStoredElementLevelMapItemMixin level scaling
    @Inject(
            method = "applyMinionEffects",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemStaffSummoning_applyMinionEffectsLevelMap(BaseCreatureEntity minion, CallbackInfo ci){
        super.applyMinionEffects(minion);
    }
}
