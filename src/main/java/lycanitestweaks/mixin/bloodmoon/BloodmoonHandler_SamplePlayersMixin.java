package lycanitestweaks.mixin.bloodmoon;

import lumien.bloodmoon.server.BloodmoonHandler;
import lycanitestweaks.compat.BloodmoonUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BloodmoonHandler.class)
public abstract class BloodmoonHandler_SamplePlayersMixin {

    @Inject(
            method = "setBloodmoon",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_bloodmoonBloodmoonHandler_setBloodmoonResetExperiencing(boolean bloodMoon, CallbackInfo ci){
        if(!bloodMoon) BloodmoonUtil.resetExperiencing();
    }
}
