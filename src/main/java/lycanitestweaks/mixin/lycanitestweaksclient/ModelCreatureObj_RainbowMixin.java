package lycanitestweaks.mixin.lycanitestweaksclient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.model.ModelCreatureObj;
import com.lycanitesmobs.client.model.ModelObjOld;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import javax.vecmath.Vector4f;

@Mixin(value = {
        ModelCreatureObj.class,
        ModelObjOld.class
})
public abstract class ModelCreatureObj_RainbowMixin {

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/client/obj/TessellatorModel;renderGroup(Lcom/lycanitesmobs/client/obj/ObjObject;Ljavax/vecmath/Vector4f;Ljavax/vecmath/Vector2f;Lnet/minecraft/client/renderer/vertex/VertexFormat;)V"),
            index = 1,
            remap = false
    )
    public Vector4f lycanitesTweaks_lycanitesMobsModelCreatureObj_renderRainbow(Vector4f color, @Local(argsOnly = true) Entity entity, @Local(argsOnly = true, ordinal = 0) float time, @Local(argsOnly = true) LayerCreatureBase layer){
        if(entity.hasCustomName()){
            if((layer == null && entity.getCustomNameTag().contains("ainbow"))
                || entity.getCustomNameTag().contains("777")){
                int i = entity.ticksExisted / 25 + entity.getEntityId();
                int j = EnumDyeColor.values().length;
                int k = i % j;
                int l = (i + 1) % j;
                float f = ((float) (entity.ticksExisted % 25) + time) / 25.0F;
                float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));

                return new Vector4f(
                        afloat1[0] * (1.0F - f) + afloat2[0] * f,
                        afloat1[1] * (1.0F - f) + afloat2[1] * f,
                        afloat1[2] * (1.0F - f) + afloat2[2] * f,
                        1.0F
                );
            }
        }
        return color;
    }
}
