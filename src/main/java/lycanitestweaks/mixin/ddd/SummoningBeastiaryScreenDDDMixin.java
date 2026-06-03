package lycanitestweaks.mixin.ddd;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.SummoningBeastiaryScreen;
import lycanitestweaks.client.gui.beastiary.lists.DDDDescriptionList;
import lycanitestweaks.client.gui.buttons.RenderToggleButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummoningBeastiaryScreen.class)
public abstract class SummoningBeastiaryScreenDDDMixin extends BeastiaryScreen {

    @Unique
    public DDDDescriptionList lycanitesTweaks$dddDescriptionList;
    @Unique
    public RenderToggleButton lycanitesTweaks$descListToggle;

    public SummoningBeastiaryScreenDDDMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(
            method = "initControls",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSummoningBeastiaryScreen_initControlsDDD(CallbackInfo ci){
        int menuPadding = 6;
        int menuX = this.centerX - Math.round((float)this.windowWidth / 2) + menuPadding;
        int menuWidth = this.windowWidth - (menuPadding * 2);

        int buttonCount = 5;
        int buttonPadding = 2;
        int buttonX = menuX + buttonPadding;
        int buttonWidth = Math.round((float)(menuWidth / buttonCount)) - (buttonPadding * 2);
        int buttonWidthPadded = buttonWidth + (buttonPadding * 2);

        int height = (int) (DDDDescriptionList.LIST_WIDTH / 3.25);
        int top = (int) (this.colRightCenterY * 0.75);
        int bottom = top + height;
        int xPos = (int) (this.colRightCenterX * 0.7);

        lycanitesTweaks$descListToggle = new RenderToggleButton(RenderToggleButton.BUTTON_ID, buttonX + (buttonWidthPadded * 4), this.colRightY , 80, 20, I18n.format("gui.beastiary.creatures.mixin.ddd"));
        this.buttonList.add(lycanitesTweaks$descListToggle);
        lycanitesTweaks$dddDescriptionList = new DDDDescriptionList(this, DDDDescriptionList.LIST_WIDTH, height, top, bottom, xPos, lycanitesTweaks$descListToggle);
    }

    @Inject(
            method = "drawForeground",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSummoningBeastiaryScreen_drawForegroundDDD(int mouseX, int mouseY, float partialTicks, CallbackInfo ci){
        if(this.creaturePreviewEntity != null) {
            this.lycanitesTweaks$descListToggle.visible = true;
            this.lycanitesTweaks$dddDescriptionList.drawScreen(mouseX, mouseY, partialTicks);
        }
        else
            this.lycanitesTweaks$descListToggle.visible = false;
    }
}
