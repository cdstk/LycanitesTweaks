package lycanitestweaks.mixin.potioncore;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.tmtravlr.potioncore.PotionCoreEventHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionCoreEventHandler.class)
public abstract class PotionCoreEventHandlerMixin {

    /**
     * Fix PotionCore forcibly overwriting player motionY breaking motion increases set by other mods
     * Based on fix in Bounceable by fonnymunkey
     * 1. Maintain "bouncing" behavior of flying mobs
     * 2. Restore motionY jumping abilities of mobs
     *
     */
    @ModifyExpressionValue(
            method = "onLivingJump",
            at = @At(value = "INVOKE", target = "Ljava/lang/Math;sqrt(D)D"),
            remap = false
    )
    private static double lycanitesTweaks_potionCoreEventHandler_onLivingJump(double potionCoreSetValue, LivingEvent.LivingJumpEvent event) {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        if(entityLivingBase instanceof BaseCreatureEntity
                || (ForgeConfigHandler.integrationConfig.fixAllMobsPotionCoreJump && entityLivingBase instanceof EntityLiving)) {
            return Math.max(potionCoreSetValue, entityLivingBase.motionY);
        }
        return potionCoreSetValue;
    }
}
