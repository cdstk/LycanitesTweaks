package lycanitestweaks.client.renderer.entity.layers;

import lycanitestweaks.client.renderer.IHasReloadableModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public abstract class ObjRenderLayer implements LayerRenderer<EntityLivingBase>, IHasReloadableModel {

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!this.canRenderLayer(entitylivingbaseIn)) return;

        if(this.getObjModel() != null) {
            if(this.getTexture() != null) {
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(this.getTexture());
            }
            this.getObjModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    public boolean canRenderLayer(EntityLivingBase entityLivingBase) {
        if(entityLivingBase.isInvisible() && entityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().player)) {
            return false;
        }
        return true;
    }
}
