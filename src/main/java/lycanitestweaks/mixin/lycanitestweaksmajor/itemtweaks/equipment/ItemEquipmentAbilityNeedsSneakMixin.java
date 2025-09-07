package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipmentAbilityNeedsSneakMixin {

    @Shadow(remap = false)
    public abstract void onItemLeftClick(World world, EntityPlayer player, EnumHand hand);

    /*
    *   Original Behavior if Mainhand and no Offhand
    *   If Offhand, require sneak
    *   If Mainhand with offhand, require sneak (enables offhand like shields now)
     */
    @Inject(
            method = "onItemRightClick",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;getMana(Lnet/minecraft/item/ItemStack;)I", remap = false),
            cancellable = true
    )
    private void lycanitesTweaks_lycanitesItemEquipment_onItemRightClick(World world, EntityPlayer player, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir, @Local ItemStack itemStack){
        if(hand == EnumHand.OFF_HAND){
            if(!player.isSneaking()) {
                this.onItemLeftClick(world, player, EnumHand.OFF_HAND);
                cir.setReturnValue(new ActionResult<>(EnumActionResult.FAIL, itemStack));
            }
        }
        else if(hand == EnumHand.MAIN_HAND && player.getHeldItem(EnumHand.OFF_HAND).getItem() != ItemStack.EMPTY.getItem() && !player.isSneaking()) cir.setReturnValue(new ActionResult<>(EnumActionResult.FAIL, itemStack));
    }
}
