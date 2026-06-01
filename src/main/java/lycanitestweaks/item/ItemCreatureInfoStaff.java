package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.PortalEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.item.interfaces.IItemWithCreatureInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCreatureInfoStaff extends ItemStaffSummoning implements IItemWithCreatureInfo {

    protected ItemStack itemStack = null;

    public ItemCreatureInfoStaff(String itemName, String textureName) {
        super(itemName, textureName);

        this.setRegistryName(LycanitesTweaks.MODID, this.itemName);
        this.setTranslationKey(LycanitesTweaks.MODID + "." + this.itemName);
    }

    @Override
    public void setup() {}

    @SideOnly(Side.CLIENT)
    @Override
    public String getDescription(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(this.getCreatureTypeName(stack).isEmpty()) return I18n.format("item.lycanitestweaks.creatureinfostaff.null");
        return I18n.format(this.getTranslationKey() + ".description", this.getLevel(stack), this.getCreatureTypeName(stack));
    }

    @Override
    public String getCreatureTypeName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CREATURE_TYPE_NBT)) return nbt.getString(CREATURE_TYPE_NBT);
        return "";
    }

    @Override
    public String getCustomName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CUSTOM_NAME_NBT)) return nbt.getString(CUSTOM_NAME_NBT);
        return "";
    }

    @Override
    public int getLevel(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(MOB_LEVEL_NBT)) return nbt.getInteger(MOB_LEVEL_NBT);
        return -1;
    }

    @Override
    public int getEntitySubspecies(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(SUBSPECIES_NBT)) return nbt.getInteger(SUBSPECIES_NBT);
        return -1;
    }

    @Override
    public int getEntityVariant(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(VARIANT_NBT)) return nbt.getInteger(VARIANT_NBT);
        return -1;
    }

    @Override
    public void setCreatureTypeName(ItemStack itemStack, String type){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CREATURE_TYPE_NBT, type);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setCustomName(ItemStack itemStack, String name){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CUSTOM_NAME_NBT, name);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setLevel(ItemStack itemStack, int level){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(MOB_LEVEL_NBT, level);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setEntitySubspecies(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(SUBSPECIES_NBT, index);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setEntityVariant(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(VARIANT_NBT, index);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        // For creative use and NBT reference
        this.itemStack = playerIn.getHeldItem(hand);
        if(playerIn.capabilities.isCreativeMode && playerIn.isSneaking() && this.getCreatureTypeName(itemStack).isEmpty()) {
            this.setCreatureTypeName(itemStack, "krake");
            this.setCustomName(itemStack, "Krill Issue");
            this.setLevel(itemStack, 0);
            this.setEntitySubspecies(itemStack, 1);
            this.setEntityVariant(itemStack, 2);
        }
        return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    // ========== Minion Behaviour ==========
    @Override
    public void applyMinionBehaviour(TameableCreatureEntity minion, EntityPlayer player) {
        super.applyMinionBehaviour(minion, player);
        if(this.itemStack != null) {
            if (!this.getCustomName(this.itemStack).isEmpty()) minion.setCustomNameTag(this.getCustomName(this.itemStack));
            if(this.getEntitySubspecies(this.itemStack) != -1) minion.setSubspecies(this.getEntitySubspecies(this.itemStack));
            if(this.getEntityVariant(this.itemStack) != -1) minion.setVariant(this.getEntityVariant(this.itemStack));
            if(minion.isRareVariant()) minion.setVariant(0); // Only Challenge Soul should summon
            if(this.getLevel(this.itemStack) != -1) minion.addLevel(this.getLevel(this.itemStack) - 1);
        }
        minion.getRandomSize();
    }


    // ==================================================
    //                      Attack
    // ==================================================
    // ========== Start ==========
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!CreatureManager.getInstance().config.isSummoningAllowed(world)) {
            return new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
        }
        ItemStack itemStack = player.getHeldItem(hand);
        if(itemStack.getItem() instanceof ItemCreatureInfoStaff) {
            ExtendedPlayer playerExt = ExtendedPlayer.getForPlayer(player);
            if (playerExt != null) {
                // Summon Selected Mob:
                CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(this.getCreatureTypeName(itemStack));
                if (creatureInfo != null) {
                    if (!player.getEntityWorld().isRemote) {
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
        // Hack to sync creature properties before spawning
        this.itemStack = itemStack;
        boolean success = super.chargedAttack(itemStack, world, entity, power);
        if(success && entity instanceof EntityPlayer){
            // Consume Soulstone:
            if (!((EntityPlayer) entity).capabilities.isCreativeMode && this.getMaxDamage(itemStack) == 1)
                itemStack.setCount(Math.max(0, itemStack.getCount() - 1));
        }
        return success;
    }
}
