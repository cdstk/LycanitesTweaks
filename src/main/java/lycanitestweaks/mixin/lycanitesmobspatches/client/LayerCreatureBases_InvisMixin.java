package lycanitestweaks.mixin.lycanitesmobspatches.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.KeyHandler;
import com.lycanitesmobs.client.renderer.LayerCreatureDye;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureDjinn;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureEffect;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureGlow;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureRoa;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureShield;
import com.lycanitesmobs.client.renderer.layer.LayerCreatureThresher;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
        LayerCreatureDye.class,
        LayerCreatureDjinn.class,
        LayerCreatureEffect.class,
        LayerCreatureGlow.class,
        LayerCreatureRoa.class,
        LayerCreatureShield.class,
        LayerCreatureThresher.class
})
public abstract class LayerCreatureBases_InvisMixin {

    @ModifyArg(
            method = "getPartColor",
            at = @At(value = "INVOKE", target = "Ljavax/vecmath/Vector4f;<init>(FFFF)V"),
            index = 3,
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsLayerCreatureBases_getPartColorInvis(float alpha, @Local(argsOnly = true) BaseCreatureEntity entity){
        if(entity == null) return alpha;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(entity.isInvisible() && !entity.isInvisibleToPlayer(player)){
            return 0.15F;
        }

        if(entity.getControllingPassenger() == player) {
            if((KeyHandler.instance.descend.isKeyDown() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountDescend)
                    || (player.isSneaking() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSneak)
                    || (player.isSprinting() && ForgeConfigHandler.mixinPatchesConfig.renderInvisMountSprint))
                return 0.15F;
        }
        return alpha;
    }
}
