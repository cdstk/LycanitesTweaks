package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.KeyHandler;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.client.renderer.TransparentObjRenderer;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerCreatureBase.class)
public abstract class LayerCreatureBase_InvisMixin {

    @Unique
    private boolean lycanitesTweaks$isTransparent = false;

    @ModifyArg(
            method = "getPartColor",
            at = @At(value = "INVOKE", target = "Ljavax/vecmath/Vector4f;<init>(FFFF)V"),
            index = 3,
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsLayerCreatureBase_getPartColorInvis(float alpha, @Local(argsOnly = true) BaseCreatureEntity entity){
        this.lycanitesTweaks$isTransparent = alpha != 1;
        if(entity == null) return alpha;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(entity.isInvisible() && !entity.isInvisibleToPlayer(player)){
            this.lycanitesTweaks$isTransparent = true;
            return 0.15F;
        }

        if(entity.getControllingPassenger() == player) {
            if((KeyHandler.instance.descend.isKeyDown() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountDescend)
                    || (player.isSneaking() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSneak)
                    || (player.isSprinting() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSprint))
            {
                this.lycanitesTweaks$isTransparent = true;
                return 0.15F;
            }
        }
        return alpha;
    }

    @Inject(
            method = "onRenderStart",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsLayerCreatureBase_onRenderStartGlProfile(Entity entity, boolean trophy, CallbackInfo ci){
        if(this.lycanitesTweaks$isTransparent) {
            TransparentObjRenderer.applyGlProfileTransparency();
        }
    }

    @Inject(
            method = "onRenderFinish",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsLayerCreatureBase_onRenderFinishGlProfile(Entity entity, boolean trophy, CallbackInfo ci){
        if(this.lycanitesTweaks$isTransparent) {
            TransparentObjRenderer.cleanGlProfileTransparency();
        }
    }
}
