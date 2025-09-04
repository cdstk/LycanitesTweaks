package lycanitestweaks.item;

import com.lycanitesmobs.core.info.AltarInfo;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.ItemBase;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.altar.IAltarNoBoost;
import lycanitestweaks.util.Helpers;
import lycanitestweaks.util.IItemInfuserDisplay_Mixin;
import lycanitestweaks.util.IItemStationDisplay_Mixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemEnchantedSoulkey extends Item implements IItemInfuserDisplay_Mixin, IItemStationDisplay_Mixin {

    // Hybrid of EquipmentPart and Soulkey
    // Don't even need to extend Soulkey or ItemBase

    public String itemName;
    public int variant;

    public ItemEnchantedSoulkey(String itemName, int variant) {
        super();
        this.itemName = itemName;
        this.variant = variant;
        this.setMaxStackSize(1);

        this.setRegistryName(LycanitesTweaks.MODID, this.itemName);
        this.setTranslationKey(LycanitesTweaks.MODID + "." + this.itemName);
    }

    // Incase extending Lycanites ItemBase
    //    @Override
    //    public void setup() {}

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getExperienceDisplay(ItemStack stack) {
        return this.getExperience(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getNextLevelDisplay(ItemStack stack) {
        return this.getExperienceForNextLevel(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getTopDisplay(ItemStack stack) {
        return this.getGemPower(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getTopMaxDisplay(ItemStack stack) {
        return ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getBottomDisplay(ItemStack stack) {
        return this.getStarPower(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int lycanitesTweaks$getBottomMaxDisplay(ItemStack stack) {
        return ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages;
    }

    /**
     * Determines the Gem Power repair amount of the provided itemstack. Stack size is not taken into account.
     * @param itemStack The itemstack to check the item and nbt data of.
     * @return The amount of Gem Power the provided itemstack restores.
     */
    public static int getSoulkeyGemPowerRepair(ItemStack itemStack) {
        if(itemStack.isEmpty()) return 0;
        Item item = itemStack.getItem();
        ResourceLocation checkId = item.getRegistryName();
        if(checkId == null) return 0;
        String itemName = checkId.toString();
        if(itemName.equals("minecraft:diamond_block") || itemName.equals("minecraft:emerald_block"))
            return 1;
        return 0;
    }

    /**
     * Determines the Star Power repair amount of the provided itemstack. Stack size is not taken into account.
     * @param itemStack The itemstack to check the item and nbt data of.
     * @return The amount of Star Power the provided itemstack restores.
     */
    public static int getSoulkeyStarPowerRepair(ItemStack itemStack) {
        return (itemStack.getItem() == Items.NETHER_STAR) ? 1 : 0;
    }

    // Enchant Glint
    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(@Nonnull ItemStack stack){
        return true;
    }

    // For the Mex Level 1000 key
    @Override
    @Nonnull
    public IRarity getForgeRarity(@Nonnull ItemStack stack){
        return this.getMaxLevel(stack) == 1000 ? EnumRarity.EPIC : EnumRarity.RARE;
    }
    /** Gets or creates an NBT Compound for the provided itemstack. **/
    public NBTTagCompound getTagCompound(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return itemStack.getTagCompound();
        }
        return new NBTTagCompound();
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack){
        if(this.getMaxLevel(stack) == 1000)
            return I18n.format(this.getTranslationKey(stack) + ".thousand.name");
        return super.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack itemStack, World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag);

        StringBuilder rawStrings = new StringBuilder();
        rawStrings.append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description"));

        if(GuiScreen.isShiftKeyDown()) {
            if(ForgeConfigHandler.server.enchSoulkeyConfig.allowStationAndInfuser) {
                rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description.mixin"));
            }
            rawStrings.append("\n").append("-------------------");
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description.power",
                    this.getGemPower(itemStack),
                    ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages));
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description.mana",
                    this.getStarPower(itemStack),
                    ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages));
        }
        else {
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
        }

        rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description.level",
                this.getLevel(itemStack),
                this.getMaxLevel(itemStack),
                this.getExperience(itemStack),
                this.getExperienceForNextLevel(itemStack))
        );
        int usages = (this.variant == 0) ? Math.min(this.getGemPower(itemStack), this.getStarPower(itemStack)) : Math.min(this.getGemPower(itemStack) / 2, this.getStarPower(itemStack));
        rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.enchantedsoulkey.description.remaining", usages));

        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    /** Returns the level for the provided ItemStack. **/
    public int getMaxLevel(ItemStack itemStack) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int level = 100;
        if(nbt.hasKey("soulkeyMaxLevel")) {
            level = nbt.getInteger("soulkeyMaxLevel");
        }
        return level;
    }

    /** Sets the level of the provided Item Stack. **/
    public void setMaxLevel(ItemStack itemStack, int level) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("soulkeyMaxLevel", Math.max(Math.min(level, ForgeConfigHandler.server.enchSoulkeyConfig.defaultMaxLevel), 0));
        itemStack.setTagCompound(nbt);
    }

    /** Returns the level for the provided ItemStack. **/
    public int getLevel(ItemStack itemStack) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int level = 1;
        if(nbt.hasKey("soulkeyLevel")) {
            level = nbt.getInteger("soulkeyLevel");
        }
        return level;
    }

    /** Sets the level of the provided Item Stack. **/
    public void setLevel(ItemStack itemStack, int level) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("soulkeyLevel", Math.max(Math.min(level, this.getMaxLevel(itemStack)), 0));
        itemStack.setTagCompound(nbt);
    }

    /** Increases the level of the provided Item Stack. This will also level up if the level is enough. **/
    public boolean addLevel(ItemStack itemStack, int level) {
        int currentLevel = this.getLevel(itemStack);
        if(currentLevel >= this.getMaxLevel(itemStack)) {
            return false;
        }
        this.setLevel(itemStack, this.getLevel(itemStack) + level);
        return true;
    }

    /** Decreases the level of the provided Item Stack. This will also level up if the level is enough. **/
    public boolean removelevel(ItemStack itemStack, int level) {
        int currentLevel = this.getLevel(itemStack);
        if(currentLevel <= 0) {
            return false;
        }
        this.setLevel(itemStack, this.getLevel(itemStack) - level);
        return true;
    }

    /** Sets the experience of the provided Item Stack. **/
    public void setExperience(ItemStack itemStack, int experience) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("soulkeyExperience", experience);
        itemStack.setTagCompound(nbt);
    }

    /** Increases the experience of the provided Item Stack. This will also level up if the experience is enough. **/
    public void addExperience(ItemStack itemStack, int experience) {
        int currentLevel = this.getLevel(itemStack);
        if(currentLevel >= this.getMaxLevel(itemStack)) {
            this.setExperience(itemStack, 0);
        }
        int increasedExperience = this.getExperience(itemStack) + experience;
        int nextLevelExperience = this.getExperienceForNextLevel(itemStack);
        if(increasedExperience >= nextLevelExperience) {
            increasedExperience = increasedExperience - nextLevelExperience;
            this.setLevel(itemStack, currentLevel + 1);
        }
        this.setExperience(itemStack, increasedExperience);
    }

    /** Returns the Experience for the provided ItemStack. **/
    public int getExperience(ItemStack itemStack) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int experience = 0;
        if(nbt.hasKey("soulkeyExperience")) {
            experience = nbt.getInteger("soulkeyExperience");
        }
        return experience;
    }

    /**
     * Determines how much experience needed in order to level up.
     * @return Experience required for a level up.
     */
    public int getExperienceForNextLevel(ItemStack itemStack) {
        return Helpers.calculateExperienceForNextLevel(
                ForgeConfigHandler.server.enchSoulkeyConfig.baseLevelupExperience,
                this.getLevel(itemStack)
        );
    }

    /**
     * Determines if the provided itemstack can be consumed to add experience.
     * @param itemStack The possible leveling itemstack.
     * @return True if should consume the itemstack and gain experience.
     */
    public boolean isValidLevelingItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ChargeItem;
    }

    /**
     * Determines how much experience the provided charge itemstack can grant.
     * @param itemStack The possible leveling itemstack.
     * @return The amount of experience to gain.
     */
    public int getExperienceFromChargeItem(ItemStack itemStack) {
        return (this.isValidLevelingItem(itemStack)) ? ChargeItem.CHARGE_EXPERIENCE : 0;
    }

    /** Returns the Gem Power for the provided ItemStack. **/
    public int getGemPower(ItemStack itemStack) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);

        int charges = (this.variant == 0) ? 1 : 2;
        int sharpness = ForgeConfigHandler.server.enchSoulkeyConfig.usagesOnCraft * charges;
        if(nbt.hasKey("soulkeySharpness")) {
            sharpness = nbt.getInteger("soulkeySharpness");
        }
        return sharpness;
    }

    /** Sets the Gem Power of the provided Item Stack. **/
    public void setGemPower(ItemStack itemStack, int sharpness) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("soulkeySharpness", Math.max(Math.min(sharpness, ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages), 0));
        itemStack.setTagCompound(nbt);
    }

    /** Increases the Gem Power of the provided Item Stack. **/
    public boolean addGemPower(ItemStack itemStack, int sharpness) {
        int currentSharpness = this.getGemPower(itemStack);
        if(currentSharpness >= ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages) {
            return false;
        }
        this.setGemPower(itemStack, this.getGemPower(itemStack) + sharpness);
        return true;
    }

    /** Decreases the GemPower of the provided Equipment Item Stack. **/
    public boolean removeGemPower(ItemStack itemStack, int sharpness) {
        int currentSharpness = this.getGemPower(itemStack);
        if(currentSharpness <= 0) {
            return false;
        }
        this.setGemPower(itemStack, this.getGemPower(itemStack) - sharpness);
        return true;
    }

    /** Returns the Nether Star Power for the provided ItemStack. **/
    public int getStarPower(ItemStack itemStack) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        int mana = ForgeConfigHandler.server.enchSoulkeyConfig.usagesOnCraft;
        if(nbt.hasKey("soulkeyMana")) {
            mana = nbt.getInteger("soulkeyMana");
        }
        return mana;
    }

    /** Sets the Nether Star Power of the provided Item Stack. **/
    public void setStarPower(ItemStack itemStack, int mana) {
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("soulkeyMana", Math.max(Math.min(mana, ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages), 0));
        itemStack.setTagCompound(nbt);
    }

    /** Increases the Nether Star Power of the provided Equipment Item Stack. **/
    public boolean addStarPower(ItemStack itemStack, int mana) {
        int currentMana = this.getStarPower(itemStack);
        if(currentMana >= ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages) {
            return false;
        }
        this.setStarPower(itemStack, this.getStarPower(itemStack) + mana);
        return true;
    }

    /** Decreases the Nether Star Power of the provided Equipment Item Stack. **/
    public boolean removeStarPower(ItemStack itemStack, int mana) {
        int currentMana = this.getStarPower(itemStack);
        if(currentMana <= 0) {
            return false;
        }
        this.setStarPower(itemStack, this.getStarPower(itemStack) - mana);
        return true;
    }

    public boolean isCharged(ItemStack itemStack){
        int charges = (this.variant == 0) ? 0 : 1;
        return this.getStarPower(itemStack) > 0 && this.getGemPower(itemStack) > charges;
    }

    public void reduceCharge(ItemStack itemStack){
        int charges = (this.variant == 0) ? 1 : 2;
        this.removeStarPower(itemStack, 1);
        this.removeGemPower(itemStack, charges);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);

        // For creative use and NBT reference
        if(player.capabilities.isCreativeMode && player.isSneaking()) {
            this.setLevel(itemStack, this.getMaxLevel(itemStack));
            this.setMaxLevel(itemStack, ForgeConfigHandler.server.enchSoulkeyConfig.defaultMaxLevel);
            this.setStarPower(itemStack, ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages);
            this.setGemPower(itemStack, ForgeConfigHandler.server.enchSoulkeyConfig.maxUsages);
        }

        if(!player.getEntityWorld().isRemote) {
            if (!this.isCharged(itemStack)) {
                player.sendMessage(new TextComponentTranslation("message.enchantedkey.fail.nocharge"));
                return EnumActionResult.FAIL;
            }
            if (!AltarInfo.checkAltarsEnabled()) {
                player.sendMessage(new TextComponentTranslation("message.soulkey.disabled"));
                return EnumActionResult.FAIL;
            }


            // Get Possible Altars:
            List<AltarInfo> possibleAltars = new ArrayList<>();
            if (AltarInfo.altars.isEmpty())
                LycanitesTweaks.LOGGER.log(Level.WARN, "No altars have been registered, Soulkeys will not work at all.");

            for (AltarInfo altarInfo : AltarInfo.altars.values()) {
                if (altarInfo.checkBlockEvent(player, world, pos) && altarInfo.quickCheck(player, world, pos)) {
                    possibleAltars.add(altarInfo);
                }
            }
            if (possibleAltars.isEmpty()) {
                player.sendMessage(new TextComponentTranslation("message.soulkey.none"));
                return EnumActionResult.FAIL;
            }

            // Activate First Valid Altar:
            for (AltarInfo altarInfo : possibleAltars) {
                if (altarInfo.fullCheck(player, world, pos)) {

                    // Valid Altar:
                    if (!player.getEntityWorld().isRemote) {
                        if (!altarInfo.activate(player, world, pos, this.variant)) {
                            player.sendMessage(new TextComponentTranslation("message.soulkey.badlocation"));
                            return EnumActionResult.FAIL;
                        }
                        if (!player.capabilities.isCreativeMode) {
                            this.reduceCharge(itemStack);
                        }
                        if (hand == EnumHand.MAIN_HAND && !(altarInfo instanceof IAltarNoBoost)) {
                            player.sendMessage(new TextComponentTranslation("message.enchantedkey.active", this.getLevel(itemStack)));
                        } else {
                            player.sendMessage(new TextComponentTranslation("message.soulkey.active"));
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }

            if (!player.capabilities.isCreativeMode) {
                player.sendMessage(new TextComponentTranslation("message.soulkey.invalid"));
            }
        }
        return EnumActionResult.FAIL;
    }
}
