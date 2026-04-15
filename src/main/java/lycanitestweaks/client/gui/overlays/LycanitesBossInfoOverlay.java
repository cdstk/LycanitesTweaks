package lycanitestweaks.client.gui.overlays;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class LycanitesBossInfoOverlay {

    protected static void drawRHS(BaseCreatureEntity creature, int x, int y, FontRenderer fontRenderer) {
        String healthString = String.format("%.2f", creature.getHealth()) + "/" + String.format("%.2f", creature.getMaxHealth());
        drawTexture(
                creature.creatureInfo.getIcon(),
                x += 2,
                y,
                0,
                0,
                0,
                1,
                1,
                16,
                16
        );
        fontRenderer.drawString(healthString, x += 18, y + (fontRenderer.FONT_HEIGHT / 2), 0xFFFFFF);
    }

    /**
     *
     * @see com.lycanitesmobs.client.gui.BaseScreen
     */
    public static void drawTexture(ResourceLocation texture, float x, float y, float z, float minU, float minV, float maxU, float maxV, float width, float height) {
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, z).tex(minU, maxV).endVertex();
        buffer.pos(x + width, y + height, z).tex(maxU, maxV).endVertex();
        buffer.pos(x + width, y, z).tex(maxU, minV).endVertex();
        buffer.pos(x, y, z).tex(minU, minV).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
    }
}
