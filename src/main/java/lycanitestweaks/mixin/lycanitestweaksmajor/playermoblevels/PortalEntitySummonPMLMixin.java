package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.PortalEntity;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalEntity.class)
public abstract class PortalEntitySummonPMLMixin {

    // TODO Consider moving to summon staffs

    @Shadow(remap = false)
    public EntityPlayer shootingEntity;

    // Set levels for summons bc don't have to care about saving creature stats
    @Inject(
            method = "summonCreatures",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/PortalEntity;shootingEntity:Lnet/minecraft/entity/player/EntityPlayer;", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesPortalEntity_summonCreatures(CallbackInfoReturnable<Integer> cir, @Local BaseCreatureEntity entityCreature) {
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelSummonStaff){
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(this.shootingEntity);
            if(pml != null){
                entityCreature.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SummonMinion, entityCreature));
            }
        }
    }
}
