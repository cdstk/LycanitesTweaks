package lycanitestweaks.mixin.vanilla.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRenderer_CrystalClickableMixin {

    @ModifyExpressionValue(
            method = "getMouseOver",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;")
    )
    private List<Entity> lycanitesTweaks_vanillaEntityRenderer_getMouseOverBossCrystal(List<Entity> original){
        original.removeIf(entity -> entity instanceof EntityBossSummonCrystal && !ClientEventListener.canBreakCrystal((EntityBossSummonCrystal) entity));
        return original;
    }
}
