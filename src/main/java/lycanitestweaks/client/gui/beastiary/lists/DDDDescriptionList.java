package lycanitestweaks.client.gui.beastiary.lists;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.core.item.ItemBase;
import lycanitestweaks.client.gui.buttons.RenderToggleButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.config.GuiUtils;
import yeelp.distinctdamagedescriptions.integration.hwyla.client.HwylaMobDamageFormatter;
import yeelp.distinctdamagedescriptions.integration.hwyla.client.HwylaMobResistanceFormatter;

public class DDDDescriptionList extends GuiScrollingList {

    public RenderToggleButton renderToggleButton;

    protected BeastiaryScreen parentGui;

    public static final int LIST_WIDTH = (int)(ItemBase.DESCRIPTION_WIDTH * 1.6);

    /**
     * Constructor
     * @param width The width of the list.
     * @param height The height of the list.
     * @param top The y position that the list starts at.
     * @param bottom The y position that the list stops at.
     * @param x The x position of the list.
     */
    public DDDDescriptionList(BeastiaryScreen parentGui, int width, int height, int top, int bottom, int x, RenderToggleButton renderToggleButton) {
        super(Minecraft.getMinecraft(), width, height, top, bottom, x, 10800, width, height);
        this.parentGui = parentGui;
        this.renderToggleButton = renderToggleButton;
    }


    @Override
    protected int getSize() {
        return 1;
    }


    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.selectedIndex = index;
    }


    @Override
    protected boolean isSelected(int index) {
        return false;
    }


    @Override
    protected void drawBackground() {}


    @Override
    protected int getContentHeight() {
        return this.parentGui.getFontRenderer().getWordWrappedHeight(this.getContent(), this.listWidth) + 10;
    }


    @Override
    protected void drawSlot(int index, int boxRight, int boxTop, int boxBottom, Tessellator tessellator) {
        if(index == 0) {
            this.parentGui.drawSplitString(this.getContent(), this.left + 6, boxTop, DDDDescriptionList.LIST_WIDTH - 12, 0xFFFFFF, true);
        }
    }


    public String getContent() {
        StringBuilder textBuilder = new StringBuilder();
        if(this.parentGui.creaturePreviewEntity != null) {
            // Stats:
            HwylaMobDamageFormatter.getInstance().format(this.parentGui.creaturePreviewEntity).forEach((line) -> textBuilder.append(line).append("\n"));
            textBuilder.append("\n");
            HwylaMobResistanceFormatter.getInstance().format(this.parentGui.creaturePreviewEntity).forEach((line) -> textBuilder.append(line).append("\n"));
            textBuilder.append("\n").append("\n");
        }
        return textBuilder.toString();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        if(!this.renderToggleButton.enabled) super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /** Overridden to change the background gradient without copying over an entire function. **/
    @Override
    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        color1 = 0xFF101010;
        color2 = color1;
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }
}
