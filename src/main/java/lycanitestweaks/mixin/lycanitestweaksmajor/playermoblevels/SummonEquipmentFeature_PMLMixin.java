package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.item.equipment.features.SummonEquipmentFeature;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SummonEquipmentFeature.class)
public abstract class SummonEquipmentFeature_PMLMixin {

    @Inject(
            method = "onHitEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;setPlayerOwner(Lnet/minecraft/entity/player/EntityPlayer;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsSummonEquipmentFeature_onHitEntityPML(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker, CallbackInfoReturnable<Boolean> cir, @Local TameableCreatureEntity entityTameable){
        EntityPlayer player = (EntityPlayer) attacker;
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
        if(pml != null){
            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant) || Helpers.hasSoulgazerEquiped(player)) {
                entityTameable.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant, entityTameable));
            }
        }
    }
}
