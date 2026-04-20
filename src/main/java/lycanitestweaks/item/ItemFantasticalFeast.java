package lycanitestweaks.item;

import com.lycanitesmobs.core.item.ItemBase;
import lycanitestweaks.compat.BaublesHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.item.base.ItemPassive;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemFantasticalFeast extends ItemPassive {

    public ItemFantasticalFeast(String name) {
        super(name);
    }

    @Override
    public boolean isEnabled() {
        return ForgeConfigHandler.server.customStaffConfig.registerFantasticalFeast;
    }

    @Override
    public boolean hasSubscriber() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack,  World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag);

        StringBuilder rawStrings = new StringBuilder();
        rawStrings.append(I18n.format("item.lycanitestweaks.fantasticalfeast.description"));
        rawStrings.append("\n").append(I18n.format("lycanitestweaks.ability.feedingfrenzy.tooltip0"));
        if(GuiScreen.isShiftKeyDown()) {
            rawStrings.append("\n").append(I18n.format("lycanitestweaks.ability.feedingfrenzy.tooltip1", String.format("%.0f%%", 100 * ForgeConfigHandler.server.customStaffConfig.feedingFrenzyBoost)));
        }
        else {
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
        }
        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        if(ForgeConfigHandler.server.customStaffConfig.fantasticalFeastRepairables) {
            if (repairStack.getItem() instanceof ItemAppleGold || repairStack.getItem() instanceof ItemChorusFruit)
                return true;
        }
        return super.getIsRepairable(itemStack, repairStack);
    }

    public static boolean isEquipped(EntityLivingBase entity){
        if(entity.getHeldItemMainhand().getItem() == LycanitesTweaksRegistry.fantasticalFeast) return true;
        if(entity.getHeldItemOffhand().getItem() == LycanitesTweaksRegistry.fantasticalFeast) return true;

        if(ModLoadedUtil.baubles.isLoaded()) {
            if (entity instanceof EntityPlayer) return BaublesHandler.hasItemAsBauble((EntityPlayer) entity, LycanitesTweaksRegistry.fantasticalFeast);
        }
        return false;
    }

    @SubscribeEvent
    public void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        ItemStack item = event.getItem();
        if(!item.isStackable() || item == ItemStack.EMPTY) return;
        if(!isEquipped(event.getEntityLiving())) return;

        EnumAction action = item.getItemUseAction();
        if(action != EnumAction.DRINK && action != EnumAction.EAT) return;

        float consumeBonus = Math.max(1F, ForgeConfigHandler.server.customStaffConfig.feedingFrenzyBoost * item.getCount() / item.getMaxStackSize());
        event.setDuration(Math.round(event.getDuration() / consumeBonus));
    }
}
