package lycanitestweaks.mixin.lycanitestweaksmajor.beastiaryclient.scrollfix;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.CreaturesBeastiaryScreen;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureDescriptionList;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureList;
import com.lycanitesmobs.client.gui.beastiary.lists.CreatureTypeList;
import com.lycanitesmobs.client.gui.beastiary.lists.SubspeciesList;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;

@Mixin(CreaturesBeastiaryScreen.class)
public abstract class CreaturesBeastiaryScreen_ScrollFixMixin extends BeastiaryScreen {

    @Shadow(remap = false) public CreatureTypeList creatureTypeList;
    @Shadow(remap = false) public CreatureList creatureList;
    @Shadow(remap = false) public SubspeciesList subspeciesList;
    @Shadow(remap = false) public CreatureDescriptionList descriptionList;

    public CreaturesBeastiaryScreen_ScrollFixMixin(EntityPlayer player) {
        super(player);
    }

    @Unique
    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if(this.creatureTypeList != null) this.creatureTypeList.handleMouseInput(mouseX, mouseY);
        if(this.creatureList != null) this.creatureList.handleMouseInput(mouseX, mouseY);
        if(this.subspeciesList != null) this.subspeciesList.handleMouseInput(mouseX, mouseY);
        if(this.descriptionList != null) this.descriptionList.handleMouseInput(mouseX, mouseY);
    }
}
