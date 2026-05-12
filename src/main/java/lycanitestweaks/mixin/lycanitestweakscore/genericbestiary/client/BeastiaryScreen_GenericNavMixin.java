package lycanitestweaks.mixin.lycanitestweakscore.genericbestiary.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.BaseScreen;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import lycanitestweaks.client.gui.beastiary.GenericEntityBestiaryScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeastiaryScreen.class)
public abstract class BeastiaryScreen_GenericNavMixin extends BaseScreen {

    @Shadow(remap = false)
    public EntityPlayer player;

    @Inject(
            method = "initControls",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBeastiaryScreen_initControlsGenericNav(CallbackInfo ci,
                  @Local(ordinal = 2) int menuY,
                  @Local(ordinal = 6) int buttonX,
                  @Local(ordinal = 7) int buttonWidth,
                  @Local(ordinal = 8) int buttonWidthPadded,
                  @Local(ordinal = 9) int buttonHeight){
        menuY += buttonHeight + 5;
        this.buttonList.add(new GuiButton(GenericEntityBestiaryScreen.BEASTIARY_GENERIC_ID, buttonX + (buttonWidthPadded * (this.buttonList.size() - 5)), menuY, buttonWidth, buttonHeight, I18n.format("gui.beastiary.bestiary")));
    }

    @Inject(
            method = "actionPerformed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/gui/BaseScreen;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V")
    )
    public void lycanitesTweaks_lycanitesMobsBeastiaryScreen_actionPerformedLTNavigation(GuiButton guiButton, CallbackInfo ci) {
        if (guiButton != null) {
            if (guiButton.id == GenericEntityBestiaryScreen.BEASTIARY_GENERIC_ID) {
                GenericEntityBestiaryScreen.openToPlayer(this.player);
            }
        }
    }
}
