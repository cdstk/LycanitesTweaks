package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.lycanitesmobs.core.container.BaseContainer;
import com.lycanitesmobs.core.container.CreatureContainer;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreatureContainer.class)
public abstract class CreatureContainer_AdvancedArmorMixin extends BaseContainer {


    @Inject(
            method = "drawCreatureEquipment",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/inventory/InventoryCreature;useAdvancedArmor()Z", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentAdvancedArmor(BaseCreatureEntity creature, int equipX, int equipY, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalIntRef drawX, @Local(argsOnly = true, ordinal = 1) LocalIntRef drawY){
        if(creature.inventory.useAdvancedArmor()){
            drawX.set(-18 * 4);
            drawY.set(0);
        }
    }

    @ModifyExpressionValue(
            method = "drawCreatureEquipment",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/container/CreatureContainer;addSlot(Lnet/minecraft/inventory/IInventory;III)Lnet/minecraft/inventory/Slot;", ordinal = 2),
            remap = false
    )
    public Slot lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentHelmet(Slot slot){
        slot.setBackgroundName("minecraft:items/empty_armor_slot_helmet");
        return slot;
    }

    @ModifyExpressionValue(
            method = "drawCreatureEquipment",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/container/CreatureContainer;addSlot(Lnet/minecraft/inventory/IInventory;III)Lnet/minecraft/inventory/Slot;", ordinal = 3),
            remap = false
    )
    public Slot lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentChestplate(Slot slot){
        slot.setBackgroundName("minecraft:items/empty_armor_slot_chestplate");
        return slot;
    }

    @ModifyExpressionValue(
            method = "drawCreatureEquipment",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/container/CreatureContainer;addSlot(Lnet/minecraft/inventory/IInventory;III)Lnet/minecraft/inventory/Slot;", ordinal = 4),
            remap = false
    )
    public Slot lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentLeggings(Slot slot){
        slot.setBackgroundName("minecraft:items/empty_armor_slot_leggings");
        return slot;
    }

    @ModifyExpressionValue(
            method = "drawCreatureEquipment",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/container/CreatureContainer;addSlot(Lnet/minecraft/inventory/IInventory;III)Lnet/minecraft/inventory/Slot;", ordinal = 5),
            remap = false
    )
    public Slot lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentBoots(Slot slot){
        slot.setBackgroundName("minecraft:items/empty_armor_slot_boots");
        return slot;    }

    @Inject(
            method = "drawCreatureEquipment",
            at = @At(value = "RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsCreatureContainer_drawCreatureEquipmentHands(BaseCreatureEntity creature, int equipX, int equipY, CallbackInfo ci){
        if(creature.inventory.useAdvancedArmor()){
            equipX = (-18 * 5);
            equipY = 0;
            this.addSlot(creature.inventory, creature.inventory.getSlotFromType("weapon"), equipX, equipY);
            equipY += 18;
            this.addSlot(creature.inventory, creature.inventory.getSlotFromType("offhand"), equipX, equipY).setBackgroundName("minecraft:items/empty_armor_slot_shield");
        }
    }
}
