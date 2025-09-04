package lycanitestweaks.mixin.lycanitestweakscore.chargeexperience;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import com.lycanitesmobs.core.item.ChargeItem;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.Helpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreatureStats.class)
public abstract class CreatureStats_LogScaleMixin {

    @Shadow(remap = false) public BaseCreatureEntity entity;

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsCreatureStats_initXPBaseValues(BaseCreatureEntity entity, CallbackInfo ci){
        CreatureStats.BASE_LEVELUP_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.baseExperiencePets;
        ChargeItem.CHARGE_EXPERIENCE = ForgeConfigHandler.server.chargeExpConfig.chargeExperienceValue;
    }

    @ModifyReturnValue(
            method = "getExperienceForNextLevel",
            at = @At("RETURN"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsCreatureStats_getExperienceForNextLevelCalc(int original){
        return Helpers.calculateExperienceForNextLevel(CreatureStats.BASE_LEVELUP_EXPERIENCE, this.entity.getLevel());
    }
}
