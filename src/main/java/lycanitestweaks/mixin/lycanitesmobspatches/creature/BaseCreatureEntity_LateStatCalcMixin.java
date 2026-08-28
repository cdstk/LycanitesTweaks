package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_LateStatCalcMixin {

    @Shadow(remap = false)
    public abstract void refreshAttributes();

    @Inject(
            method = "onFirstSpawn",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onFirstSpawn(CallbackInfo ci){
        this.refreshAttributes();
    }
}
