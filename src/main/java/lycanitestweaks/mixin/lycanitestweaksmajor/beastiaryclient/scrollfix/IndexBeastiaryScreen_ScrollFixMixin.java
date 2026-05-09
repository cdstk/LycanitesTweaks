package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient.scrollfix;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.IndexBeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.BeastiaryIndexList;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

@Mixin(IndexBeastiaryScreen.class)
public abstract class IndexBeastiaryScreen_ScrollFixMixin extends BeastiaryScreen {

    @Shadow(remap = false) public BeastiaryIndexList indexList;

    public IndexBeastiaryScreen_ScrollFixMixin(EntityPlayer player) {
        super(player);
    }

    @Unique
    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if(this.indexList != null) this.indexList.handleMouseInput(mouseX, mouseY);
    }
}
