package lycanitestweaks.util;

import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.item.ChargeItem;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IItemStaffSummoningElementLevelMapMixin {

    // Based on ItemEquipmentPart

    /** Set a reference to ItemStack for usage when no convenient itemStack reference exists. **/
    void lycanitesTweaks$setItemStack(ItemStack itemStack);

    /** Returns the highest level from given List of ElementInfo **/
    int lycanitesTweaks$getHighestLevel(List<ElementInfo> elements);

    /** Adds NBT for each element of a ChargeItem, config on set whether the first charge sets and limits elements **/
    void lycanitesTweaks$addElements(ItemStack itemStack, ChargeItem chargeItem);

    /** Returns Level for the provided ItemStack and ElementName. **/
    int lycanitesTweaks$getLevel(ItemStack itemStack, String elementName);
    /** Sets the level of the provided Item Stack and ElementName. **/
    void lycanitesTweaks$setLevel(ItemStack itemStack, String elementName, int level);

    /** Sets the experience of the provided Item Stack and ElementName. **/
    void lycanitesTweaks$setExperience(ItemStack itemStack, String elementName, int experience);
    /** Increases the experience of the provided Item Stack. This will also level up if the experience is enough. **/
    void lycanitesTweaks$addExperience(ItemStack itemStack, String elementName, int experience);
    /** Returns the Element Experience for the provided ItemStack. **/
    int lycanitesTweaks$getExperience(ItemStack itemStack, String elementName);

    /**
     * Determines how much experience the ItemStack needs in order to level up.
     * @return Experience required for a level up.
     */
    int lycanitesTweaks$getExperienceForNextLevel(ItemStack itemStack, String elementName);

    /**
     * Determines if the provided itemstack can be consumed to add experience this ItemStack.
     * @param itemStack The possible leveling itemstack.
     * @return True if this part should consume the itemstack and gain experience.
     */
    boolean lycanitesTweaks$isLevelingChargeItem(ItemStack itemStack);

    /**
     * Determines how much experience the provided charge itemstack can grant this ItemStack.
     * @param itemStack The possible leveling itemstack.
     * @return The amount of experience to gain.
     */
    int lycanitesTweaks$getExperienceFromChargeItem(ItemStack itemStack);

    /**
     * Returns if this ItemStack has the provided element.
     * @param element The element to check for.
     * @return True if this ItemStack has the element.
     */
    boolean lycanitesTweaks$hasElement(ItemStack itemStack, ElementInfo element);
}
