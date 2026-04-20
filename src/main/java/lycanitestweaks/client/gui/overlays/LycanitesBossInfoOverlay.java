package lycanitestweaks.client.gui.overlays;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class LycanitesBossInfoOverlay {

    protected static final ResourceLocation AMALGALICH_EVENT_TEXTURE = new ResourceLocation(LycanitesMobs.modid,"textures/mobevents/amalgalich.png");
    protected static final ResourceLocation ASMODEUS_EVENT_TEXTURE = new ResourceLocation(LycanitesMobs.modid,"textures/mobevents/asmodeus.png");
    protected static final ResourceLocation RAHOVART_EVENT_TEXTURE = new ResourceLocation(LycanitesMobs.modid,"textures/mobevents/rahovart.png");

    protected static ResourceLocation fearTexture = null;
    protected static ResourceLocation decayTexture = null;
    protected static ResourceLocation instabilityTexture = null;

    protected static ResourceLocation krillIssue = null;
    protected static ResourceLocation hotKotlin = null;
    protected static ResourceLocation kotlin = null;

    public static void initClientReferences() {
        if(ModLoadedUtil.eagleMixins.isLoaded()) {
            krillIssue = new ResourceLocation(ModLoadedUtil.EAGLEMIXINS_MODID, "textures/paintings/krillissue.png");
            hotKotlin = new ResourceLocation(ModLoadedUtil.EAGLEMIXINS_MODID, "textures/paintings/hotkotlin.png");
            kotlin = new ResourceLocation(ModLoadedUtil.EAGLEMIXINS_MODID, "textures/paintings/kotlin.png");
        }

        fearTexture = AssetManager.getTexture("effect.fear");
        decayTexture = AssetManager.getTexture("effect.decay");
        instabilityTexture = AssetManager.getTexture("effect.instability");
    }

    protected static void drawRHS(BaseCreatureEntity creature, int x, int y, FontRenderer fontRenderer) {
        String healthString = String.format("%.0f", creature.getHealth()) + "/" + String.format("%.0f", creature.getMaxHealth());
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

    public static void drawTopCenteredConfigurableTexture(ResourceLocation texture, ScaledResolution scaledResolution, float[] renderConfig) {
        float[] eventRender = ForgeConfigProvider.getFloatArray(renderConfig, 0F, 4);
        if(eventRender[0] != 0F) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(
                    eventRender[1] - (eventRender[0] * 128F) + (scaledResolution.getScaledWidth() / 2F),
                    -eventRender[2],
                    eventRender[3]
            );
            GlStateManager.scale(eventRender[0], eventRender[0], eventRender[0]);
            Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
            GuiUtils.drawTexturedModalRect(
                    0,
                    0,
                    256,
                    256,
                    256,
                    256,
                    0
            );
            GlStateManager.popMatrix();
        }
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

    protected static class BouncingTexture {

        private final ResourceLocation texture;
        private final ScaledResolution scaledResolution;
        private final int width;
        private final int height;
        private final float scale;

        private int lifeTime;
        private float x;
        private float y;
        private float dx;
        private float dy;

        BouncingTexture(@Nullable ResourceLocation texture, Random random, ScaledResolution scaledResolution, int width, int height, int durationFrames) {
            this.texture = texture;
            this.scaledResolution = scaledResolution;
            this.width = width;
            this.height = height;
            this.scale = 1F / scaledResolution.getScaleFactor();

            this.lifeTime = durationFrames;

            this.x = random.nextFloat() * this.getWidthBounds();
            this.y = random.nextFloat() * this.getHeightBounds();
            this.dx = (-2 * random.nextInt(2) + 1) * random.nextFloat() * 16F * this.scale;
            this.dy = (-2 * random.nextInt(2) + 1) * random.nextFloat() * 16F * this.scale;
        }

        public BouncingTexture doRender() {
            if(lifeTime-- <= 0) {
                return null;
            }

            this.x += this.dx;
            this.y += this.dy;

            if(this.x <= 0 || this.x >= this.getWidthBounds())
                this.dx = -this.dx;
            if(this.y <= 0 || this.y >= this.getHeightBounds())
                this.dy = -this.dy;

            GlStateManager.pushMatrix();
            GlStateManager.translate(
                    this.x,
                    this.y,
                    0
            );
            GlStateManager.scale(this.scale, this.scale, this.scale);
            if(this.texture != null) {
                LycanitesBossInfoOverlay.drawTexture(
                        this.texture,
                        0,
                        0,
                        0,
                        0,
                        0,
                        1,
                        1,
                        this.width,
                        this.height
                );
            }
            GlStateManager.popMatrix();

            return this;
        }

        private float getWidthBounds() {
            return this.scaledResolution.getScaledWidth() - (this.width * this.scale);
        }

        private float getHeightBounds() {
            return this.scaledResolution.getScaledHeight() - (this.height * this.scale);
        }
    }
}
