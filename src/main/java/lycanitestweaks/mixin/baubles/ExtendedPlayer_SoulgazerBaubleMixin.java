package lycanitestweaks.mixin.baubles;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExtendedPlayer.class)
public abstract class ExtendedPlayer_SoulgazerBaubleMixin {

    @Shadow(remap = false)
    public abstract EntityPlayer getPlayer();

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;spiritRecharge:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsExtendedPlayer_onUpdateSpiritRegenBauble(int original){
        if(Helpers.hasSoulgazerEquiped(this.getPlayer(), true)) return original + ForgeConfigHandler.integrationConfig.soulgazerBaubleBonusRecharge;
        return original;
    }

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;summonFocusRecharge:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsExtendedPlayer_onUpdateFocusRegenBauble(int original){
        if(Helpers.hasSoulgazerEquiped(this.getPlayer(), true)) return original + ForgeConfigHandler.integrationConfig.soulgazerBaubleBonusRecharge;
        return original;
    }
}
