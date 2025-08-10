package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import lycanitestweaks.util.IBaseCreatureEntity_VanillaEquipmentMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryCreature.class)
public abstract class InventoryCreature_AdvancedArmorMixin {

    @Shadow(remap = false)
    protected NonNullList<ItemStack> inventoryContents;
    @Shadow(remap = false)
    protected abstract void addEquipmentSlot(String type);
    @Shadow(remap = false)
    public abstract void setAdvancedArmor(boolean advanced);
    @Shadow
    public abstract int getSizeInventory();

    @Inject(
            method = "<init>",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsInventoryCreature_initMoreEquipmentSlots(String inventoryName, BaseCreatureEntity creature, CallbackInfo ci){
        this.setAdvancedArmor(true);
    }

    @Inject(
            method = "setAdvancedArmor",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsInventoryCreature_setAdvancedArmorSetContents(boolean advanced, CallbackInfo ci){
        if(advanced){
            this.addEquipmentSlot("weapon");
            this.addEquipmentSlot("offhand");
        }
        this.inventoryContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
    }

    @Inject(
            method = "isEquipmentValidForSlot",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsInventoryCreature_isEquipmentValidForSlotHandItems(String type, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if(type.equals("weapon") || type.equals("offhand")) cir.setReturnValue(true);
    }

    @Inject(
            method = "getEquipmentDataParameter",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void lycanitesTweaks_lycanitesMobsInventoryCreature_getEquipmentDataParameterHandItems(String type, CallbackInfoReturnable<DataParameter<ItemStack>> cir){
        if(type.equals("weapon")) cir.setReturnValue(IBaseCreatureEntity_VanillaEquipmentMixin.handDataParameters.get("weapon"));
        if(type.equals("offhand")) cir.setReturnValue(IBaseCreatureEntity_VanillaEquipmentMixin.handDataParameters.get("offhand"));
    }
}
