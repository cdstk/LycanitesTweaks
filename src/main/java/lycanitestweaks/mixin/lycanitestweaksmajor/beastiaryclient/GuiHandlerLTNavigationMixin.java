package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient;

import com.lycanitesmobs.GuiHandler;
import lycanitestweaks.client.gui.beastiary.AltarsBeastiaryScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiHandler.class)
public abstract class GuiHandlerLTNavigationMixin {

    @Inject(
            method = "getClientGuiElement",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    public void lycanitesTweaks_lycanitesMobsGuiHandler_getClientGuiElementLT(int id, EntityPlayer player, World world, int x, int y, int z, CallbackInfoReturnable<Object> cir){
        if(id == GuiHandler.GuiType.BEASTIARY.id) {
            if (x == AltarsBeastiaryScreen.BEASTIARY_ALTAR_ID) {
                cir.setReturnValue(new AltarsBeastiaryScreen(player));
            }
        }
    }
}
