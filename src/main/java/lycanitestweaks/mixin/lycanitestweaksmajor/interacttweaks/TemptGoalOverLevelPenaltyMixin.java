package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.actions.TemptGoal;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TemptGoal.class)
public abstract class TemptGoalOverLevelPenaltyMixin {

    @Shadow(remap = false)
    private BaseCreatureEntity host;
    @Shadow(remap = false)
    private EntityPlayer player;

    @ModifyReturnValue(
            method = "isTemptStack",
            at = @At(value = "RETURN", ordinal = 1),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTemptGoal_isTemptStackLevelPenalty(boolean original){
        if(original && !ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelTreatTempt){
            if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel > 0){
                IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.player);
                if(pml != null){
                    if (Math.max(0, this.host.getLevel() - pml.getHighestLevelPetSoulbound()) > ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel)
                        return false;
                }
//                else{
//                    if(this.host.getLevel() > ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel) return false;
//                }
            }
        }
        return original;
    }
}
