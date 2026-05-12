package lycanitestweaks.client.gui.beastiary.lists;

import lycanitestweaks.client.gui.beastiary.CustomBeastiaryScreen;
import lycanitestweaks.info.beastiary.BeastiaryModInfo;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModInfoList extends GuiScrollingList {

	private Map<Integer, BeastiaryModInfo> modInfoList = new HashMap<>();
	private List<GenericEntityList> entityInfoLists = new ArrayList<>();
	protected CustomBeastiaryScreen parentGui;

	/**
	 * Constructor
	 * @param width The width of the list.
	 * @param height The height of the list.
	 * @param top The y position that the list starts at.
	 * @param bottom The y position that the list stops at.
	 * @param x The x position of the list.
	 */
	public ModInfoList(CustomBeastiaryScreen parentGui, int width, int height, int top, int bottom, int x, int slotHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, x, slotHeight, width, height);
		this.parentGui = parentGui;
		this.refreshList();
	}

	public void refreshList() {
		this.modInfoList.clear();

		int groupIndex = 0;
        List<BeastiaryModInfo> modInfos = new ArrayList<>(GenericEntityInfoManager.getInstance().modInfos.values());
		modInfos.removeIf(modInfo -> modInfo.disableAllBestiaryEntries);
		modInfos.removeIf(modInfo -> this.parentGui.ltp.getBestiary().getCreaturesDiscovered(modInfo) == 0);
		modInfos.sort(Comparator.comparing(BeastiaryModInfo::getTitle));
		for (BeastiaryModInfo modInfo : modInfos) {
			this.modInfoList.put(groupIndex++, modInfo);
		}
	}

	@Override
	protected int getSize() {
		return this.modInfoList.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectedIndex = index;
		this.parentGui.onSelectableUpdate();
		this.entityInfoLists.forEach(GenericEntityList::refreshList);
	}

	@Override
	protected boolean isSelected(int index) {
		return this.selectedIndex == index;
	}

	@Override
	protected void drawBackground() {}

	@Override
	protected void drawSlot(int index, int boxRight, int boxTop, int boxBottom, Tessellator tessellator) {
		BeastiaryModInfo modInfo = this.modInfoList.get(index);
		if(modInfo == null) {
			return;
		}
		int buffer = 2;
		this.parentGui.getFontRenderer().drawSplitString(modInfo.getTitle(), this.left + buffer, boxTop + buffer, this.listWidth - (2 * buffer), 0xFFFFFF);
	}


	/**
	 * Adds a Entity List as a list that should be filtered by this filter list.
	 * @param entityList The Entity List to add and refresh as this filter list changes.
	 */
	public void addEntityInfoList(GenericEntityList entityList) {
		if(!this.entityInfoLists.contains(entityList)) {
			this.entityInfoLists.add(entityList);
		}
	}


	/**
	 * Returns if this filter list allows the provided Entity Info to be added to the display list.
	 * @param entityInfo The Entity info to display.
	 * @return True if the Entity Info should be included.
	 */
	public boolean canListEntity(GenericEntityInfo entityInfo) {
		if(this.modInfoList.get(this.selectedIndex) != null) {
			return this.modInfoList.get(this.selectedIndex) == entityInfo.getModInfo();
		}

		return false;
	}

	public BeastiaryModInfo getModInfo() {
		return this.modInfoList.get(this.selectedIndex);
	}
}
