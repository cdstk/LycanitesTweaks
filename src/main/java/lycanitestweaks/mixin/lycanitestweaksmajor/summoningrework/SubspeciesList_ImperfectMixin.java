package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.SubspeciesList;
import com.lycanitesmobs.core.info.CreatureInfo;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SubspeciesList.class)
public abstract class SubspeciesList_ImperfectMixin {

    @Shadow(remap = false) private BeastiaryScreen parentGui;
    @Shadow(remap = false) private CreatureInfo creature;

    // Client and gameplay
    @ModifyArg(
            method = "refreshList",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/Beastiary;hasKnowledgeRank(Ljava/lang/String;I)Z"),
            index = 1,
            remap = false
    )
    public int lycanitesTweaks_lycanitesSubspeciesList_refreshListVariantRank(int rank, @Local(ordinal = 1) int variantIndex){
        if(variantIndex == 0) return ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.normalSummonRank;
        return ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.variantSummonRank;
    }
}
