package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks.summonstafflevelmap;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.temp.ItemScepter;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.IItemStaffSummoningElementLevelMapMixin;
import net.minecraft.client.Minecraft;
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
import java.util.Locale;

@Mixin(ItemStaffSummoning.class)
public abstract class ItemStaffSummoningElementLevelMapMixin extends ItemScepter implements IItemStaffSummoningElementLevelMapMixin {

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
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        StringBuilder rawStrings = new StringBuilder();
        NBTTagCompound nbt = this.getTagCompound(stack);

        rawStrings.append(I18n.format("item.summoningstaff.description.mixin"));
        if(nbt.hasKey("ChargeItem"))
            if(ForgeConfigHandler.client.translateWhenPossible) {
                //TODO: nisch suggestion, rlmixins had a mixin specifically to get rid of all toLowerCase calls. this is especially egregious in a once-per-frame method like this one. are you sure this is needed?
                ChargeItem chargeItem = (ChargeItem)ObjectManager.getItem(nbt.getString("ChargeItem").toLowerCase(Locale.ROOT));
                if(chargeItem != null)
                    rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.chargeitem", chargeItem.getProjectileName()));
            }
            else{
                rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.chargeitem", nbt.getString("ChargeItem")));
            }
        if(nbt.hasKey("ElementsLevel"))
            for(String element : nbt.getCompoundTag("ElementsLevel").getKeySet()) {
                if(ForgeConfigHandler.client.translateWhenPossible) {
                    ElementInfo elementInfo = ElementManager.getInstance().getElement(element);
                    if(elementInfo != null)
                        rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.element",
                                elementInfo.getTitle())
                        );
                }
                else
                    rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.element",
                            element)
                    );
                rawStrings.append("\n").append(I18n.format("item.summoningstaff.description.mixin.level",
                        this.lycanitesTweaks$getLevel(stack, element),
                        this.lycanitesTweaks$getExperience(stack, element),
                        this.lycanitesTweaks$getExperienceForNextLevel(stack, element))
                );
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

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffElementsByCharge) {
            if (!nbt.hasKey("ChargeItem")) {
                nbt.setString("ChargeItem", chargeItem.itemName);
            } else
                return;
        }

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

    /** Do not use, no practical application atm, would be the opposite of addElements except erroneously having ChargeItem with clear blank level map **/
    @Unique
    public void lycanitesTweaks$removeElements(ItemStack itemStack, ChargeItem chargeItem){
//        NBTTagCompound nbt = this.getTagCompound(itemStack);
//        if(ForgeConfigHandler.server.itemConfig.summonStaffElementsByCharge){
//            if(nbt.hasKey("ChargeItem")) nbt.removeTag("ChargeItem");
//        }
//
//        if(nbt.hasKey("ElementsExperience")){
//            NBTTagCompound experienceNBT = nbt.getCompoundTag("ElementsExperience");
//            for(ElementInfo elementInfo : chargeItem.getElements())
//                if(experienceNBT.hasKey(elementInfo.name)) experienceNBT.removeTag(elementInfo.name);
//        }
//        if(nbt.hasKey("ElementsLevel")){
//            NBTTagCompound levelNBT = nbt.getCompoundTag("ElementsLevel");
//            for(ElementInfo elementInfo : chargeItem.getElements())
//                if(levelNBT.hasKey(elementInfo.name)) levelNBT.removeTag(elementInfo.name);
//        }
//
//        itemStack.setTagCompound(nbt);
    }

    /** Do not use, no practical application atm **/
    @Unique
    public void lycanitesTweaks$removeElement(ItemStack itemStack, ElementInfo elementInfo){

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
        return ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffBaseLevelupExperience
                + Math.round(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffBaseLevelupExperience
                    * (this.lycanitesTweaks$getLevel(itemStack, elementName) - 1) * 0.25F);
    }

    public boolean lycanitesTweaks$isLevelingChargeItem(ItemStack itemStack){
        return itemStack.getItem() instanceof ChargeItem;
    }

    public int lycanitesTweaks$getExperienceFromChargeItem(ItemStack itemStack){
        return (this.lycanitesTweaks$isLevelingChargeItem(itemStack)) ? ChargeItem.CHARGE_EXPERIENCE : 0;
    }

    @Unique
    public boolean lycanitesTweaks$hasElement(ItemStack itemStack, ElementInfo element){
        NBTTagCompound nbt = this.getTagCompound(itemStack);

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.summonStaffElementsByCharge){
            if(!nbt.hasKey("ChargeItem")) return false;
            //TODO: same here
            ChargeItem chargeItem = (ChargeItem)ObjectManager.getItem(nbt.getString("ChargeItem").toLowerCase(Locale.ROOT));
            if(chargeItem == null) return false;
            return chargeItem.getElements().contains(element);
        }
        else {
            if(!nbt.hasKey("ElementsLevel")) return false;
            return nbt.getCompoundTag("ElementsLevel").getKeySet().contains(element.name);
        }
    }
}
