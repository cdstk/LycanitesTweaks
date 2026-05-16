package lycanitestweaks.client.gui.beastiary.lists;

import lycanitestweaks.client.gui.beastiary.CustomBeastiaryScreen;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.GenericEntityKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GenericEntityDescriptionList extends GuiScrollingList {

	protected CustomBeastiaryScreen parentGui;
	protected GenericEntityList entityList;
	protected List<String> descriptionList = new ArrayList<>();

	public int textBuffer = 10;

	/**
	 * Constructor
	 * @param width The width of the list.
	 * @param height The height of the list.
	 * @param top The y position that the list starts at.
	 * @param bottom The y position that the list stops at.
	 * @param x The x position of the list.
	 */
	public GenericEntityDescriptionList(CustomBeastiaryScreen parentGui, GenericEntityList entityList, int width, int height, int top, int bottom, int x) {
		super(Minecraft.getMinecraft(), width, height, top, bottom, x, parentGui.getFontRenderer().FONT_HEIGHT, width, height);
		this.parentGui = parentGui;
		this.entityList = entityList;
		this.refreshList();
	}

	public int getTextWidth() {
		return this.listWidth - (2 * textBuffer);
	}

	/**
	 * Reloads all items in this list.
	 */
	public void refreshList() {
		// Clear:
		this.descriptionList.clear();

		this.descriptionList.addAll(this.parentGui.getFontRenderer().listFormattedStringToWidth(this.getContent(), this.getTextWidth()));
		this.descriptionList.add("");
		this.descriptionList.add("");
	}

	@Override
	protected int getSize() {
		return this.descriptionList.size();
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
	protected void drawBackground() {
		if(this.parentGui.player.ticksExisted % 20 == 0) this.refreshList();
	}

	@Override
	protected void drawSlot(int index, int boxRight, int boxTop, int boxBottom, Tessellator tessellator) {
		if(index < this.descriptionList.size()) {
			// Name:
			int nameY = boxTop + textBuffer;
			this.parentGui.getFontRenderer().drawSplitString(this.descriptionList.get(index), this.left + textBuffer, nameY, this.getTextWidth(), 0xFFFFFF);
		}
	}

	public String getContent() {
		GenericEntityInfo creatureInfo = this.entityList.getEntityInfo();
		GenericEntityKnowledge entityKnowledge = this.parentGui.genericEntityKnowledge;
		if(creatureInfo == null || entityKnowledge == null) return "";

		StringBuilder stringBuilder = new StringBuilder();
		String infoText = "";

		// Potential Spawn:
		if(creatureInfo.potentialSpawn) {
			stringBuilder.append(I18n.format("gui.bestiary.potentialspawn")).append("\n");
			stringBuilder.append(I18n.format("gui.bestiary.potentialspawn.true")).append("\n");
			stringBuilder.append("\n");
		}

		stringBuilder.append(I18n.format("gui.bestiary.properties")).append("\n");
		if(entityKnowledge.getRankWithKnowledge() >= 1.25F) {
			if (creatureInfo.boss) {
				stringBuilder.append(I18n.format("gui.bestiary.properties.boss")).append("\n");
			}

			if (creatureInfo.animal) {
				stringBuilder.append(I18n.format("gui.bestiary.properties.animal")).append("\n");
			}

			// Taming:
			if (creatureInfo.tameable) {
				stringBuilder.append(I18n.format("gui.bestiary.properties.tameable")).append("\n");
			}

			// Perching:
			if (creatureInfo.perchable) {
				stringBuilder.append(I18n.format("gui.bestiary.properties.perchable")).append("\n");
			}

			// Groups:
			if(this.parentGui.creaturePreviewEntity != null) {
				EnumCreatureAttribute creatureAttribute = this.parentGui.creaturePreviewEntity.getCreatureAttribute();
				if(creatureAttribute == EnumCreatureAttribute.UNDEFINED) {
					stringBuilder.append(I18n.format("gui.bestiary.properties.creatureattribute.undefined")).append("\n");
				}
				else {
					stringBuilder.append(I18n.format("gui.bestiary.properties.creatureattribute", creatureAttribute)).append("\n");
				}
			}
		}
		else {
			stringBuilder.append(I18n.format("gui.bestiary.knowledge.requirement", 1, 25F)).append("\n");
		}
		stringBuilder.append("\n");

		// Combat:
		stringBuilder.append("§l").append(I18n.format("gui.beastiary.combat")).append(": §r\n");
		if(entityKnowledge.getRankWithKnowledge() >= 1.25F) {
			List<String> combat = creatureInfo.getCombatDescription();
			if(combat.isEmpty()) {
				stringBuilder.append(I18n.format("gui.bestiary.combat.none")).append("\n");
			}
			else {
				combat.forEach(entry -> stringBuilder.append(entry).append("\n"));
			}
		}
		else {
			stringBuilder.append(I18n.format("gui.bestiary.knowledge.requirement", 1, 25F)).append("\n");
		}
		stringBuilder.append("\n");

		// Stats:
		stringBuilder.append("§l").append(I18n.format("creature.stat.base")).append(": §r");
		if(entityKnowledge.getRankWithKnowledge() >= 2.0F) {
			// Stats:
			stringBuilder.append("\n").append(I18n.format("creature.stat.health")).append(": ").append(String.format("%.1f", creatureInfo.health));
			stringBuilder.append("\n").append(I18n.format("creature.stat.armor")).append(": ").append(String.format("%.2f", creatureInfo.armor));
//			stringBuilder.append("\n").append(I18n.format("creature.stat.armorToughness")).append(": ").append(String.format("%.2f", creatureInfo.armorToughness));
			stringBuilder.append("\n").append(I18n.format("creature.stat.speed")).append(": ").append(String.format("%.2f", creatureInfo.speed));
			if(creatureInfo.knockbackResistance != 0D) stringBuilder.append("\n").append(I18n.format("creature.stat.knockbackresistance")).append(": ").append(String.format("%.1f", creatureInfo.knockbackResistance));
			if(creatureInfo.damage != -1D) stringBuilder.append("\n").append(I18n.format("creature.stat.damage")).append(": ").append(String.format("%.1f", creatureInfo.damage));
			if(creatureInfo.followRange != -1D) stringBuilder.append("\n").append(I18n.format("creature.stat.followRange")).append(": ").append(String.format("%.1f", creatureInfo.followRange));
			stringBuilder.append("\n").append(I18n.format("creature.stat.experience")).append(": ").append(creatureInfo.experience);
		}
		else {
			stringBuilder.append("\n").append(I18n.format("gui.beastiary.unlockedat")).append(" ").append(I18n.format("creature.stat.knowledge")).append(" 2");
		}
		stringBuilder.append("\n\n");

		// Summary:
		infoText = creatureInfo.getDescription();
		if(!infoText.isEmpty()) {
			stringBuilder.append("§l").append(I18n.format("gui.beastiary.summary")).append(": §r");
			if(entityKnowledge.getRankWithKnowledge() >= 2.0F) {
				stringBuilder.append("\n").append(infoText).append("\n");
			}
			else {
				stringBuilder.append("\n").append(I18n.format("gui.beastiary.unlockedat")).append(" ").append(I18n.format("creature.stat.knowledge")).append(" 2");
			}
			stringBuilder.append("\n\n");
		}

		// Biomes:
		stringBuilder.append("§l").append(I18n.format("gui.beastiary.biomes")).append(": §r").append("\n");
		if(entityKnowledge.getRankWithKnowledge() >= 1.5F) {
			infoText = creatureInfo.getBiomeNames();
			if(!infoText.isEmpty()) {
				stringBuilder.append(infoText);
			}
			else {
				stringBuilder.append(I18n.format("gui.bestiary.biomes.none")).append("\n");
			}
		}
		else {
			stringBuilder.append(I18n.format("gui.bestiary.knowledge.requirement", 1, 50F)).append("\n");
		}

		return stringBuilder.toString();
	}

	/** Overridden to change the background gradient without copying over an entire function. **/
	@Override
	protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
		color1 = 0x33101010;
		color2 = color1;
		GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
	}
}
