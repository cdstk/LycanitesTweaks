package lycanitestweaks.client.renderer;

import net.minecraft.client.renderer.GlStateManager;

public class TransparentObjRenderer {

    // Based on GlStateManager.Profile.TRANSPARENT_MODEL
    public static void applyGlProfileTransparency() {
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 1F / 255);
    }

    // Based on GlStateManager.Profile.TRANSPARENT_MODEL
    public static void cleanGlProfileTransparency() {
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.depthMask(true);
    }
}
