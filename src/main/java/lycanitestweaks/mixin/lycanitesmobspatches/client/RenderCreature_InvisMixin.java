package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.lycanitesmobs.client.renderer.RenderCreature;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderCreature.class)
public abstract class RenderCreature_InvisMixin extends RenderLiving<BaseCreatureEntity> {

    public RenderCreature_InvisMixin(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Unique
    @Override
    protected boolean isVisible(BaseCreatureEntity entity) {
        return super.isVisible(entity) || !entity.isInvisibleToPlayer(Minecraft.getMinecraft().player);
    }
}
