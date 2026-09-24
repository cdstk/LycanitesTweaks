package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.client.model.ModelCreatureObj;
import com.lycanitesmobs.client.model.ModelObjOld;
import com.lycanitesmobs.client.obj.ObjObject;
import com.lycanitesmobs.client.obj.TessellatorModel;
import lycanitestweaks.client.renderer.TransparentObjRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;

// Credit https://github.com/Fresh-glitch for suggesting a fix
@Mixin(value = {
        ModelCreatureObj.class,
        ModelObjOld.class
})
public abstract class ModelCustoms_DepthMaskMixin {

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/obj/TessellatorModel;renderGroup(Lcom/lycanitesmobs/client/obj/ObjObject;Ljavax/vecmath/Vector4f;Ljavax/vecmath/Vector2f;Lnet/minecraft/client/renderer/vertex/VertexFormat;)V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsModelCustoms_renderGlProfile(TessellatorModel instance, ObjObject group, Vector4f color, Vector2f textureOffset, VertexFormat vertexFormat, Operation<Void> original){
        if(color.w != 1) {
            TransparentObjRenderer.applyGlProfileTransparency();
        }

        original.call(instance, group, color, textureOffset, vertexFormat);

        if(color.w != 1) {
            TransparentObjRenderer.cleanGlProfileTransparency();
        }
    }
}
