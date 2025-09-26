package lycanitestweaks.client.gui.beastiary.lists;

import com.lycanitesmobs.client.gui.beastiary.BeastiaryScreen;
import com.lycanitesmobs.core.info.AltarInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.List;

// Based on CreatureFilterList.class, likely could use gnerics but nah
public class AltarFilterList extends GuiScrollingList {
	protected List<AltarList> filteredLists = new ArrayList<>();
	protected BeastiaryScreen parentGui;

	/**
	 * Constructor
	 * @param width The width of the list.
	 * @param height The height of the list.
	 * @param top The y position that the list starts at.
	 * @param bottom The y position that the list stops at.
	 * @param x The x position of the list.
	 */
	public AltarFilterList(BeastiaryScreen parentGui, int width, int height, int top, int bottom, int x, int slotHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, x, slotHeight, width, height);
		this.parentGui = parentGui;
	}


	/**
	 * Reloads all items in this list.
	 */
	public void refreshList() {}


	@Override
	protected int getSize() {
		return 0;
	}


	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectedIndex = index;
		for(AltarList altarList : this.filteredLists) {
			if(altarList != null) {
				altarList.refreshList();
			}
		}
	}


	@Override
	protected boolean isSelected(int index) {
		return false;
	}
	

	@Override
	protected void drawBackground() {}


    @Override
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight;
    }


	@Override
	protected void drawSlot(int index, int boxRight, int boxTop, int boxBottom, Tessellator tessellator) {}


	/**
	 * Adds a Altar List as a list that should be filtered by this filter list.
	 * @param altarList The Altar List to add and refresh as this filter list changes.
	 */
	public void addFilteredList(AltarList altarList) {
		if(!this.filteredLists.contains(altarList)) {
			this.filteredLists.add(altarList);
		}
	}

	/**
	 * Returns if this filter list allows the provided Altar Info to be added to the display list.
	 * @param altarInfo The Altar info to display.
	 * @param listType The type of Altar List.
	 * @return True if the Altar Info should be included.
	 */
	public boolean canListAltar(AltarInfo altarInfo, AltarList.Type listType) {
		return true;
	}
}
