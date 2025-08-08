package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_MainhandExclusiveInteractMixin {

    @Unique
    public boolean lycanitesTweaks$mainHandUsing = false;

    @Inject(
            method = "assessInteractCommand",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitestweaks_lycanitesMobsBaseCreatureEntity_assessInteractCommandMainhandUnlock(HashMap<Integer, String> commands, EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if(hand == EnumHand.OFF_HAND && lycanitesTweaks$mainHandUsing) {
            lycanitesTweaks$mainHandUsing = false;
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "assessInteractCommand",
            at = @At(value = "RETURN", ordinal = 2),
            remap = false
    )
    public void lycanitestweaks_lycanitesMobsBaseCreatureEntity_assessInteractCommandMainhandLock(HashMap<Integer, String> commands, EntityPlayer player, EnumHand hand, ItemStack itemStack, CallbackInfoReturnable<Boolean> cir){
        if(hand == EnumHand.MAIN_HAND) lycanitesTweaks$mainHandUsing = true;
    }
}
