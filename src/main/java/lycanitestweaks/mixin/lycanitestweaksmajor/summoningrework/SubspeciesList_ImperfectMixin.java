package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.beastiary.lists.SubspeciesList;
import com.lycanitesmobs.core.info.Beastiary;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SubspeciesList.class)
public abstract class SubspeciesList_ImperfectMixin {

//     Client and gameplay
    @Redirect(
            method = "refreshList",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/Beastiary;hasKnowledgeRank(Ljava/lang/String;I)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesSubspeciesList_refreshList(Beastiary instance, String creatureName, int rank, @Local(ordinal = 1) int variantIndex){
        if(instance.hasKnowledgeRank(creatureName, ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.variantSummonRank)) return true;
        else if(instance.hasKnowledgeRank(creatureName, ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.normalSummonRank)) return variantIndex == 0;
        return false;
    }
}
