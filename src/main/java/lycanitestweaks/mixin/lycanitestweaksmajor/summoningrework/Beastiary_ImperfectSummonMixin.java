package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.lycanitesmobs.core.info.Beastiary;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Beastiary.class)
public abstract class Beastiary_ImperfectSummonMixin {

    // Client Chat Message
    @ModifyConstant(
            method = "sendAddedMessage",
            constant = @Constant(intValue = 2, ordinal = 0),
            remap = false
    )
    public int lycanitesTweaks_lycanitesBeastiary_sendAddedMessage(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.normalSummonRank;
    }

    // Summon Gameplay
    @ModifyConstant(
            method = "getSummonableList",
            constant = @Constant(intValue = 2),
            remap = false
    )
    public int lycanitesTweaks_lycanitesBeastiary_getSummonableList(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.normalSummonRank;
    }
}
