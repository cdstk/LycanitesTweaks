package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import com.lycanitesmobs.core.mobevent.MobEvent;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MobEvent.class)
public abstract class MobEventBossesPlayerMobLevelsMixin {

    // TODO Consider moving this to an altar event

    @Shadow(remap = false)
    public String channel;

    @ModifyArg(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;addLevel(I)V"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobEvent_onSpawn(int level, @Local(argsOnly = true) EntityPlayer player, @Local BaseCreatureEntity entityCreature){
        if(player != null && (!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.AltarBossMain) || player.getHeldItemMainhand().getItem() instanceof ItemSoulgazer)) {
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
            if (pml != null) {
                if ("boss".equals(this.channel)) {
                    player.sendStatusMessage(new TextComponentTranslation("message.event.boss.playermoblevels"), true);
                    return level + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.AltarBossMain, entityCreature);
                }
            }
        }
        return level;
    }
}
