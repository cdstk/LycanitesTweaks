package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.lycanitesmobs.client.gui.CreatureInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CreatureInventoryScreen.class)
public abstract class CreatureInventoryScreen_AdvancedArmorMixin {

    @ModifyConstant(
            method = "drawSlots",
            constant = @Constant(stringValue = "chest"),
            remap = false
    )
    public String lycanitesTweaks_lycanitesMobs_CreatureInventoryScreen_drawSlotsChestplate(String constant){
        return "nochest";
    }
}
