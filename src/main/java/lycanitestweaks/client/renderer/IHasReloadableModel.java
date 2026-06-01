package lycanitestweaks.client.renderer;

import com.lycanitesmobs.client.model.ModelCustom;
import net.minecraft.util.ResourceLocation;

public interface IHasReloadableModel {

    ModelCustom getObjModel();

    ResourceLocation getTexture();

    void onReloadModel();
}
