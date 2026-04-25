package lycanitestweaks.mixin.reachfix.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import meldexun.reachfix.hook.client.EntityRendererHook;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRendererHook.class)
public abstract class EntityRendererHook_Mixin {

    @ModifyExpressionValue(
            method = "lambda$getPointedEntity$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;canBeCollidedWith()Z")
    )
    private static boolean lycanitesTweaks_reachFixEntityRendererHook_getPointedEntityBossCrystal(boolean canBeCollidedWith, @Local(argsOnly = true) Entity entityx){
        return canBeCollidedWith && (!(entityx instanceof EntityBossSummonCrystal) || ClientEventListener.canBreakCrystal((EntityBossSummonCrystal) entityx));
    }
}
