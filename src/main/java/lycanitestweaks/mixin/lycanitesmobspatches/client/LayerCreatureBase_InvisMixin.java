package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.lycanitesmobs.client.KeyHandler;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.vecmath.Vector4f;

@Mixin(LayerCreatureBase.class)
public abstract class LayerCreatureBase_InvisMixin {

    @Inject(
            method = "getPartColor",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsLayerCreatureBase_getPartColorInvis(String partName, BaseCreatureEntity entity, boolean trophy, CallbackInfoReturnable<Vector4f> cir){
        if(entity == null) return;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(entity.isInvisible() && !entity.isInvisibleToPlayer(player)){
            cir.setReturnValue(new Vector4f(1, 1, 1, 0.15F));
        }

        if(entity.getControllingPassenger() == player) {
            if((KeyHandler.instance.descend.isKeyDown() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountDescend)
                    || (player.isSneaking() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSneak)
                    || (player.isSprinting() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSprint))
                cir.setReturnValue(new Vector4f(1, 1, 1, 0.15F));
        }
    }
}
