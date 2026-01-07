package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntitySaddleLevelMixin extends TameableCreatureEntity {

    @Unique
    private boolean lycanitesTweaks$hasVanillaSaddle = false;

    public RideableCreatureEntitySaddleLevelMixin(World world) {
        super(world);
    }

    @Inject(
            method = "moveMountedWithHeading",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/RideableCreatureEntity;mountAbility(Lnet/minecraft/entity/Entity;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_moveMountedWithHeadingVanillaLimited(float strafe, float up, float forward, CallbackInfo ci, @Local EntityPlayer player){
        if(lycanitesTweaks$hasVanillaSaddle && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleAllowAbilities)
            player.sendStatusMessage(new TextComponentTranslation("message.mount.fail.noability"), true);
    }

    // Fired twice for both hands, hide it with status message
    @ModifyExpressionValue(
            method = "getInteractCommands",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z")
    )
    public boolean lycanitesTweaks_lycanitesMobsRideableCreatureEntity_getInteractCommandsVanillaLimited(boolean original, @Local(argsOnly = true) EntityPlayer player) {
        if (!original) {
            if (!this.inventory.getEquipmentStack("saddle").isEmpty() &&
                    this.inventory.getEquipmentStack("saddle").getItem() == Items.SADDLE) {
                if (LycanitesEntityUtil.isPracticallyFlying(this) && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleAllowFlying) {
                    player.sendStatusMessage(new TextComponentTranslation("message.mount.fail.noflying"), true);
                    return true;
                } else if (this.getLevel() >= ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleLevelRequirement) {
                    return false;
                } else if (player instanceof EntityPlayer) {
                    player.sendStatusMessage(new TextComponentTranslation("message.mount.fail.saddlelevel", ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleLevelRequirement), true);
                    return true;
                }
            }
        }
        return original;
    }

    @ModifyReturnValue(
            method = "hasSaddle",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsRideableCreatureEntity_hasSaddleVanillaLimited(boolean original, @Local ItemStack saddleStack) {
        if (!original) {
            if (!saddleStack.isEmpty() && saddleStack.getItem() == Items.SADDLE) {
                lycanitesTweaks$hasVanillaSaddle = true;
                return true;
            }
        }
        lycanitesTweaks$hasVanillaSaddle = false;
        return original;
    }

    @Unique
    @Override
    public float getStamina() {
        if(lycanitesTweaks$hasVanillaSaddle && !ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.vanillaSaddleAllowAbilities) return 0;
        return super.getStamina();
    }

}
