package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.ElementDescriptionList;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.Helpers;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ElementDescriptionList.class)
public abstract class ElementDescriptionListMixin {

    @Unique
    private int lycanitesTweaks$cachePML = 0;

    @Shadow(remap = false)
    protected BeastiaryScreen parentGui;
    @Shadow(remap = false)
    public ElementInfo elementInfo;

    @ModifyReturnValue(
            method = "getContent",
            at = @At(value = "RETURN", ordinal = 1),
            remap = false
    )
    public String lycanitesTweaks_lycanitesMobsElementDescriptionList_getContentPML(String text){
        if(this.parentGui.player != null && this.parentGui.playerExt != null){
            StringBuilder textBuilder = new StringBuilder(text);
            textBuilder.append("\n\n").append(I18n.format("gui.beastiary.elements.mixin.creatures"));
            if(Helpers.getCreatureElementsMap().containsKey(this.elementInfo.name)) {
                for (String creatureName : Helpers.getCreatureElementsMap().get(this.elementInfo.name)) {
                    if (this.parentGui.playerExt.beastiary.hasKnowledgeRank(creatureName, 1))
                        textBuilder.append("\n").append(I18n.format("gui.beastiary.elements.mixin.creatures.description",
                                CreatureManager.getInstance().creatures.get(creatureName).getTitle(),
                                this.parentGui.playerExt.beastiary.getCreatureKnowledge(creatureName).rank));
                }
                List<ElementInfo> elementList = new ArrayList<>();
                elementList.add(this.elementInfo);
                if(ForgeConfigHandler.clientFeaturesMixinConfig.beastiaryGUIPML && ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability) {
                    if (this.parentGui.player.ticksExisted % 20 == 0) {
                        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.parentGui.player);
                        if (pml != null) {
                            lycanitesTweaks$cachePML = pml.getCurrentLevelBestiary(elementList);
                        }
                    }
                    textBuilder.append("\n\n").append(I18n.format("gui.beastiary.elements.mixin.creatures.description.pml", lycanitesTweaks$cachePML));
                }
            }
            text = textBuilder.toString();
        }
        return text;
    }
}
