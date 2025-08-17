package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviourSummon;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileBehaviourSummon.class)
public abstract class ProjectileBehaviourSummon_PMLMixin {

    @Inject(
            method = "onProjectileImpact",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;setPlayerOwner(Lnet/minecraft/entity/player/EntityPlayer;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsProjectileBehaviourSummon_onProjectileImpactPML(BaseProjectileEntity projectile, World world, BlockPos pos, CallbackInfo ci, @Local TameableCreatureEntity entityTameable){
        EntityPlayer player = (EntityPlayer) projectile.getThrower();
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
        if(pml != null){
            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant) || Helpers.hasSoulgazerEquiped(player)) {
                entityTameable.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant, entityTameable));
            }
        }
    }
}
