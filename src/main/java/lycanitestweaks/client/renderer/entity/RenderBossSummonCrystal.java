package lycanitestweaks.client.renderer.entity;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.entitystorecreature.EntityStoreCreatureCapabilityHandler;
import lycanitestweaks.capability.entitystorecreature.IEntityStoreCreatureCapability;
import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.storedcreatureentity.StoredCreatureEntity;
import lycanitestweaks.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class RenderBossSummonCrystal extends Render<EntityBossSummonCrystal> {

    private static final ResourceLocation BOSS_SUMMON_CRYSTAL_TEXTURES = new ResourceLocation(LycanitesTweaks.MODID,"textures/entity/dungeonbosscrystal/dungeonbosscrystal.png");
    private static final ResourceLocation BOSS_SUMMON_CRYSTAL_TEXTURES_DIAMOND = new ResourceLocation(LycanitesTweaks.MODID,"textures/entity/dungeonbosscrystal/dungeonbosscrystal_diamond.png");
    private static final ResourceLocation BOSS_SUMMON_CRYSTAL_TEXTURES_EMERALD = new ResourceLocation(LycanitesTweaks.MODID,"textures/entity/dungeonbosscrystal/dungeonbosscrystal_emerald.png");
    private static final ResourceLocation BOSS_SUMMON_CRYSTAL_TEXTURES_ENCOUNTER = new ResourceLocation(LycanitesTweaks.MODID,"textures/entity/dungeonbosscrystal/dungeonbosscrystal_encounter.png");
    private static final ResourceLocation BOSS_SUMMON_CRYSTAL_TEXTURES_CLEAR = new ResourceLocation(LycanitesTweaks.MODID,"textures/entity/dungeonbosscrystal/dungeonbosscrystal_clear.png");

    private final ModelBase modelBossSummonCrystal = new ModelEnderCrystal(0.0F, true);
    private final ModelBase modelBossSummonCrystalNoBase = new ModelEnderCrystal(0.0F, false);

    public RenderBossSummonCrystal(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityBossSummonCrystal entity, double x, double y, double z, float entityYaw, float partialTicks){
        ResourceLocation crystalTexture = this.getEntityTexture(entity);
        boolean renderTransparent = !ClientEventListener.canBreakCrystal(entity);
        float rotation = (float)entity.innerRotation + partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.bindTexture(this.getEntityTexture(entity));
        float bob = MathHelper.sin(rotation * 0.2F) / 2.0F + 0.5F;
        bob = bob * bob + bob;

        if(this.renderOutlines){
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        if (renderTransparent) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }

        if(entity.shouldShowBottom()){
            this.modelBossSummonCrystal.render(
                    entity,
                    0.0F,
                    rotation * 3.0F,
                    bob * 0.2F,
                    0.0F,
                    0.0F,
                    0.0625F
            );
        }
        else{
            this.modelBossSummonCrystalNoBase.render(
                    entity,
                    0.0F,
                    rotation * 3.0F,
                    bob * 0.2F,
                    0.0F,
                    0.0F,
                    0.0625F
            );
        }

        if(this.renderOutlines){
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        IEntityStoreCreatureCapability storeCreature = entity.getCapability(EntityStoreCreatureCapabilityHandler.ENTITY_STORE_CREATURE, null);
        if (storeCreature != null && crystalTexture == BOSS_SUMMON_CRYSTAL_TEXTURES_CLEAR) {
            renderMob(storeCreature.getStoredCreatureEntity(), entity.posX, entity.posY, entity.posZ, partialTicks, rotation, bob);
        }

        if (renderTransparent) {
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }

        GlStateManager.popMatrix();
        BlockPos blockpos = entity.getBeamTarget();

        if (blockpos != null){
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            float beamX = (float)blockpos.getX() + 0.5F;
            float beamY = (float)blockpos.getY() + 0.5F;
            float beamZ = (float)blockpos.getZ() + 0.5F;
            double deltaX = (double)beamX - entity.posX;
            double deltaY = (double)beamY - entity.posY;
            double deltaZ = (double)beamZ - entity.posZ;
            RenderDragon.renderCrystalBeams(
                    x + deltaX,
                    y - 0.3D + (double)(bob * 0.4F) + deltaY,
                    z + deltaZ,
                    partialTicks,
                    beamX,
                    beamY,
                    beamZ,
                    entity.innerRotation,
                    entity.posX,
                    entity.posY,
                    entity.posZ
            );
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(EntityBossSummonCrystal entity){
        if(this.shouldRenderMob(entity)) return BOSS_SUMMON_CRYSTAL_TEXTURES_CLEAR;
        switch (entity.getVariantType()){
            case -1: return BOSS_SUMMON_CRYSTAL_TEXTURES_ENCOUNTER;
            case 0: return BOSS_SUMMON_CRYSTAL_TEXTURES;
            case 1: return BOSS_SUMMON_CRYSTAL_TEXTURES_DIAMOND;
            case 2: return BOSS_SUMMON_CRYSTAL_TEXTURES_EMERALD;
        }
        return new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    }

    @Override
    public boolean shouldRender(@Nonnull EntityBossSummonCrystal livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ) || livingEntity.getBeamTarget() != null;
    }

    public boolean shouldRenderMob(EntityBossSummonCrystal entity) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        return player != null && player.canEntityBeSeen(entity) && Helpers.hasSoulgazerEquiped(player);
    }

    public static void renderMob(StoredCreatureEntity storedCreatureEntity, double posX, double posY, double posZ, float partialTicks, float rotation, float bob) {
        Entity entity = storedCreatureEntity.getCachedEntity();

        if (entity != null) {
            float scale = 0.3F;
            float largestDimension = Math.max(entity.width, entity.height);

            if (largestDimension > 1.0D) {
                scale /= largestDimension;
            }

            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            GlStateManager.translate(0F, -0.7F, 0F);

            GlStateManager.rotate(rotation * 10F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.8F + (bob * 0.2F), 0.0F);
            GlStateManager.scale(scale, scale, scale);

            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        }
    }
}
