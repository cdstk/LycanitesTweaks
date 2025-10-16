package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.EquipmentUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_EnchantmentsMixin extends ItemBase {

    @Shadow(remap = false)
    public abstract NonNullList<ItemStack> getEquipmentPartStacks(ItemStack itemStack);
    @Shadow(remap = false)
    public abstract ItemEquipmentPart getEquipmentPart(ItemStack itemStack);
    @Shadow(remap = false)
    public abstract boolean removeSharpness(ItemStack equipmentStack, int sharpness);

    @Shadow(remap = false) public static int PART_LIMIT;

    @WrapWithCondition(
            method = "onItemUse",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;removeSharpness(Lnet/minecraft/item/ItemStack;I)Z", remap = false)
    )
    public boolean lycanitesTweaks_lycanitesMobsItemEquipment_onItemUseUnbreaking(ItemEquipment instance, ItemStack equipmentPartStack, int i, @Local(argsOnly = true) EntityPlayer player){
        lycanitesTweaks$removeSharpnessWithUnbreaking(equipmentPartStack, i, player.getRNG());
        return false;
    }

    @WrapWithCondition(
            method = "itemInteractionForEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;removeSharpness(Lnet/minecraft/item/ItemStack;I)Z", remap = false)
    )
    public boolean lycanitesTweaks_lycanitesMobsItemEquipment_itemInteractionForEntityUnbreaking(ItemEquipment instance, ItemStack equipmentPartStack, int i, @Local(argsOnly = true) EntityPlayer player){
        lycanitesTweaks$removeSharpnessWithUnbreaking(equipmentPartStack, i, player.getRNG());
        return false;
    }

    @WrapWithCondition(
            method = "onBlockDestroyed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;removeSharpness(Lnet/minecraft/item/ItemStack;I)Z", remap = false)
    )
    public boolean lycanitesTweaks_lycanitesMobsItemEquipment_onBlockDestroyedUnbreaking(ItemEquipment instance, ItemStack equipmentPartStack, int i, @Local(argsOnly = true) EntityLivingBase entityLiving){
        lycanitesTweaks$removeSharpnessWithUnbreaking(equipmentPartStack, i, entityLiving.getRNG());
        return false;
    }

    @WrapWithCondition(
            method = "hitEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;removeSharpness(Lnet/minecraft/item/ItemStack;I)Z", remap = false)
    )
    public boolean lycanitesTweaks_lycanitesMobsItemEquipment_hitEntityUnbreaking(ItemEquipment instance, ItemStack equipmentPartStack, int i, @Local(argsOnly = true, ordinal = 1) EntityLivingBase attacker){
        lycanitesTweaks$removeSharpnessWithUnbreaking(equipmentPartStack, i, attacker.getRNG());
        return false;
    }

    @ModifyExpressionValue(
            method = "getAttributeModifiers",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/ItemBase;getItemAttributeModifiers(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lcom/google/common/collect/Multimap;")
    )
    public Multimap<String, AttributeModifier> lycanitesTweaks_lycanitesMobsItemEquipment_getAttributeModifiersWithStack(Multimap<String, AttributeModifier> original, EntityEquipmentSlot slot, ItemStack itemStack){
        return super.getAttributeModifiers(slot, itemStack);
    }

    @Inject(
            method = "addEquipmentPart",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipment_addEquipmentPartStoredEnchantments(ItemStack equipmentStack, ItemStack equipmentPartStack, int slotIndex, CallbackInfo ci){
        if(slotIndex >= PART_LIMIT) return;

        // Simplified ContainerRepair.updateRepairOutput()
        if(equipmentStack.isEmpty() || equipmentPartStack.isEmpty()) return;

        Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.getEnchantments(equipmentStack);
        Map<Enchantment, Integer> possibleEnchantments = EnchantmentHelper.getEnchantments(equipmentPartStack);

        for(Enchantment possible : possibleEnchantments.keySet()){
            int currentLevel = currentEnchantments.getOrDefault(possible, 0);
            int possibleLevel = possibleEnchantments.get(possible);
            if(currentLevel > 0) {
                currentEnchantments.put(possible, Math.max(currentLevel, possibleLevel));
            }
            else {
                boolean canApplyPossible = true;
                for(Enchantment current : currentEnchantments.keySet()){
                    if(!possible.isCompatibleWith(current)){
                        canApplyPossible = false;
                        break;
                    }
                }
                if(canApplyPossible) currentEnchantments.put(possible, possibleLevel);
            }
        }
        EnchantmentHelper.setEnchantments(currentEnchantments, equipmentStack);
    }

    @Override
    @Unique
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    // Enchantment Table
    @Override
    @Unique
    public int getItemEnchantability(@Nonnull ItemStack stack){
        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantability == -1) return 1;
        int value = 0;
        for(ItemStack equipmentPartStack : this.getEquipmentPartStacks(stack)) {
            ItemEquipmentPart equipmentPart = this.getEquipmentPart(equipmentPartStack);
            if (equipmentPart != null) {
                value += (equipmentPart.getLevel(equipmentPartStack) * ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantability);
            }
        }
        return value;
    }

    // Used by both Enchantment Table and Anvil
    @Override
    @Unique
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack, @Nonnull Enchantment enchantment){
        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants) return super.canApplyAtEnchantingTable(stack, enchantment);

        List<ItemEquipmentPart> equipmentParts = this.getEquipmentPartStacks(stack)
                .stream()
                .map(this::getEquipmentPart)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return EquipmentUtil.canApplyEnchantmentToParts(stack, enchantment, equipmentParts) || super.canApplyAtEnchantingTable(stack, enchantment);
    }

    /**
     * Decreases the Sharpness of this Equipment by decreasing the Sharpness of all parts.
     * @param equipmentStack The itemStack of the Equipment.
     */
    @Unique
    public void lycanitesTweaks$removeSharpnessWithUnbreaking(ItemStack equipmentStack, int sharpnessCost, Random rand) {
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, equipmentStack);
        int costReduction = 0;

        for (int costPoint = 0; enchantmentLevel > 0 && costPoint < sharpnessCost; ++costPoint) {
            if (EnchantmentDurability.negateDamage(equipmentStack, enchantmentLevel, rand)) {
                ++costReduction;
            }
        }

        sharpnessCost -= costReduction;
        if (sharpnessCost <= 0) sharpnessCost = 0;

        this.removeSharpness(equipmentStack, sharpnessCost);
    }
}
