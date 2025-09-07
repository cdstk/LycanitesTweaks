package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.equipment;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_HarvestEnchantmentsMixin extends ItemBase {

    @Unique
    private boolean lycanitesTweaks$isHarvestLooping = false;

    @Inject(
            method = "onBlockDestroyed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipment;getFeaturesByType(Lnet/minecraft/item/ItemStack;Ljava/lang/String;)Ljava/util/List;", remap = false),
            cancellable = true
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipment_onBlockDestroyedHarvestMutexLock(ItemStack itemStack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase entityLiving, CallbackInfoReturnable<Boolean> cir){
        if(lycanitesTweaks$isHarvestLooping) cir.setReturnValue(super.onBlockDestroyed(itemStack, worldIn, blockState, pos, entityLiving));
        lycanitesTweaks$isHarvestLooping = true;
    }

        @Inject(
            method = "onBlockDestroyed",
            at = @At(value = "RETURN", ordinal = 2)
    )
    public void lycanitesTweaks_lycanitesMobsItemEquipment_onBlockDestroyedHarvestMutexUnlock(ItemStack itemStack, World worldIn, IBlockState blockState, BlockPos pos, EntityLivingBase entityLiving, CallbackInfoReturnable<Boolean> cir){
        lycanitesTweaks$isHarvestLooping = false;
    }
}
