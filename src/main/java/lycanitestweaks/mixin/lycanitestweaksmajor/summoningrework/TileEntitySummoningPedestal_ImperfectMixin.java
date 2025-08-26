package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.tileentity.TileEntitySummoningPedestal;
import lycanitestweaks.util.IObjectImperfectSummoning_Mixin;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntitySummoningPedestal.class)
public abstract class TileEntitySummoningPedestal_ImperfectMixin implements IObjectImperfectSummoning_Mixin {

    @Shadow(remap = false) public abstract EntityPlayer getPlayer();

    @Inject(
            method = "applyMinionBehaviour",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsTileEntitySummoningPedestal_applyMinionBehaviourImperfect(TameableCreatureEntity minion, CallbackInfo ci){
        this.lycanitesTweaks$applyImperfectSummoning(minion, this.getPlayer() , false);
    }
}
