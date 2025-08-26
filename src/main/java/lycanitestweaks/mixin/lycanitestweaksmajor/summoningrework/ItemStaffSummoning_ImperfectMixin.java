package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.util.IObjectImperfectSummoning_Mixin;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStaffSummoning.class)
public abstract class ItemStaffSummoning_ImperfectMixin implements IObjectImperfectSummoning_Mixin {

    @Inject(
            method = "applyMinionBehaviour",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemStaffSummoning_applyMinionBehaviourImperfect(TameableCreatureEntity minion, EntityPlayer player, CallbackInfo ci){
        this.lycanitesTweaks$applyImperfectSummoning(minion, player, true);
    }
}
