package lycanitestweaks.client.model;

import com.lycanitesmobs.client.model.ModelCustom;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

import java.util.Map;

public class ModelCustomGeneric extends ModelCustom {

    // This exists because unnecessary unchecked casts from Entity -> EntityLiving crippling usage for Players

    @Override
    public void render(Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale, LayerCreatureBase layer, boolean animate) {
        float sizeScale = 1F;
        if(entity instanceof BaseCreatureEntity) {
            sizeScale *= (float) ((BaseCreatureEntity) entity).getRenderScale();
        }
        else {
            sizeScale *= this.getRenderScale(entity);
        }
        GlStateManager.scale(sizeScale, sizeScale, sizeScale);
        GlStateManager.translate(0F, 0.5F - sizeScale / 2F, 0F);

        setAngles(entity, time, distance, loop, lookY, lookX, scale);
        animate(entity, time, distance, loop, lookY, lookX, scale);
    }

    // ==================================================
    //                   Set Angles
    // ==================================================
    public void setAngles(Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        // Set Initial Rotations:
        for(Map.Entry<ModelRenderer, float[]> initRotation : initRotations.entrySet()) {
            float[] rotations = initRotation.getValue();
            setRotation(initRotation.getKey(), rotations[0], rotations[1], rotations[2]);
        }
    }


    // ==================================================
    //                 Animate Model
    // ==================================================
    public void animate(Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        return;
    }

    public float getRenderScale(Entity entity) {
        return 1F;
    }
}
