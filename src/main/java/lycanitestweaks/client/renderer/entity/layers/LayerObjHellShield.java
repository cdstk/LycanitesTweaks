package lycanitestweaks.client.renderer.entity.layers;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.model.ModelCustom;
import lycanitestweaks.client.LycanitesAssetReloader;
import lycanitestweaks.client.model.item.ModelHellShield;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.item.ItemHellShield;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerObjHellShield extends ObjRenderLayer {

    private int currentTick = 0;
    private ModelHellShield objModel = null;
    private static final ResourceLocation ASMODEUS_TEXTURE = new ResourceLocation(LycanitesMobs.modid, "textures/entity/asmodeus.png");

    @Override
    public boolean canRenderLayer(EntityLivingBase entityLivingBase) {
        if(this.objModel != null) {
            if(this.currentTick == entityLivingBase.ticksExisted) {
                // Don't update
            }
            else {
                this.objModel.setShieldActive(false);
                if(ItemHellShield.getTickedStacks(entityLivingBase) != null && !ItemHellShield.getTickedStacks(entityLivingBase).isEmpty()) {
                    for (ItemStack itemStack : ItemHellShield.getTickedStacks(entityLivingBase)) {
                        if (itemStack.getItem() == LycanitesTweaksRegistry.hellShield) {
                            ItemHellShield hellShield = (ItemHellShield) itemStack.getItem();
                            this.objModel.setShieldVariant(hellShield.getEntityVariant(itemStack));
                            this.objModel.setShieldActive(true);
                            break;
                        }
                    }
                }
            }
            this.currentTick = entityLivingBase.ticksExisted;
        }
        return super.canRenderLayer(entityLivingBase);
    }

    @Override
    public ModelCustom getObjModel() {
        if(this.objModel == null) this.objModel = new ModelHellShield();
        this.objModel.setItem(false);
        return this.objModel;
    }

    @Override
    public ResourceLocation getTexture() {
        if(this.objModel != null) {
            switch (this.objModel.getShieldVariant()) {
                case 1: return LycanitesAssetReloader.ASMODEUS_TEXTURE_1;
                case 2: return LycanitesAssetReloader.ASMODEUS_TEXTURE_2;
            }
        }
        return ASMODEUS_TEXTURE;
    }

    @Override
    public void onReloadModel() {
        this.objModel = null;
    }
}
