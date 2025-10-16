package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelper_PartsEnchantmentsMixin {

    @ModifyExpressionValue(
            method = "getEnchantments",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getEnchantmentTagList()Lnet/minecraft/nbt/NBTTagList;")
    )
    private static NBTTagList lycanitesTweaks_vanillaEnchantmentHelper_getEnchantmentsEquipmentPart(NBTTagList original, @Local(argsOnly = true) ItemStack stack){
        if(stack.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants) return ItemEnchantedBook.getEnchantments(stack);
        return original;
    }

    @Inject(
            method = "setEnchantments",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagList;appendTag(Lnet/minecraft/nbt/NBTBase;)V")
    )
    private static void lycanitesTweaks_vanillaEnchantmentHelper_setEnchantmentsAddedForEquipmentPart(Map<Enchantment, Integer> enchMap, ItemStack stack, CallbackInfo ci, @Local Enchantment enchantment, @Local int i){
        if(stack.getItem() instanceof ItemEquipmentPart  && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants) ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(enchantment, i));
    }

    @WrapWithCondition(
            method = "setEnchantments",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V")
    )
    private static boolean lycanitesTweaks_vanillaEnchantmentHelper_setEnchantmentsEquipmentPart(ItemStack instance, String tagName, NBTBase nbttaglist){
        return !(instance.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants);
    }

    @WrapOperation(
            method = "addRandomEnchantment",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V")
    )
    private static void lycanitesTweaks_vanillaEnchantmentHelper_addRandomEnchantmentEquipmentPart(ItemStack instance, Enchantment enchantment, int enchantmentLevel, Operation<Void> original, @Local EnchantmentData enchantmentdata){
        if(instance.getItem() instanceof ItemEquipmentPart && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.partsStoreEnchants){
            ItemEnchantedBook.addEnchantment(instance, enchantmentdata);
        }
        else {
            original.call(instance, enchantment, enchantmentLevel);
        }
    }
}
