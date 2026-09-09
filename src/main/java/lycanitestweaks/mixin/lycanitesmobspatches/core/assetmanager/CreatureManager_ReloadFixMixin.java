package lycanitestweaks.mixin.lycanitesmobspatches.core.assetmanager;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.info.CreatureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CreatureManager.class)
public abstract class CreatureManager_ReloadFixMixin {

    @ModifyExpressionValue(
            method = "parseJson",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z", ordinal = 2),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsCreatureManager_parseJsonInitOnce(boolean creatureInfoExists){
        // Fix 2x drops due to double init, replacement is already fully prepared
        return false;
    }
}
