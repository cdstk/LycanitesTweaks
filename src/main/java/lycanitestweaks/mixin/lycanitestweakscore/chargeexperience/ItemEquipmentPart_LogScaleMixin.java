package lycanitestweaks.mixin.lycanitestweakscore.chargeexperience;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.info.ModInfo;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEquipmentPart.class)
public abstract class ItemEquipmentPart_LogScaleMixin {

    @Shadow(remap = false) public abstract int getLevel(ItemStack itemStack);

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipmentPart_initXPBaseValues(ModInfo groupInfo, CallbackInfo ci){
        ItemEquipmentPart.BASE_LEVELUP_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.baseExperienceEquipment;
        ChargeItem.CHARGE_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.chargeExperienceValue;
    }

    @ModifyReturnValue(
            method = "getExperienceForNextLevel",
            at = @At("RETURN"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsItemEquipmentPart_getExperienceForNextLevelCalc(int original, ItemStack itemStack){
        return LycanitesEntityUtil.calculateExperienceForNextLevel(ItemEquipmentPart.BASE_LEVELUP_EXPERIENCE, this.getLevel(itemStack));
    }
}
