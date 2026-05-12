package lycanitestweaks.client.gui.beastiary.lists;

import lycanitestweaks.client.gui.beastiary.CustomBeastiaryScreen;
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

public class GenericEntityList extends GuiScrollingList {

	private CustomBeastiaryScreen parentGui;
	private ModInfoList modInfoList;
	private List<EntityModificationList> entityModificationLists = new ArrayList<>();
	private Map<Integer, GenericEntityInfo> entityInfoMap = new HashMap<>();

	/**
	 * Constructor
	 * @param parentGui The Beastiary GUI using this list.
	 * @param modInfoList A mod info list used to filter this list by mod
	 * @param width The width of the list.
	 * @param height The height of the list.
	 * @param top The y position that the list starts at.
	 * @param bottom The y position that the list stops at.
	 * @param x The x position of the list.
	 */
	public GenericEntityList(CustomBeastiaryScreen parentGui, ModInfoList modInfoList, int width, int height, int top, int bottom, int x, int slotHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, x, slotHeight, width, height);
		this.parentGui = parentGui;
		this.modInfoList = modInfoList;
		if(this.modInfoList != null) {
			this.modInfoList.addEntityInfoList(this);
		}
		this.refreshList();
	}


	/**
	 * Reloads all items in this list.
	 */
	public void refreshList() {
		// Clear:
		this.entityInfoMap.clear();
		int creatureIndex = 0;

		List<GenericEntityInfo> entityInfos = new ArrayList<>(GenericEntityInfoManager.getInstance().entities.values());
		entityInfos.removeIf(entityInfo -> entityInfo.disableBestiaryEntry);
		entityInfos.removeIf(entityInfo -> this.modInfoList != null && !this.modInfoList.canListEntity(entityInfo));
		entityInfos.removeIf(entityInfo -> !this.parentGui.ltp.getBestiary().entityKnowledgeMap.containsKey(entityInfo.getEntityId()));
		entityInfos.sort(Comparator.comparing(GenericEntityInfo::getLocalizedName));
		for(GenericEntityInfo entityInfo : entityInfos) {
			this.entityInfoMap.put(creatureIndex++, entityInfo);
		}
	}

	@Override
	protected int getSize() {
		return entityInfoMap.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectedIndex = index;
		this.parentGui.onSelectableUpdate();
		this.entityModificationLists.forEach(EntityModificationList::refreshList);
	}

	@Override
	protected boolean isSelected(int index) {
		return this.selectedIndex == index;
	}

	@Override
	protected void drawBackground() {

	}

	@Override
	protected void drawSlot(int index, int boxRight, int boxTop, int boxBottom, Tessellator tessellator) {
		GenericEntityInfo entityInfo = this.entityInfoMap.get(index);
		if (entityInfo == null) return;

		// Name:
		int buffer = 3;
		int nameY = boxTop + buffer;
		this.parentGui.getFontRenderer().drawSplitString(entityInfo.getLocalizedName(), this.left + buffer, nameY, this.listWidth - (2 * buffer), 0xFFFFFF);
	}

	public void addTagModificationList(EntityModificationList entityModificationList) {
		if(!this.entityModificationLists.contains(entityModificationList)) {
			this.entityModificationLists.add(entityModificationList);
		}
	}

	public GenericEntityInfo getEntityInfo() {
		return this.entityInfoMap.get(this.selectedIndex);
	}
}

