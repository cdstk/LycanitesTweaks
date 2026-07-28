package lycanitestweaks.mixin.lycanitestweaksmajor.summoningrework;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.PortalEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalEntity.class)
public abstract class PortalEntity_SummonKnowledgeMixin {

    @Shadow(remap = false) public EntityPlayer shootingEntity;

    @Inject(
            method = "summonCreatures",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;setMinion(Z)V", ordinal = 0),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsPortalEntity_summonCreaturesStaffKnowledge(CallbackInfoReturnable<Integer> cir, @Local BaseCreatureEntity entityCreature){
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.shootingEntity);

        if(extendedPlayer != null) {
            extendedPlayer.studyCreature(entityCreature, ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.summonKnowledgeGain, false, false);
        }
    }
}
