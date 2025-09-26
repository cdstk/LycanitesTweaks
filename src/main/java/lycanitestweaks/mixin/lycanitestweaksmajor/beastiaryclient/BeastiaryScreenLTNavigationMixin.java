package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.BaseScreen;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.core.info.CreatureInfo;
import lycanitestweaks.client.gui.beastiary.AltarsBeastiaryScreen;
import lycanitestweaks.network.PacketExtendedPlayerSelectedCreature;
import lycanitestweaks.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeastiaryScreen.class)
public abstract class BeastiaryScreenLTNavigationMixin extends BaseScreen {

    @Shadow(remap = false)
    public EntityPlayer player;

    @Inject(
            method = "initControls",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBeastiaryScreen_initControlsLTNavigation(CallbackInfo ci,
                  @Local(ordinal = 2) int menuY,
                  @Local(ordinal = 6) int buttonX,
                  @Local(ordinal = 7) int buttonWidth,
                  @Local(ordinal = 8) int buttonWidthPadded,
                  @Local(ordinal = 9) int buttonHeight){
        menuY += 30;
        this.buttonList.add(new GuiButton(AltarsBeastiaryScreen.BEASTIARY_ALTAR_ID, buttonX + (buttonWidthPadded * (this.buttonList.size() - 5)), menuY, buttonWidth, buttonHeight, I18n.format("gui.beastiary.altars")));
    }

    @Inject(
            method = "actionPerformed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/gui/BaseScreen;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V")
    )
    public void lycanitesTweaks_lycanitesMobsBeastiaryScreen_actionPerformedLTNavigation(GuiButton guiButton, CallbackInfo ci) {
        if (guiButton != null) {
            if (guiButton.id == AltarsBeastiaryScreen.BEASTIARY_ALTAR_ID) {
                AltarsBeastiaryScreen.openToPlayer(this.player);
            }
        }
    }

    @Inject(
            method = "onCreateDisplayEntity",
            at = @At("HEAD"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBeastiaryScreen_onCreateDisplayEntitySendPacket(CreatureInfo creatureInfo, EntityLivingBase entity, CallbackInfo ci){
        PacketHandler.instance.sendToServer(new PacketExtendedPlayerSelectedCreature(creatureInfo, entity));
    }
}
