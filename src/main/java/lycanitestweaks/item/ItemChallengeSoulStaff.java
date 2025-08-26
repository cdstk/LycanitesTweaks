package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.PortalEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.LycanitesTweaks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemChallengeSoulStaff extends ItemStaffSummoning {

    // Either BaseCreatureEntity typename or ResourceLocation resourceName
    // Can be null but should be set at some point
    public String creatureTypeName;

    /** Entity Level **/
    public int entityLevel = 1;

    /** The name to use for the entity. Leave empty/null "" for no name. **/
    public String entityName = "";
    /** The Subspecies to use for the entity. **/
    public int subspeciesIndex = 0;
    /** The Variant to use for the entity. **/
    public int variantIndex = 0;

    public ItemChallengeSoulStaff(String itemName, String textureName) {
        super(itemName, textureName);

        this.setRegistryName(LycanitesTweaks.MODID, this.itemName);
        this.setTranslationKey(LycanitesTweaks.MODID + "." + this.itemName);
    }

    @Override
    public void setup() {}

    @SideOnly(Side.CLIENT)
    @Override
    public String getDescription(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        return I18n.format(this.getTranslationKey() + ".description");
    }

    public String getCreatureTypeName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("creatureTypeName")) this.creatureTypeName = nbt.getString("creatureTypeName");
        return this.creatureTypeName;
    }

    public String getEntityName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("EntityName")) this.entityName = nbt.getString("EntityName");
        return this.entityName;
    }

    public int getLevel(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("MobLevel")) this.entityLevel = nbt.getInteger("MobLevel");
        return this.entityLevel;
    }

    public int getEntitySubspecies(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("Subspecies")) this.subspeciesIndex = nbt.getInteger("Subspecies");
        return this.subspeciesIndex;
    }

    public int getEntityVariant(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey("Variant")) this.variantIndex = nbt.getInteger("Variant");
        return this.variantIndex;
    }

    public void setCreatureTypeName(ItemStack itemStack, String type){
        this.creatureTypeName = type;
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString("creatureTypeName", this.creatureTypeName);
        itemStack.setTagCompound(nbt);
    }

    public void setEntityName(ItemStack itemStack, String name){
        this.entityName = name;
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString("EntityName", this.entityName);
        itemStack.setTagCompound(nbt);
    }

    public void setLevel(ItemStack itemStack, int level){
        this.entityLevel = level;
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("MobLevel", this.entityLevel);
        itemStack.setTagCompound(nbt);
    }

    public void setEntitySubspecies(ItemStack itemStack, int index){
        this.subspeciesIndex = index;
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("Subspecies", this.subspeciesIndex);
        itemStack.setTagCompound(nbt);
    }

    public void setEntityVariant(ItemStack itemStack, int index){
        this.variantIndex = index;
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger("Variant", this.variantIndex);
        itemStack.setTagCompound(nbt);
    }

    // ==================================================
    //                       Use
    // ==================================================
    // ========== Durability ==========
//    @Override
//    public int getDurability() {
//        return 69420;
//    }

    // ========== Summon Cost ==========
    @Override
    public float getSummonCostMod() {
        return 0F;
    }

    // ========== Summon Duration ==========
    @Override
    public int getSummonDuration() {
        return 20 * 60 * 20;
    }


    // ========== Minion Behaviour ==========
    @Override
    public void applyMinionBehaviour(TameableCreatureEntity minion, EntityPlayer player) {
        if(!this.entityName.isEmpty()) minion.setCustomNameTag(this.entityName);
        minion.setFollowing(true);
        minion.setAssist(true);
        minion.setLevel(this.entityLevel);
        minion.setSubspecies(this.subspeciesIndex);
        minion.applyVariant(this.variantIndex);
        minion.getRandomSize();
    }


    // ==================================================
    //                      Attack
    // ==================================================
    // ========== Start ==========
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!CreatureManager.getInstance().config.isSummoningAllowed(world) || !true) {
            return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        ItemStack itemStack = player.getHeldItem(hand);
        if(itemStack.getItem() instanceof ItemChallengeSoulStaff) {
            ExtendedPlayer playerExt = ExtendedPlayer.getForPlayer(player);
            if (playerExt != null) {
                // Summon Selected Mob:
                CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(this.getCreatureTypeName(itemStack));
                if (creatureInfo != null) {
                    if (!player.getEntityWorld().isRemote) {
                        this.getLevel(itemStack);
                        this.getEntityName(itemStack);
                        this.getEntitySubspecies(itemStack);
                        this.getEntityVariant(itemStack);
                        playerExt.staffPortal = new PortalEntity(world, player, creatureInfo.getEntityClass(), this);
                        playerExt.staffPortal.setLocationAndAngles(player.posX, player.posY, player.posZ, world.rand.nextFloat() * 360.0F, 0.0F);
                        world.spawnEntity(playerExt.staffPortal);
                    }
                }
                else {
                    return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
                }
            }
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        return super.onItemRightClick(world, player, hand);
    }

    // ========== Charged ==========
    @Override
    public boolean chargedAttack(ItemStack itemStack, World world, EntityLivingBase entity, float power) {
        boolean success = super.chargedAttack(itemStack, world, entity, power);
        if(success && entity instanceof EntityPlayer){
            // Consume Soulstone:
            if (!((EntityPlayer) entity).capabilities.isCreativeMode)
                itemStack.setCount(Math.max(0, itemStack.getCount() - 1));
        }
        return success;
    }


    // ==================================================
    //                     Repairs
    // ==================================================
    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        return false;
    }
}
