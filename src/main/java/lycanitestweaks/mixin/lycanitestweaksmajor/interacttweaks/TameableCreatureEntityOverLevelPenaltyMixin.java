package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntityOverLevelPenaltyMixin extends AgeableCreatureEntity {

    public TameableCreatureEntityOverLevelPenaltyMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "tame",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureRelationshipEntry;increaseReputation(I)Z"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsTameableCreatureEntity_tameLevelPenalty(int amount, @Local(argsOnly = true) EntityPlayer player){
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelTreatPointPenalty && ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel > 0){
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
            if (pml != null){
                if (Math.max(0, this.getLevel() - pml.getHighestLevelPetSoulbound()) > ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel) {
                    player.sendStatusMessage(new TextComponentTranslation("message.tame.overlevel.penalty"), false);
                    return (int) Math.max(1, amount * (float) (pml.getHighestLevelPetSoulbound()) / (2 * this.getLevel()));
                }
            }
//            else{
//                if(this.getLevel() > ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel) {
//                    player.sendStatusMessage(new TextComponentTranslation("message.tame.overlevel.penalty"), false);
//                    return (int) Math.max(1, amount * (float) ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlTamedOverLevelStartLevel / (2 * this.getLevel()));
//                }
//            }
        }
        return amount;
    }
}
