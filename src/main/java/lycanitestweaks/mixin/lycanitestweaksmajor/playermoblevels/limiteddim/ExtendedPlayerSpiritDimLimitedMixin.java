package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels.limiteddim;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExtendedPlayer.class)
public abstract class ExtendedPlayerSpiritDimLimitedMixin {

    @Shadow(remap = false)
    public abstract EntityPlayer getPlayer();

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;spiritRecharge:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobs_onUpdateLimitedDimNoSpirit(int original){
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimNoSpiritRecharge && PlayerMobLevelsConfig.isDimensionLimitedMinion(this.getPlayer().dimension)) return 0;
        return original;
    }
}
