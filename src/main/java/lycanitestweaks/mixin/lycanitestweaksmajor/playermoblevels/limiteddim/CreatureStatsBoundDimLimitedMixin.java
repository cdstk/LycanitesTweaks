package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels.limiteddim;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreatureStats.class)
public abstract class CreatureStatsBoundDimLimitedMixin {

    @Shadow(remap = false)
    public BaseCreatureEntity entity;

    // Use limited stat calc for Soulbounds, doesn't reflect nametag levels but won't delete actual levels.
    @ModifyExpressionValue(
            method = "getLevelMultiplier",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getLevel()I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsPetEntry_onUpdate(int original){
        if(PlayerMobLevelsConfig.isDimensionLimitedMinion(this.entity.dimension) && this.entity.isBoundPet() && this.entity.getPetEntry().host instanceof EntityPlayer){
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer((EntityPlayer) this.entity.getPetEntry().host);
            if(pml != null){
                int levels = Math.min(original,
                        pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SoulboundTame, this.entity));
                if(this.entity.ticksExisted == 0) {
                    ((EntityPlayer) this.entity.getPetEntry().host).sendStatusMessage(new TextComponentTranslation("message.soulbound.limited.levels", levels), true);
                }
                return levels;
            }
        }
        return original;
    }
}
