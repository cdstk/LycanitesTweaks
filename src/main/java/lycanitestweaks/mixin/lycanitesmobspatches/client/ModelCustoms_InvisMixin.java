package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.lycanitesmobs.client.KeyHandler;
import com.lycanitesmobs.client.model.creature.ModelAegis;
import com.lycanitesmobs.client.model.creature.ModelJengu;
import com.lycanitesmobs.client.model.creature.ModelNymph;
import com.lycanitesmobs.client.model.creature.ModelReiver;
import com.lycanitesmobs.client.model.creature.ModelSylph;
import com.lycanitesmobs.client.model.creature.ModelVapula;
import com.lycanitesmobs.client.model.creature.ModelWisp;
import com.lycanitesmobs.client.model.creature.ModelWraith;
import com.lycanitesmobs.client.model.creature.ModelZephyr;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureBase;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.vecmath.Vector4f;

@Mixin(value = {
        ModelAegis.class,
        ModelJengu.class,
        ModelNymph.class,
        ModelReiver.class,
        ModelSylph.class,
        ModelVapula.class,
        ModelWisp.class,
        ModelWraith.class,
        ModelZephyr.class
})
public abstract class ModelCustoms_InvisMixin {

    @Inject(
            method = "getPartColor",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsModelCustoms_getPartColorInvis(String partName, Entity entity, LayerCreatureBase layer, boolean trophy, float loop, CallbackInfoReturnable<Vector4f> cir){
        if(entity == null) return;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(entity != player && entity.isInvisible() && !entity.isInvisibleToPlayer(player)){
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
