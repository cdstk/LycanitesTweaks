package lycanitestweaks.client.gui.beastiary.lists;

import lycanitestweaks.client.gui.beastiary.CustomBeastiaryScreen;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityModificationList extends GuiScrollingList {

	private CustomBeastiaryScreen parentGui;
	private GenericEntityList entityList;
	private Map<Integer, AbstractEntityModification> tagModificationMap = new HashMap<>();


	/**
	 * Constructor
	 * @param parentGui The Bestiary GUI using this list.
	 * @param width The width of the list.
	 * @param height The height of the list.
	 * @param top The y position that the list starts at.
	 * @param bottom The y position that the list stops at.
	 * @param x The x position of the list.
	 */
	public EntityModificationList(CustomBeastiaryScreen parentGui, GenericEntityList entityList, int width, int height, int top, int bottom, int x, int slotHeight) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, x, slotHeight, width, height);
		this.parentGui = parentGui;
		this.entityList = entityList;
		if(this.entityList != null) {
			this.entityList.addTagModificationList(this);
		}
		this.refreshList();
	}


	/**
	 * Reloads all items in this list.
	 */
	public void refreshList() {
		// Clear:
		this.tagModificationMap.clear();

		if(this.entityList.getEntityInfo() == null) {
			return;
		}

		int index = 0;
		List<AbstractEntityModification> beastiaryTagModifications = new ArrayList<>(this.entityList.getEntityInfo().bestiaryModifiers);
		if(this.parentGui.genericEntityKnowledge != null) {
			beastiaryTagModifications.removeIf(tagMod -> this.parentGui.genericEntityKnowledge.rank < tagMod.getKnowledgeRankRequirement());
			beastiaryTagModifications.removeIf(tagMod -> this.parentGui.genericEntityKnowledge.getExperienceRatio() < tagMod.getKnowledgeExperienceRequirement());
		}
		for(AbstractEntityModification tagMod : beastiaryTagModifications) {
			this.tagModificationMap.put(index++, tagMod);
		}
	}

	@Override
	protected int getSize() {
		return this.tagModificationMap.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectedIndex = index;
		this.parentGui.onSelectableUpdate();
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
		AbstractEntityModification beastiaryTagModification = this.tagModificationMap.get(index);
		if (beastiaryTagModification == null) return;

		// Name:
		int buffer = 3;
		int nameY = boxTop + buffer;
		this.parentGui.getFontRenderer().drawSplitString(I18n.format(beastiaryTagModification.getOptionLangKey()), this.left + buffer, nameY, this.listWidth - (2 * buffer), 0xFFFFFF);
	}

	public AbstractEntityModification getTagModification() {
		return this.tagModificationMap.get(this.selectedIndex);
	}
}
