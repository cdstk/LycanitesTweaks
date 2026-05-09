package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient.scrollfix;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.PetsBeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureFilterList;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureList;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

@Mixin(PetsBeastiaryScreen.class)
public abstract class PetsBeastiaryScreen_ScrollFixMixin extends BeastiaryScreen {

    @Shadow(remap = false) public CreatureFilterList petTypeList;
    @Shadow(remap = false) public CreatureList petList;

    public PetsBeastiaryScreen_ScrollFixMixin(EntityPlayer player) {
        super(player);
    }

    @Unique
    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if(this.petTypeList != null) this.petTypeList.handleMouseInput(mouseX, mouseY);
        if(this.petList != null) this.petList.handleMouseInput(mouseX, mouseY);
    }
}
