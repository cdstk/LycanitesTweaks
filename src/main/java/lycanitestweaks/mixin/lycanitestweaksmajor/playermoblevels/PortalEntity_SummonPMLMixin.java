package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.PortalEntity;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalEntity.class)
public abstract class PortalEntity_SummonPMLMixin {

    @Shadow(remap = false)
    public EntityPlayer shootingEntity;

    @Inject(
            method = "summonCreatures",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/PortalEntity;shootingEntity:Lnet/minecraft/entity/player/EntityPlayer;", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsPortalEntity_summonCreaturesPML(CallbackInfoReturnable<Integer> cir, @Local BaseCreatureEntity entityCreature) {
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.shootingEntity);
        if(pml != null){
            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SummonMinion) || Helpers.hasSoulgazerEquiped(this.shootingEntity)) {
                entityCreature.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SummonMinion, entityCreature));
            }
        }
    }
}
