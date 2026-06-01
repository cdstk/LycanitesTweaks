package lycanitestweaks.client;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.client.model.projectile.AetherwaveModel;
import com.lycanitesmobs.client.model.projectile.ChaosOrbModel;
import com.lycanitesmobs.client.model.projectile.CrystalShardModel;
import com.lycanitesmobs.client.model.projectile.LightBallModel;
import lycanitestweaks.client.renderer.IHasReloadableModel;
import lycanitestweaks.client.renderer.entity.layers.ObjRenderLayer;
import lycanitestweaks.client.renderer.tile.ReloadableModelItemStackRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LycanitesAssetReloader implements ISelectiveResourceReloadListener {

    private static LycanitesAssetReloader INSTANCE;
    private static final List<IHasReloadableModel> CUSTOM_RELOADABLES = new ArrayList<>();

    public static LycanitesAssetReloader getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LycanitesAssetReloader();
        }
        return INSTANCE;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.MODELS)) {
            // Projectile Models:
            AssetManager.models.clear();
            AssetManager.projectileModels.clear();
            AssetManager.objModels.clear();
            AssetManager.itemModels.clear();
            AssetManager.addModel("lightball", new LightBallModel());
            AssetManager.addModel("crystalshard", new CrystalShardModel());
            AssetManager.addModel("aetherwave", new AetherwaveModel());
            AssetManager.addModel("chaosorb", new ChaosOrbModel());

            AssetManager.registerModels();
            CUSTOM_RELOADABLES.forEach(IHasReloadableModel::onReloadModel);
        }
    }

    public static ObjRenderLayer addGenericRenderLayer(ObjRenderLayer renderLayer) {
        addReloadable(renderLayer);
        return renderLayer;
    }

    public static ReloadableModelItemStackRenderer addGenericStackRenderer(ReloadableModelItemStackRenderer stackRenderer) {
        addReloadable(stackRenderer);
        return stackRenderer;
    }

    public static void addReloadable(IHasReloadableModel reloadable) {
        CUSTOM_RELOADABLES.add(reloadable);
    }

    public static final ResourceLocation ASMODEUS_TEXTURE_0 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/asmodeus.png");
    public static final ResourceLocation ASMODEUS_TEXTURE_1 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/asmodeus_verdant.png");
    public static final ResourceLocation ASMODEUS_TEXTURE_2 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/asmodeus_azure.png");

    public static final ResourceLocation RAHOVART_TEXTURE_0 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/rahovart.png");
    public static final ResourceLocation RAHOVART_TEXTURE_1 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/rahovart_lux.png");
    public static final ResourceLocation RAHOVART_TEXTURE_2 = new ResourceLocation(LycanitesMobs.modid, "textures/entity/rahovart_dark.png");
}
