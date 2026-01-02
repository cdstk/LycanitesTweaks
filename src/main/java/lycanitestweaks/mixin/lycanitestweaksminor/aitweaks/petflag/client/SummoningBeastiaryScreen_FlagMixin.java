package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.petflag.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.SummoningBeastiaryScreen;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.util.ISummonSet_TargetFlagMixin;
import lycanitestweaks.util.ITameableCreatureEntity_TargetFlagMixin;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SummoningBeastiaryScreen.class)
public abstract class SummoningBeastiaryScreen_FlagMixin extends BeastiaryScreen {

    @Shadow(remap = false) private int petCommandIdStart;

    public SummoningBeastiaryScreen_FlagMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(
            method = "initControls",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummoningBeastiaryScreen_initControlsPetFlags(CallbackInfo ci,
                     @Local(name = "buttonWidth") int buttonWidth,
                     @Local(name = "buttonHeight") int buttonHeight,
                     @Local(name = "buttonSpacing") int buttonSpacing,
                     @Local(name = "buttonX") int buttonX,
                     @Local(name = "buttonY") int buttonY
    ){
        buttonY -= buttonHeight + 2;
        this.buttonList.add(new GuiButton(ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_TARGET_BOSS + this.petCommandIdStart, buttonX, buttonY, buttonWidth, buttonHeight, I18n.format("gui.pet.boss")));
        buttonX += buttonWidth + buttonSpacing;
        this.buttonList.add(new GuiButton(ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_DO_GRIEF + this.petCommandIdStart, buttonX, buttonY, buttonWidth, buttonHeight, I18n.format("gui.pet.grief")));
    }

    @Inject(
            method = "updateControls",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity$PET_COMMAND_ID;SIT:Lcom/lycanitesmobs/core/entity/BaseCreatureEntity$PET_COMMAND_ID;"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSummoningBeastiaryScreen_updateControlsPetFlags(int mouseX, int mouseY, float partialTicks, CallbackInfo ci, @Local GuiButton button){
        if(this.playerExt.getSelectedSummonSet() instanceof ISummonSet_TargetFlagMixin){
            if (button.id == ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_TARGET_BOSS + this.petCommandIdStart) {
                if (((ISummonSet_TargetFlagMixin) this.playerExt.getSelectedSummonSet()).lycanitesTweaks$shouldTargetBoss()) {
                    button.displayString = I18n.format("gui.pet.boss") + ": " + I18n.format("common.yes");
                }
                else {
                    button.displayString = I18n.format("gui.pet.boss") + ": " + I18n.format("common.no");
                }
            }
            else if (button.id == ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_DO_GRIEF + this.petCommandIdStart) {
                if (((ISummonSet_TargetFlagMixin) this.playerExt.getSelectedSummonSet()).lycanitesTweaks$shouldDoGrief()) {
                    button.displayString = I18n.format("gui.pet.grief") + ": " + I18n.format("common.yes");
                }
                else {
                    button.displayString = I18n.format("gui.pet.grief") + ": " + I18n.format("common.no");
                }
            }
        }
    }

    @Inject(
            method = "actionPerformed",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/ExtendedPlayer;sendSummonSetToServer(B)V", remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsSummoningBeastiaryScreen_actionPerformedPetFlags(GuiButton button, CallbackInfo ci, @Local SummonSet summonSet, @Local int petCommandId){
        if(summonSet instanceof ISummonSet_TargetFlagMixin){
            if (petCommandId == ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_TARGET_BOSS) {
                ((ISummonSet_TargetFlagMixin) summonSet).lycanitesTweaks$setTargetBoss(!((ISummonSet_TargetFlagMixin) summonSet).lycanitesTweaks$shouldTargetBoss());
            }
            if (petCommandId == ITameableCreatureEntity_TargetFlagMixin.PET_COMMAND_DO_GRIEF) {
                ((ISummonSet_TargetFlagMixin) summonSet).lycanitesTweaks$setDoGrief(!((ISummonSet_TargetFlagMixin) summonSet).lycanitesTweaks$shouldDoGrief());
            }
        }
    }
}
