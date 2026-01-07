package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.summonstafflevelmap;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.temp.ItemScepter;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.IItemInfuserDisplay_Mixin;
import lycanitestweaks.util.IItemStaffSummoningElementLevelMapMixin;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemStaffSummoning.class)
public abstract class ItemStaffSummoning_ElementLevelMapMixin extends ItemScepter implements IItemStaffSummoningElementLevelMapMixin, IItemInfuserDisplay_Mixin {

    // Could have been a Forge Cap, but it looked like Forge was being inefficient with it

    // Reference to stack to get NBT as most Lycanites usages of Summon Staffs don't have convenient itemStack access
    @Unique
    public ItemStack lycanitesTweaks$stack = null;

    @Inject(
            method = "applyMinionEffects",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsItemStaffSummoning_applyMinionEffectsLevelMap(BaseCreatureEntity minion, CallbackInfo ci){
        minion.addLevel(this.lycanitesTweaks$getHighestLevel(minion.getElements()) - 1);
    }

    @Unique
    @SideOnly(Side.CLIENT)
    public ElementInfo lycanitesTweaks$getHighestLevelElement(ItemStack stack, List<ElementInfo> elements) {
        ElementInfo highestElement = null;
        int level = 0;
        for(ElementInfo elementInfo : elements){
            if(lycanitesTweaks$hasElement(stack, elementInfo)) {
                if(level < this.lycanitesTweaks$getLevel(stack, elementInfo.name)){
                    level = this.lycanitesTweaks$getLevel(stack, elementInfo.name);
                    highestElement = elementInfo;
                }
            }
        }
        return highestElement;
    }

    // Show for selected minion
    @Unique
    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getExperienceDisplay(ItemStack stack){
        NBTTagCompound nbt = this.getTagCompound(stack);
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(Minecraft.getMinecraft().player);
        int exp = 0;

        if(nbt != null && extendedPlayer != null){
            CreatureInfo creatureInfo = extendedPlayer.getSelectedSummonSet().getCreatureInfo();
            if (creatureInfo != null && nbt.hasKey("ElementsLevel")) {
                List<ElementInfo> elementInfos = (extendedPlayer.getSelectedSummonSet().getSubspecies() == 0)
                        ? creatureInfo.elements
                        : creatureInfo.getSubspecies(extendedPlayer.getSelectedSummonSet().getSubspecies()).elements;
                ElementInfo element = this.lycanitesTweaks$getHighestLevelElement(stack, elementInfos);
                if(element != null) exp = this.lycanitesTweaks$getExperience(stack, element.name);
            }
        }

        return exp;
    }

    // Show for selected minion
    @Unique
    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getNextLevelDisplay(ItemStack stack){
        NBTTagCompound nbt = this.getTagCompound(stack);
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(Minecraft.getMinecraft().player);
        int exp = 0;

        if(nbt != null && extendedPlayer != null){
            CreatureInfo creatureInfo = extendedPlayer.getSelectedSummonSet().getCreatureInfo();
            if (creatureInfo != null && nbt.hasKey("ElementsLevel")) {
                List<ElementInfo> elementInfos = (extendedPlayer.getSelectedSummonSet().getSubspecies() == 0)
                        ? creatureInfo.elements
                        : creatureInfo.getSubspecies(extendedPlayer.getSelectedSummonSet().getSubspecies()).elements;
                this.lycanitesTweaks$setItemStack(stack);
                ElementInfo element = this.lycanitesTweaks$getHighestLevelElement(stack, elementInfos);
                if(element != null) exp = this.lycanitesTweaks$getExperienceForNextLevel(stack, element.name);
            }
        }

        return exp;
    }

    @Unique
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        StringBuilder rawStrings = new StringBuilder();
        NBTTagCompound nbt = this.getTagCompound(stack);

        rawStrings.append(I18n.format("item.summoningstaff.description.mixin"));

        // Full XP
        if(GuiScreen.isShiftKeyDown()) {
            if(nbt.hasKey("ElementsLevel")) {
                rawStrings.append("\n").append("-------------------");
                for (String element : nbt.getCompoundTag("ElementsLevel").getKeySet()) {
                    ElementInfo elementInfo = ElementManager.getInstance().getElement(element);
                    if (elementInfo != null) {
                        rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.element",
                                elementInfo.getTitle())
                        );
                    }
                    rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.level",
                            this.lycanitesTweaks$getLevel(stack, element),
                            this.lycanitesTweaks$getExperience(stack, element),
                            this.lycanitesTweaks$getExperienceForNextLevel(stack, element))
                    );
                }
            }
        }
        // Selected Minion XP
        else {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(Minecraft.getMinecraft().player);
            if(extendedPlayer != null){
                rawStrings.append("\n").append("-------------------");
                if(nbt.hasKey("ElementsLevel")){
                    rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
                }
                CreatureInfo creatureInfo = extendedPlayer.getSelectedSummonSet().getCreatureInfo();
                if (creatureInfo != null) {
                    rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.creature", creatureInfo.getTitle()));
                    List<ElementInfo> elementInfos = (extendedPlayer.getSelectedSummonSet().getSubspecies() == 0)
                            ? creatureInfo.elements
                            : creatureInfo.getSubspecies(extendedPlayer.getSelectedSummonSet().getSubspecies()).elements;
                    for (ElementInfo elementInfo : elementInfos) {
                        rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.element",
                                elementInfo.getTitle())
                        );
                        rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.level",
                                this.lycanitesTweaks$getLevel(stack, elementInfo.name),
                                this.lycanitesTweaks$getExperience(stack, elementInfo.name),
                                this.lycanitesTweaks$getExperienceForNextLevel(stack, elementInfo.name))
                        );
                    }
                }
            }
        }

        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    @Unique
    public void lycanitesTweaks$setItemStack(ItemStack itemStack){
        this.lycanitesTweaks$stack = itemStack;
    }

    @Unique
    public int lycanitesTweaks$getHighestLevel(List<ElementInfo> elements){
        if(this.lycanitesTweaks$stack == null) return 1;

        int level = 1;
        for(ElementInfo elementInfo : elements){
            if(lycanitesTweaks$hasElement(this.lycanitesTweaks$stack, elementInfo)) level = Math.max(level, this.lycanitesTweaks$getLevel(this.lycanitesTweaks$stack, elementInfo.name));
        }
        return level;
    }

    @Unique
    public void lycanitesTweaks$addElements(ItemStack itemStack, ChargeItem chargeItem){
        NBTTagCompound nbt = this.getTagCompound(itemStack);

        NBTTagCompound experienceNBT = nbt.hasKey("ElementsExperience") ? nbt.getCompoundTag("ElementsExperience") : new NBTTagCompound();
        NBTTagCompound levelNBT = nbt.hasKey("ElementsLevel") ? nbt.getCompoundTag("ElementsLevel") : new NBTTagCompound();

        for(ElementInfo elementInfo : chargeItem.getElements()){
            if(!experienceNBT.hasKey(elementInfo.name)) {
                experienceNBT.setInteger(elementInfo.name, 0);
            }
            if(!levelNBT.hasKey(elementInfo.name)) {
                levelNBT.setInteger(elementInfo.name, 1);
            }
            nbt.setTag("ElementsExperience", experienceNBT);
            nbt.setTag("ElementsLevel", levelNBT);
        }

        itemStack.setTagCompound(nbt);
    }

    @Unique
    public int lycanitesTweaks$getLevel(ItemStack itemStack, String elementName) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int level = 1;

        if(nbt.hasKey("ElementsLevel")){
            NBTTagCompound levelNBT = nbt.getCompoundTag("ElementsLevel");
            if(levelNBT.hasKey(elementName)) level = levelNBT.getInteger(elementName);
        }

        return level;
    }

    @Unique
    public void lycanitesTweaks$setLevel(ItemStack itemStack, String elementName, int level) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);

        if(nbt.hasKey("ElementsLevel")){
            NBTTagCompound levelNBT = nbt.getCompoundTag("ElementsLevel");
            if(levelNBT.hasKey(elementName)) levelNBT.setInteger(elementName, level);
        }
        itemStack.setTagCompound(nbt);
    }

    @Unique
    public void lycanitesTweaks$setExperience(ItemStack itemStack, String elementName, int experience) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("ElementsExperience")){
            NBTTagCompound experienceNBT = nbt.getCompoundTag("ElementsExperience");
            if(experienceNBT.hasKey(elementName)) experienceNBT.setInteger(elementName, experience);
        }
        itemStack.setTagCompound(nbt);
    }

    @Unique
    public void lycanitesTweaks$addExperience(ItemStack itemStack, String elementName, int experience) {
        int currentLevel = this.lycanitesTweaks$getLevel(itemStack, elementName);

        int increasedExperience = this.lycanitesTweaks$getExperience(itemStack, elementName) + experience;
        int nextLevelExperience = this.lycanitesTweaks$getExperienceForNextLevel(itemStack, elementName);
        if(increasedExperience >= nextLevelExperience) {
            increasedExperience = increasedExperience - nextLevelExperience;
            this.lycanitesTweaks$setLevel(itemStack, elementName, currentLevel + 1);
        }
        this.lycanitesTweaks$setExperience(itemStack, elementName, increasedExperience);
    }

    @Unique
    public int lycanitesTweaks$getExperience(ItemStack itemStack, String elementName) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int experience = 0;
        if(nbt.hasKey("ElementsExperience")){
            NBTTagCompound experienceNBT = nbt.getCompoundTag("ElementsExperience");
            if(experienceNBT.hasKey(elementName)) experience = experienceNBT.getInteger(elementName);
        }
        return experience;
    }

    @Unique
    public int lycanitesTweaks$getExperienceForNextLevel(ItemStack itemStack, String elementName) {
        return LycanitesEntityUtil.calculateExperienceForNextLevel(
                ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffBaseLevelupExperience,
                this.lycanitesTweaks$getLevel(itemStack, elementName)
        );
    }

    @Unique
    public boolean lycanitesTweaks$isLevelingChargeItem(ItemStack itemStack){
        return itemStack.getItem() instanceof ChargeItem;
    }

    @Unique
    public int lycanitesTweaks$getExperienceFromChargeItem(ItemStack itemStack){
        return (this.lycanitesTweaks$isLevelingChargeItem(itemStack)) ? ChargeItem.CHARGE_EXPERIENCE : 0;
    }

    @Unique
    public boolean lycanitesTweaks$hasElement(ItemStack itemStack, ElementInfo element){
        NBTTagCompound nbt = this.getTagCompound(itemStack);

        if(!nbt.hasKey("ElementsLevel")) return false;
        return nbt.getCompoundTag("ElementsLevel").getKeySet().contains(element.name);
    }
}
