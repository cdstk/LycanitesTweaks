package lycanitestweaks.client.gui.beastiary;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.GenericEntityKnowledge;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.mixin.vanilla.EntityLiving_InvokerMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomBeastiaryScreen extends BeastiaryScreen {

    // bc modifying com.lycanitesmobs.GuiHandler.Beastiary is stupid
    public static byte nextBeastiaryId = 6;;
    // Lyca Beastiary ID that is unique and also not caught by default handling
    public static int nextInputID = -69;

    public ILycanitesTweaksPlayerCapability ltp;
    public GenericEntityKnowledge genericEntityKnowledge = null;

    private int prevPlayerTick = 0;

    public CustomBeastiaryScreen(EntityPlayer player) {
        super(player);
        this.ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
    }

    public abstract void onSelectableUpdate();

    public void onCreateDisplayEntity(GenericEntityInfo entityInfo, EntityLivingBase entity) {
        if(entityInfo != null) {
            if(this.creaturePreviewEntity instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) this.creaturePreviewEntity;
                if(entityInfo.doInitialSpawn) entityLiving.onInitialSpawn(player.getEntityWorld().getDifficultyForLocation(player.getPosition()), null);
            }

            if(this.creaturePreviewEntity instanceof EntityLiving_InvokerMixin) {
                SoundEvent ambientSound = ((EntityLiving_InvokerMixin) this.creaturePreviewEntity).lycanitesTweaks$invokeGetAmbientSound();
                if(ambientSound != null) {
                    this.player.getEntityWorld().playSound(this.player, this.player.posX, this.player.posY, this.player.posZ, ambientSound, SoundCategory.NEUTRAL, 1, 1);
                }
            }

            entityInfo.getDataFromEntity(entity);

            List<AbstractEntityModification> beastiaryTagModifications = new ArrayList<>(entityInfo.defaultModifiers);
            beastiaryTagModifications.forEach(tagMod -> tagMod.modifyEntity(entity));

            this.onSelectableUpdate();
        }
    }

    public void renderEntity(GenericEntityInfo entityInfo, int x, int y, int mouseX, int mouseY, float partialTicks) {
        // Clear:
        if(entityInfo == null) {
            this.creaturePreviewEntity = null;
            return;
        }

        try {
            if(this.creaturePreviewEntity == null || this.creaturePreviewEntity.isDead || this.creaturePreviewEntity.getClass() != entityInfo.entityClass) {
                this.creaturePreviewEntity = entityInfo.entityClass.getConstructor(new Class[]{World.class}).newInstance(this.player.getEntityWorld());
                this.creaturePreviewEntity.onGround = true;
                if (this.creaturePreviewEntity instanceof BaseCreatureEntity) {
                    ((BaseCreatureEntity) this.creaturePreviewEntity).updateSize();
                }
                if (this.creaturePreviewEntity instanceof AgeableCreatureEntity) {
                    ((AgeableCreatureEntity) this.creaturePreviewEntity).setGrowingAge(0);
                }
                this.onCreateDisplayEntity(entityInfo, this.creaturePreviewEntity);
            }

            // Render:
            if(this.creaturePreviewEntity != null) {
                int creatureSize = 70;
                float creatureWidth = this.creaturePreviewEntity.width;
                float creatureHeight = this.creaturePreviewEntity.height;
                int scale = Math.round((1.8F / Math.max(creatureWidth, creatureHeight)) * creatureSize);
                int posX = x;
                int posY = y + 32 + creatureSize;
                float lookX = (float)posX - mouseX;
                float lookY = (float)posY - mouseY;
                this.creaturePreviewTicks += partialTicks;
                if(this.creaturePreviewEntity instanceof BaseCreatureEntity) {
                    BaseCreatureEntity previewCreatureBase = (BaseCreatureEntity)this.creaturePreviewEntity;
                    previewCreatureBase.onlyRenderTicks = this.creaturePreviewTicks;
                }
                else {
                    if(this.prevPlayerTick != this.player.ticksExisted) {
                        this.creaturePreviewEntity.ticksExisted++;
                        this.prevPlayerTick = this.player.ticksExisted;
                        if(!(this.creaturePreviewEntity instanceof EntityLiving) || !((EntityLiving) this.creaturePreviewEntity).isAIDisabled()) {
                            this.creaturePreviewEntity.onUpdate();
                        }
                    }
                }

                GlStateManager.enableColorMaterial();
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)posX, (float)posY, -500.0F);
                GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                float f = this.creaturePreviewEntity.renderYawOffset;
                float f1 = this.creaturePreviewEntity.rotationYaw;
                float f2 = this.creaturePreviewEntity.rotationPitch;
                float f3 = this.creaturePreviewEntity.prevRotationYawHead;
                float f4 = this.creaturePreviewEntity.rotationYawHead;
                GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-((float)Math.atan((double)(lookY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
                this.creaturePreviewEntity.renderYawOffset = (float)Math.atan((double)(lookX / 40.0F)) * 20.0F;
                this.creaturePreviewEntity.rotationYaw = (float)Math.atan((double)(lookX / 40.0F)) * 40.0F;
                this.creaturePreviewEntity.rotationPitch = -((float)Math.atan((double)(lookY / 40.0F))) * 20.0F;
                this.creaturePreviewEntity.rotationYawHead = this.creaturePreviewEntity.rotationYaw;
                this.creaturePreviewEntity.prevRotationYawHead = this.creaturePreviewEntity.rotationYaw;
                GlStateManager.translate(0.0F, 0.0F, 0.0F);
                RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
                rendermanager.setPlayerViewY(180.0F);
                rendermanager.setRenderShadow(false);
                rendermanager.renderEntity(this.creaturePreviewEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, true);
                rendermanager.setRenderShadow(true);
                this.creaturePreviewEntity.renderYawOffset = f;
                this.creaturePreviewEntity.rotationYaw = f1;
                this.creaturePreviewEntity.rotationPitch = f2;
                this.creaturePreviewEntity.prevRotationYawHead = f3;
                this.creaturePreviewEntity.rotationYawHead = f4;
                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.disableTexture2D();
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            }
        }
        catch (Exception e) {
            LycanitesTweaks.LOGGER.log(Level.WARN, "An exception occurred when trying to preview an entity in the Beastiary.");
            e.printStackTrace();
        }
    }
}
