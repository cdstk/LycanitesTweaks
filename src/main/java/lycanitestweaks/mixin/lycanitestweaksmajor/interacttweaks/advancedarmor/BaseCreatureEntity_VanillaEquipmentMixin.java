package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_VanillaEquipmentMixin extends EntityLiving {

    @Shadow(remap = false) public InventoryCreature inventory;

    public BaseCreatureEntity_VanillaEquipmentMixin(World world) {
        super(world);
    }

    @Inject(
            method = "setCurrentItemOrArmor",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/inventory/InventoryCreature;setEquipmentStack(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_setCurrentItemOrArmorOffhand(int slot, ItemStack itemStack, CallbackInfo ci, @Local LocalRef<String> type){
        if(slot == 5) type.set("offhand");
    }

    @ModifyExpressionValue(
            method = "getMeleeCooldown",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D", remap = true),
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getMeleeCooldownMinimum(double attackSpeed){
        return Math.max(1, attackSpeed);
    }

    @Unique
    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        if(this.inventory != null){
            switch (slotIn){
                case MAINHAND: return this.inventory.getEquipmentStack("weapon");
                case OFFHAND: return this.inventory.getEquipmentStack("offhand");
                case HEAD: return this.inventory.getEquipmentStack("head");
                case CHEST: return this.inventory.getEquipmentStack("chest");
                case LEGS: return this.inventory.getEquipmentStack("legs");
                case FEET: return this.inventory.getEquipmentStack("feet");
                default:
                    return ItemStack.EMPTY;
            }
        }
        else return super.getItemStackFromSlot(slotIn);
    }
}
