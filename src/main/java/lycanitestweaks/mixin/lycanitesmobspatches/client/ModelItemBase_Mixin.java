package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.client.model.ModelItemBase;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelItemBase.class)
public abstract class ModelItemBase_Mixin {

    @WrapOperation(
            method = "onRenderStart",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableBlend()V")
    )
    private void lycanitesTweaks_lycanitesMobsModelItemBase_onRenderStartBlendFuncConfig(Operation<Void> original){
        original.call();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    @WrapOperation(
            method = "onRenderStart",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;blendFunc(Lnet/minecraft/client/renderer/GlStateManager$SourceFactor;Lnet/minecraft/client/renderer/GlStateManager$DestFactor;)V")
    )
    private void lycanitesTweaks_lycanitesMobsModelItemBase_onRenderStartBlendFuncNoop(GlStateManager.SourceFactor p_187401_0_, GlStateManager.DestFactor p_187401_1_, Operation<Void> original){
        // no op
    }
}
