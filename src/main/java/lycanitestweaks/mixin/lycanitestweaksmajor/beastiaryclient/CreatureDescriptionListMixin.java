package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureDescriptionList;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureKnowledge;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreatureDescriptionList.class)
public abstract class CreatureDescriptionListMixin {

    @Shadow(remap = false)
    protected BeastiaryScreen parentGui;
    @Shadow(remap = false)
    public CreatureKnowledge creatureKnowledge;

    @ModifyReturnValue(
            method = "getContent",
            at = @At(value = "RETURN", ordinal = 2),
            remap = false
    )
    public String lycanitesTweaks_lycanitesMobsCreatureDescriptionList_getContentPML(String original){
        if(!ForgeConfigHandler.clientFeaturesMixinConfig.beastiaryGUIPML || !ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability) return original;

        if(this.parentGui.playerExt.getBeastiary().hasKnowledgeRank(this.creatureKnowledge.creatureName, 2)){
            StringBuilder textBuilder = new StringBuilder();

            if(CreatureManager.getInstance().config.startingLevelMax > CreatureManager.getInstance().config.startingLevelMin){
                textBuilder.append(I18n.format("gui.beastiary.creatures.mixin.pml.range",
                        CreatureManager.getInstance().config.startingLevelMin,
                        CreatureManager.getInstance().config.startingLevelMax));
            }
            else{
                int timeLevel = (int)(CreatureManager.getInstance().config.levelPerLocalDifficulty * 6.75D);
                if(CreatureManager.getInstance().config.levelPerDay > 0) timeLevel += CreatureManager.getInstance().config.levelPerDayMax;
                textBuilder.append(I18n.format("gui.beastiary.creatures.mixin.pml.time", timeLevel));
            }
            textBuilder.append("\n\n").append(original);
            return textBuilder.toString();
        }
        return original;
    }

    @Inject(
            method = "getContent",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;isMountable()Z"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsCreatureDescriptionList_getContentHealingFoodTame(CallbackInfoReturnable<String> cir, @Local LocalRef<String> text){
        if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.tameWithHealingFood && this.parentGui.creaturePreviewEntity instanceof BaseCreatureEntity &&
                !(LycanitesEntityUtil.isPracticallyFlying((BaseCreatureEntity) this.parentGui.creaturePreviewEntity)
                        && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.tamedWithFoodAllowFlying)){
            StringBuilder textBuilder = new StringBuilder(text.get());
            textBuilder.deleteCharAt(textBuilder.lastIndexOf("\n"));
            textBuilder.append(I18n.format("gui.beastiary.creatures.mixin.interact.diet")).append("\n\n");
            text.set(textBuilder.toString());
        }
    }

    @Inject(
            method = "getContent",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/CreatureInfo;isSummonable()Z", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsCreatureDescriptionList_getContentVanillaSaddle(CallbackInfoReturnable<String> cir, @Local CreatureInfo creatureInfo, @Local LocalRef<String> text){
        if(creatureInfo.isMountable())
            if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.mountVanillaSaddleLimited && this.parentGui.creaturePreviewEntity instanceof BaseCreatureEntity &&
                    !(LycanitesEntityUtil.isPracticallyFlying((BaseCreatureEntity) this.parentGui.creaturePreviewEntity)
                            && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleAllowFlying)){
                StringBuilder textBuilder = new StringBuilder(text.get());
                textBuilder.deleteCharAt(textBuilder.lastIndexOf("\n"));
                textBuilder.append(I18n.format("gui.beastiary.creatures.mixin.interact.saddle", ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleLevelRequirement)).append("\n\n");
                text.set(textBuilder.toString());
            }
    }
}
