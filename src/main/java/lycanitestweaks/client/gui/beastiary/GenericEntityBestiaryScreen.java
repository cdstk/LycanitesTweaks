package lycanitestweaks.client.gui.beastiary;

import com.lycanitesmobs.GuiHandler;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.client.localisation.LanguageManager;
import lycanitestweaks.client.gui.beastiary.lists.DDDDescriptionList;
import lycanitestweaks.client.gui.beastiary.lists.EntityModificationList;
import lycanitestweaks.client.gui.beastiary.lists.GenericEntityDescriptionList;
import lycanitestweaks.client.gui.beastiary.lists.GenericEntityList;
import lycanitestweaks.client.gui.beastiary.lists.ModInfoList;
import lycanitestweaks.client.gui.buttons.RenderToggleButton;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.mixin.vanilla.EntityLiving_InvokerMixin;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GenericEntityBestiaryScreen extends CustomBeastiaryScreen {

	public static final byte BEASTIARY_GENERIC_ID = nextBeastiaryId++;;
	public static final int NBT_FIELD_ID = nextInputID--;
	public static final int APPLY_NBT_BUTTON_ID = nextInputID--;

	public ModInfoList modInfoList;
	public GenericEntityList entityList;
	public EntityModificationList modificationList;
	public GenericEntityDescriptionList descriptionList;
	public DDDDescriptionList dddDescriptionList = null;

	public RenderToggleButton renderDescriptionButton;
	public RenderToggleButton renderDDDButton;
	private GuiTextField customInputField;
	private GuiButton applyModificationButton;

	/**
	 * Opens this GUI up to the provided player.
	 * @param player The player to open the GUI to.
	 */
	public static void openToPlayer(EntityPlayer player) {
		if(player != null) {
			player.openGui(
					LycanitesMobs.instance,
					GuiHandler.GuiType.BEASTIARY.id,
					player.getEntityWorld(),
					BEASTIARY_GENERIC_ID,
					0,
					0
			);
		}
	}


	public GenericEntityBestiaryScreen(EntityPlayer player) {
		super(player);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(customInputField.textboxKeyTyped(typedChar, keyCode)) return;
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.customInputField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		super.handleMouseInput();
		if(this.modInfoList != null) this.modInfoList.handleMouseInput(mouseX, mouseY);
		if(this.entityList != null) this.entityList.handleMouseInput(mouseX, mouseY);
		if(this.modificationList != null) this.modificationList.handleMouseInput(mouseX, mouseY);
		if(this.descriptionList != null) this.descriptionList.handleMouseInput(mouseX, mouseY);
		if(this.dddDescriptionList != null) this.dddDescriptionList.handleMouseInput(mouseX, mouseY);
	}

	@Override
	public String getTitle() {
		if(this.entityList != null && this.entityList.getEntityInfo() != null) {
			return "";
//			return this.entityList.getEntityInfo().getTitle();
		}
		if(this.ltp.getBestiary().entityKnowledgeMap.isEmpty()) {
			I18n.format("gui.beastiary.creatures.empty.title");
		}
		return I18n.format("gui.beastiary.creatures");
	}


	@Override
	public void initControls() {
		super.initControls();

		this.modInfoList = new ModInfoList(this, this.colLeftWidth, this.colLeftHeight, this.colLeftY,this.colLeftY + this.colLeftHeight, this.colLeftX, 24);

		int selectionListsWidth = this.getScaledX(240F / 1920F);

		int creatureListY = this.colRightY;
		int creatureListHeight = Math.round((float)this.colRightHeight * 0.6f);
		this.entityList = new GenericEntityList(this, this.modInfoList, selectionListsWidth, creatureListHeight, creatureListY,creatureListY + creatureListHeight, this.colRightX, 32);
		this.entityList.registerScrollButtons(this.buttonList, 1, 1);

		int subspeciesListY = creatureListY + 2 + creatureListHeight;
		int subspeciesListHeight = Math.round((float)this.colRightHeight * 0.4f) - 2;
		this.modificationList = new EntityModificationList(this, this.entityList, selectionListsWidth, subspeciesListHeight, subspeciesListY,subspeciesListY + subspeciesListHeight, this.colRightX, 24);

		int newLine = this.getFontRenderer().getWordWrappedHeight("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", this.colRightWidth - selectionListsWidth) + 2;
		int descriptionListY = this.colRightY + (newLine * 3);
		this.descriptionList = new GenericEntityDescriptionList(this, this.entityList, this.colRightWidth - selectionListsWidth, this.colRightHeight, descriptionListY, this.colRightY + this.colRightHeight, this.colRightX + selectionListsWidth + 2);

		this.applyModificationButton = new GuiButton(APPLY_NBT_BUTTON_ID, this.colRightX, subspeciesListY + subspeciesListHeight + 2 , selectionListsWidth, 20, "Default Label");
		this.applyModificationButton.visible = false;
		this.buttonList.add(applyModificationButton);

		this.customInputField = new GuiTextField(NBT_FIELD_ID, this.getFontRenderer(), this.colRightX, subspeciesListY + subspeciesListHeight + this.applyModificationButton.height + 4, selectionListsWidth, 20);
		this.customInputField.setMaxStringLength(256);
		this.customInputField.setVisible(false);
		this.customInputField.setFocused(true);

		int height = (int) (DDDDescriptionList.LIST_WIDTH / 3.25);
		int top = (int) (this.colRightCenterY * 0.75);
		int bottom = top + height;
		int xPos = (int) (this.colRightCenterX * 0.7);

		this.renderDDDButton = new RenderToggleButton(RenderToggleButton.BUTTON_ID, this.colRightX * 3, this.colRightY, 80, 20, I18n.format("gui.beastiary.creatures.mixin.ddd"));
		if(ModLoadedUtil.ddd.isLoaded()) {
			this.buttonList.add(this.renderDDDButton);
			this.dddDescriptionList = new DDDDescriptionList(this, DDDDescriptionList.LIST_WIDTH, height, top, bottom, xPos, this.renderDDDButton);
		}

		this.renderDescriptionButton = new RenderToggleButton(RenderToggleButton.BUTTON_ID - 1, this.colRightX + selectionListsWidth + 2, this.colRightY + this.colRightHeight + 2 , 80, 20, I18n.format("gui.bestiary.button.description"));
		this.renderDescriptionButton.visible = false;
		this.buttonList.add(this.renderDescriptionButton);
	}

	@Override
	protected void updateControls(int mouseX, int mouseY, float partialTicks) {
		super.updateControls(mouseX, mouseY, partialTicks);

		this.modInfoList.drawScreen(mouseX, mouseY, partialTicks);
		if(this.modInfoList.getModInfo() != null) {
			this.entityList.drawScreen(mouseX, mouseY, partialTicks);
		}
		if(this.entityList.getEntityInfo() != null) {
			this.modificationList.drawScreen(mouseX, mouseY, partialTicks);
			this.customInputField.drawTextBox();
		}
	}


	@Override
	public void drawForeground(int mouseX, int mouseY, float partialTicks) {
		super.drawForeground(mouseX, mouseY, partialTicks);

		int marginX = this.getScaledX(240F / 1920F) + 8;
		int nextX = this.colRightX + marginX;
		int nextY = this.colRightY;
		int width = this.colRightWidth - marginX;

		if(this.ltp.getBestiary().entityKnowledgeMap.isEmpty()) {
			nextY += 20;
			String text = I18n.format("gui.beastiary.creatures.empty.info");
			this.drawSplitString(text, this.colRightX, nextY, this.colRightWidth, 0xFFFFFF, true);
			return;
		}

		// Creature Display:
		if(this.entityList.getEntityInfo() != null) {
			GenericEntityInfo entityInfo = this.entityList.getEntityInfo();

			// Model:
			this.renderEntity(entityInfo, this.colRightX + (marginX / 2) + (this.colRightWidth / 2), this.colRightY + 100, mouseX, mouseY, partialTicks);

			// Knowledge Rank:
			String text = "§l" + I18n.format("creature.stat.knowledge") + ": §r";
			int rankX = nextX + this.getFontRenderer().getStringWidth(text);
			int barX = rankX + 29;
			int barWidth = (256 / 4) + 16;
			int barHeight = (32 / 4) + 2;
			int barCenter = barX + (barWidth / 2);

			this.getFontRenderer().drawString(text, nextX, nextY, 0xFFFFFF, true);
			this.drawBar(AssetManager.getTexture("GUIPetSpiritEmpty"), rankX, nextY, 0, 9, 9, 3, 10);
			this.drawTexture(AssetManager.getTexture("GUIPetBarEmpty"), barX, nextY, 0, 1, 1, barWidth, barHeight);

			if(this.genericEntityKnowledge != null) {
				this.drawBar(AssetManager.getTexture("GUIPetSpiritUsed"), rankX, nextY, 0, 9, 9, this.genericEntityKnowledge.rank, 10);

				this.drawBar(AssetManager.getTexture("GUIPetSpiritUsed"), rankX, nextY, 0, 9, 9, this.genericEntityKnowledge.rank, 10);
				float experienceNormal = 1;
				if (this.genericEntityKnowledge.getMaxExperience() > 0) {
					experienceNormal = (float)this.genericEntityKnowledge.experience / this.genericEntityKnowledge.getMaxExperience();
				}
				this.drawTexture(AssetManager.getTexture("GUIBarExperience"), barX, nextY, 0, experienceNormal, 1, barWidth * experienceNormal, barHeight);
				String experienceText = "100%";
				if (this.genericEntityKnowledge.getMaxExperience() > 0) {
					experienceText = this.genericEntityKnowledge.experience + "/" + this.genericEntityKnowledge.getMaxExperience();
				}
				this.getFontRenderer().drawString(experienceText, Math.round(barCenter - ((float)this.getFontRenderer().getStringWidth(experienceText) / 2)), nextY + 2, 0xFFFFFF);
			}

			// Description:
			if(!this.renderDescriptionButton.enabled) this.descriptionList.drawScreen(mouseX, mouseY, partialTicks);
		}
		// Creature Type Display:
		else if(this.modInfoList.getModInfo() != null) {
			// Descovered:
			nextY += 20;
			String text = LanguageManager.translate("gui.beastiary.creatures.discovered") + ": ";
			text += this.ltp.getBestiary().getCreaturesDiscovered(this.modInfoList.getModInfo());
			text += "/" + this.modInfoList.getModInfo().entityInfoMap.size();
			this.getFontRenderer().drawString(text, nextX, nextY, 0xFFFFFF, true);
		}
		// Base Display:
		else {
			nextY += 20;
			String text = LanguageManager.translate("gui.beastiary.creatures.select");
			this.drawSplitString(text, this.colRightX, nextY, this.colRightWidth, 0xFFFFFF, true);
		}

		if (this.dddDescriptionList != null && this.creaturePreviewEntity != null) {
			this.renderDDDButton.visible = true;
			this.dddDescriptionList.drawScreen(mouseX, mouseY, partialTicks);
		} else
			this.renderDDDButton.visible = false;
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if(guiButton.id == APPLY_NBT_BUTTON_ID) {
			if(this.creaturePreviewEntity != null) {
				AbstractEntityModification tagMod = this.modificationList.getTagModification();
				if(tagMod != null) {
					if(tagMod.takesUserInput()) {
						tagMod.modifyEntity(this.creaturePreviewEntity, this.customInputField.getText());
					}
					else {
						tagMod.modifyEntity(this.creaturePreviewEntity);
					}
					if(tagMod.refreshEntityData() && this.entityList.getEntityInfo() != null) {
						if(this.creaturePreviewEntity instanceof EntityLiving_InvokerMixin) {
							SoundEvent ambientSound = ((EntityLiving_InvokerMixin) this.creaturePreviewEntity).lycanitesTweaks$invokeGetAmbientSound();
							if(ambientSound != null) {
								this.player.getEntityWorld().playSound(this.player, this.player.posX, this.player.posY, this.player.posZ, ambientSound, SoundCategory.NEUTRAL, 1, 1);
							}
						}
						this.entityList.getEntityInfo().getDataFromEntity(this.creaturePreviewEntity);
						this.onSelectableUpdate();
					}
					this.applyModificationButton.displayString = I18n.format(tagMod.getActionLangKey());
				}
			}
		}
		this.descriptionList.refreshList();
	}

	@Override
	public void onSelectableUpdate() {
		GenericEntityInfo entityInfo = this.entityList.getEntityInfo();
		if(entityInfo != null) {
			this.genericEntityKnowledge = this.ltp.getBestiary().getCreatureKnowledge(entityInfo.getEntityId());
		}

		AbstractEntityModification tagMod = this.modificationList.getTagModification();
		if(tagMod != null) {
			if(tagMod.takesUserInput()) {
				this.customInputField.setEnabled(true);
				this.customInputField.setFocused(true);
				this.customInputField.setVisible(true);
			}
			else {
				this.customInputField.setEnabled(false);
				this.customInputField.setFocused(false);
				this.customInputField.setVisible(false);
			}
			this.applyModificationButton.displayString = I18n.format(tagMod.getActionLangKey());
			this.applyModificationButton.visible = true;
		}
		else {
			this.customInputField.setVisible(false);
			this.applyModificationButton.visible = false;
		}
		this.renderDescriptionButton.visible = this.entityList.getEntityInfo() != null;
		this.descriptionList.refreshList();
	}
}
