package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.BaseContainerScreen;
import com.lycanitesmobs.client.gui.CreatureInventoryScreen;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreatureInventoryScreen.class)
public abstract class CreatureInventoryScreen_FlagMixin extends BaseContainerScreen {

    public CreatureInventoryScreen_FlagMixin(Container container) {
        super(container);
    }

    @Inject(
            method = "drawControls",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 4),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsCreatureInventoryScreen_drawControlsPetFlags(int backX, int backY, CallbackInfo ci,
                     @Local TameableCreatureEntity pet,
                     @Local(name = "buttonSpacing") int buttonSpacing,
                     @Local(name = "buttonWidth") int buttonWidth,
                     @Local(name = "buttonHeight") int buttonHeight,
                     @Local(name = "buttonY") int buttonY
    ){
        String buttonText = I18n.format("gui.pet.boss") + ": "
                + (pet instanceof ITameableCreatureEntity_TargetFlagMixin && ((ITameableCreatureEntity_TargetFlagMixin) pet).lycanitesTweaks$shouldTargetBoss()
                ? I18n.format("common.yes")
                : I18n.format("common.no"));
        this.buttonList.add(new GuiButton(ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_TARGET_BOSS, backX - buttonWidth - buttonSpacing, buttonY, buttonWidth, buttonHeight, buttonText));

        buttonText = I18n.format("gui.pet.grief") + ": "
                + (pet instanceof ITameableCreatureEntity_TargetFlagMixin && ((ITameableCreatureEntity_TargetFlagMixin) pet).lycanitesTweaks$shouldDoGrief()
                ? I18n.format("common.yes")
                : I18n.format("common.no"));
        buttonY += buttonHeight + (buttonSpacing * 2);
        this.buttonList.add(new GuiButton(ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_DO_GRIEF, backX - buttonWidth - buttonSpacing, buttonY, buttonWidth, buttonHeight, buttonText));
    }
}
