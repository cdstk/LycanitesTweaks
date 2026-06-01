package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.client.model.ModelCreatureObj;
import com.lycanitesmobs.client.model.ModelItemBase;
import com.lycanitesmobs.client.model.ModelObjOld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        ModelCreatureObj.class,
        ModelItemBase.class,
        ModelObjOld.class
})
public abstract class ModelLycanites_Mixin {

    @WrapOperation(
            method = "onRenderFinish",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableBlend()V")
    )
    private void lycanitesTweaks_lycanitesMobsModelLycanites_onRenderFinishBlendFuncNoop(Operation<Void> original){
        // no op
    }
}
