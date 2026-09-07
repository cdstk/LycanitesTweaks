package lycanitestweaks.mixin.vanilla.pickupfix;

import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemChorusFruit.class)
public abstract class ItemChorusFruit_PickupMixin {

    @Inject(
            method = "onItemUseFinish",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isRiding()Z")
    )
    private void lycanitesTweaks_vanillaItemChorusFruit_onItemUseFinishDropPickup(ItemStack stack, World worldIn, EntityLivingBase entityLiving, CallbackInfoReturnable<ItemStack> cir){
        LycanitesMobsWrapper.dropPickedUpBy(entityLiving);
    }
}
