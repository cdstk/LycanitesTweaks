package lycanitestweaks.mixin.lycanitestweaksmajor.playermoblevels.limiteddim;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntityBoundDimLimitedMixin extends AgeableCreatureEntity {

    public TameableCreatureEntityBoundDimLimitedMixin(World world) {
        super(world);
    }

    @ModifyArgs(
            method = "getInteractCommands",
            at = @At(value = "INVOKE", target = "Ljava/util/HashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 1),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_getInteractCommands(Args args, @Local(argsOnly = true) EntityPlayer player, @Local(argsOnly = true) EnumHand hand) {
        if(ForgeConfigHandler.majorFeaturesConfig.pmlConfig.playerMobLevelCapability
                && this.isBoundPet()
                && ForgeConfigHandler.majorFeaturesConfig.pmlConfig.pmlMinionLimitDimNoInventory
                && PlayerMobLevelsConfig.isDimensionLimitedMinion(this.dimension)){
            if(hand == EnumHand.MAIN_HAND) player.sendStatusMessage(new TextComponentTranslation("message.soulbound.limited.inventory"), false);
            args.set(1, "");
        }
    }
}
