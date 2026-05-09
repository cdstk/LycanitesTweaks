package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient.scrollfix;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.ElementsBeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.ElementDescriptionList;
import com.lycanitesmobs.client.gui.beastiary.lists.ElementList;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

@Mixin(ElementsBeastiaryScreen.class)
public abstract class ElementsBeastiaryScreen_ScrollFixMixin extends BeastiaryScreen {

    @Shadow(remap = false) protected ElementList elementList;
    @Shadow(remap = false) protected ElementDescriptionList descriptionList;

    public ElementsBeastiaryScreen_ScrollFixMixin(EntityPlayer player) {
        super(player);
    }

    @Unique
    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if(this.elementList != null) this.elementList.handleMouseInput(mouseX, mouseY);
        if(this.descriptionList != null) this.descriptionList.handleMouseInput(mouseX, mouseY);
    }
}
