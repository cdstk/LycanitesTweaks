package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.lycanitesmobs.client.gui.beastiary.lists.CreatureList;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CreatureList.class)
public abstract class CreatureList_ImperfectSummonMixin {

    // Client and Gameplay
    @ModifyConstant(
            method = "refreshList",
            constant = @Constant(intValue = 2),
            remap = false
    )
    public int lycanitesTweaks_lycanitesCreatureList_refreshList(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.normalSummonRank;
    }
}
