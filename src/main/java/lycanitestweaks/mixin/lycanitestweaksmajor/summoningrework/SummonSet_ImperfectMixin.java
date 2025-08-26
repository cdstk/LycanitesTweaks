package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.handlers.ForgeConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SummonSet.class)
public abstract class SummonSet_ImperfectMixin {

    @Shadow(remap = false)
    public ExtendedPlayer playerExt;
    @Shadow(remap = false)
    public String summonType;

    @ModifyReturnValue(
            method = "getVariant",
            at = @At("RETURN"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesSummonSet_getVariant(int original){
        if(this.playerExt == null) return original;
        if(this.playerExt.getBeastiary().hasKnowledgeRank(this.summonType, ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.variantSummonRank)) return original;
        return 0;
    }
}
