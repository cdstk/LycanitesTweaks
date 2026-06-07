package lycanitestweaks.item;

import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.Variant;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.ItemBase;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.features.item.ConfigurableItemHandler;
import lycanitestweaks.item.base.ItemBossRangedWeapon;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ItemDevilGatlingGun extends ItemBossRangedWeapon {

    public ItemDevilGatlingGun(String itemName) {
        super(itemName);
        this.infinityProjectileName = "devilgatling";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack itemStack, World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag);

        StringBuilder rawStrings = new StringBuilder();
        rawStrings.append(I18n.format("item.lycanitestweaks.devilgatlinggun.description"));

        CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature("asmodeus");
        if(creatureInfo != null && this.getLevel(itemStack) >= 0) {
            rawStrings.append("\n");
            Variant variant = creatureInfo.getSubspecies(0).getVariant(this.getEntityVariant(itemStack));
            if (variant != null) rawStrings.append(variant.getTitle()).append(" ");

            rawStrings.append(creatureInfo.getTitle()).append(" ");
            rawStrings.append(I18n.format("entity.level")).append(" ").append(this.getLevel(itemStack));
        }

        rawStrings.append("\n");
        if(GuiScreen.isShiftKeyDown()) {
            if(ModLoadedUtil.baubles.isLoaded()) {
                ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemStack);
                if(slots != null && slots.baubleAttributes) {
                    rawStrings.append(I18n.format("lycanitestweaks.ability.attributebauble.tooltip0")).append("\n");
                }
            }
            rawStrings.append(I18n.format("lycanitestweaks.ability.devilgatlinggun.tooltip0")).append("\n");
            rawStrings.append(I18n.format("lycanitestweaks.ability.devilgatlinggun.tooltip1")).append("\n");
        }
        else {
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
        }

        ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(itemStack);
        if(stats != null) {
            rawStrings.append("\n");
            rawStrings.append(I18n.format("item.stats.ticksperuse.weapon.tooltip", stats.ticksPerUse / 20F));
        }

        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    @Override
    protected boolean isArrow(ItemStack stack) {
        if(stack.getItem() instanceof ChargeItem) {
            ChargeItem item = (ChargeItem) stack.getItem();
            for(String chargeName : ForgeConfigHandler.server.customStaffConfig.gatlingAmmoBlacklist) {
                if(item.projectileInfo.getName().equals(chargeName))
                    return false;
            }
            return true;
        }
        return false;
    }

    // SME strafe balance
//    @Override
//    public int getMaxItemUseDuration(ItemStack stack) {
//        return 1200; // One minute
//    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            player.getCooldownTracker().setCooldown(this, 20 + (this.getMaxItemUseDuration(stack) - timeLeft) / 4);
        }
    }

    // SME Strafe breaks handling like these
    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count){
        int useTime = this.getMaxItemUseDuration(stack) - count;
        if(useTime >= 1200) {
            player.stopActiveHand();
        }
        if(useTime < 20) return;
        int useRate = 10;
        ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(stack);
        if(stats != null) {
            useRate = stats.baseTicksPerUse;
            if(stats.ticksPerUse != useRate && count % stats.ticksPerUse == 0) this.fireChargeProjectile(stack, player.getEntityWorld(), player, count, 0, 0, 8F, 5, false);
        }
        if(count % useRate == 0) this.fireChargeProjectile(stack, player.getEntityWorld(), player, count, 0, 0, 8F, 5, true);
    }

    @Override
    public SoundEvent getFireSound(){
        return AssetManager.getSound( "asmodeus_attack");
    }

    public float getFireVelocityModifier(){
        return 4F;
    }

    @Override
    protected void setInfinityProjectileName(String projectileName) {}

    @Override
    protected List<BaseProjectileEntity> createChargeProjectileList(ItemStack itemStack, World world, EntityPlayer player, ChargeItem chargeItem) {
        return Collections.singletonList(LycanitesEntityUtil.createProjectileFromItem(itemStack, world, player, chargeItem));
    }

    @Override
    protected List<BaseProjectileEntity> createInfinityProjectileList(World world, EntityPlayer player) {
        BaseProjectileEntity baseProjectileEntity = createInfinityProjectile(world, player);
        return Collections.singletonList(baseProjectileEntity);
    }

    @Override
    protected void customizeProjectile(BaseProjectileEntity projectile, EntityLivingBase entityLivingBase) {
        super.customizeProjectile(projectile, entityLivingBase);
        if(projectile == null) return;
        // Less screen flashing
        projectile.setPosition(
                projectile.posX + projectile.motionX / this.getFireVelocityModifier(),
                projectile.posY + projectile.motionY / this.getFireVelocityModifier(),
                projectile.posZ + projectile.motionZ / this.getFireVelocityModifier()
        );
    }
}
