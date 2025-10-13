package lycanitestweaks.mixin.lycanitestweaksmajor.player;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExtendedPlayer.class)
public abstract class ExtendedPlayer_SummoningAttributesMixin {

    @Shadow(remap = false)
    public abstract EntityPlayer getPlayer();

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;spiritRecharge:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsExtendedPlayer_onUpdateSpiritRegenAttribute(int original){
        IAttributeInstance playerAttribute = this.getPlayer().getAttributeMap().getAttributeInstanceByName(ForgeConfigHandler.majorFeaturesConfig.playerConfig.overrideSpiritRegen);
        if(playerAttribute != null) {
            return original + ForgeConfigHandler.majorFeaturesConfig.playerConfig.overrideSpiritRegenBase + (int) playerAttribute.getAttributeValue();
        }
        return original;
    }

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;summonFocusRecharge:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsExtendedPlayer_onUpdateFocusRegenAttribute(int original){
        IAttributeInstance playerAttribute = this.getPlayer().getAttributeMap().getAttributeInstanceByName(ForgeConfigHandler.majorFeaturesConfig.playerConfig.overrideFocusRegen);
        if(playerAttribute != null) {
            return original + ForgeConfigHandler.majorFeaturesConfig.playerConfig.overrideFocusRegenBase + (int) playerAttribute.getAttributeValue();
        }
        return original;
    }
}
