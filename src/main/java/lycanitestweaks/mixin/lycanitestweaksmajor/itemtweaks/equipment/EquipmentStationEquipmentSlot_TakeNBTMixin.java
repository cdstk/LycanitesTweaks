package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.container.EquipmentForgeSlot;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EquipmentForgeSlot.class)
public abstract class EquipmentStationEquipmentSlot_TakeNBTMixin {

    @Inject(
            method = "onTake",
            at = @At("TAIL")
    )
    public void lycanitesTweaks_lycanitesMobsEquipmentForgeSlot_onTakeDummyNBT(EntityPlayer player, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir, @Local Item item){
        if (!ModLoadedUtil.isQualityToolsLoaded() || !(item instanceof ItemEquipment)) return;
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        if (itemStack.getTagCompound().hasKey("Quality")) return;

        itemStack.getTagCompound().setTag("Quality", new NBTTagCompound());
    }
}
