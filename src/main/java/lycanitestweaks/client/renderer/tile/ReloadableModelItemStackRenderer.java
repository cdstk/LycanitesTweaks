package lycanitestweaks.client.renderer.tile;

import com.lycanitesmobs.client.renderer.IItemModelRenderer;
import com.lycanitesmobs.client.renderer.layer.LayerItem;
import lycanitestweaks.client.renderer.IHasReloadableModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public abstract class ReloadableModelItemStackRenderer extends TileEntityItemStackRenderer implements IItemModelRenderer, IHasReloadableModel {

    public abstract void renderInHand(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    public abstract ResourceLocation getStackTexture(ItemStack itemStack);

    @Override
    public void bindItemTexture(ResourceLocation location) {
        if(location == null) return;
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(location);
    }

    @Override
    public List<LayerItem> addLayer(LayerItem renderLayer) {
        return Collections.emptyList();
    }

    protected EntityLivingBase getRenderingEntity() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.getRenderViewEntity() == mc.player) {
            return mc.player;
        }

        RenderManager manager = mc.getRenderManager();
        if (manager.renderViewEntity instanceof EntityLivingBase) {
            return (EntityLivingBase) manager.renderViewEntity;
        }

        return null;
    }
}
