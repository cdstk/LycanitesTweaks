package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient.scrollfix;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.SummoningBeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureList;
import com.lycanitesmobs.client.gui.beastiary.lists.SubspeciesList;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

@Mixin(SummoningBeastiaryScreen.class)
public abstract class SummoningBeastiaryScreen_ScrollFixMixin extends BeastiaryScreen {

    @Shadow(remap = false) public CreatureList petList;
    @Shadow(remap = false) public SubspeciesList subspeciesList;

    public SummoningBeastiaryScreen_ScrollFixMixin(EntityPlayer player) {
        super(player);
    }

    @Unique
    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if(this.petList != null) this.petList.handleMouseInput(mouseX, mouseY);
        if(this.subspeciesList != null) this.subspeciesList.handleMouseInput(mouseX, mouseY);
    }
}
