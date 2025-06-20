package lycanitestweaks.client;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import com.lycanitesmobs.core.item.temp.ItemStaffSummoning;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.IPlayerMobLevelCapability;
import lycanitestweaks.capability.PlayerMobLevelCapability;
import lycanitestweaks.handlers.ClientModRegistry;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventListener {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(event.getEntityPlayer());
        if(pml != null) {
            if (event.getItemStack().getItem() instanceof ItemSoulgazer) {
                if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().isEmpty()) {
                    event.getToolTip().addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(
                            I18n.format("item.soulgazer.description.pmlsoulgazer"), ItemBase.DESCRIPTION_WIDTH));
                }
                if(event.getEntityPlayer().isCreative()){
                    event.getToolTip().add(I18n.format("item.soulgazer.description.pmlcreative",
                            pml.getTotalEnchantmentLevels(),
                            pml.getHighestLevelPetSoulbound()
                    ));
                }

            }
            else if (event.getItemStack().getItem() instanceof ItemStaffSummoning) {
                if(PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.SummonMinion)) {
                    event.getToolTip().addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(
                            I18n.format("item.summonstaff.description.pmlsummon"), ItemBase.DESCRIPTION_WIDTH));
                }
            }
        }
    }

    // ty Ice and Fire
    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(player.getRidingEntity() != null) {
            if(player.getRidingEntity() instanceof RideableCreatureEntity){
                int currentView = LycanitesTweaks.PROXY.getMount3rdPersonView();
                float scale = Math.max(1, player.getRidingEntity().width / 3F);
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
                    if (currentView == 1) GlStateManager.translate(scale * 0.5F, 0F, -scale * 3F);
                    else if (currentView == 2) GlStateManager.translate(0, 0F, -scale * 3F);
                    else if (currentView == 3) GlStateManager.translate(scale * 0.5F, 0F, -scale * 0.5F);
                }
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                    if (currentView == 1) GlStateManager.translate(-scale  * 1.2F, 0F, 5);
                    else if(currentView == 2) GlStateManager.translate(scale  * 1.2F, 0F, 5);
                    else if(currentView == 3) GlStateManager.translate(0, 0F, scale * 3F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMountView(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            if (player.getEntityWorld().isRemote && ClientModRegistry.mount_change_view.isPressed()) {
                int currentView = LycanitesTweaks.PROXY.getMount3rdPersonView();
                if(currentView + 1 > 3) currentView = 0;
                else currentView++;
                LycanitesTweaks.PROXY.setMount3rdPersonView(currentView);
            }
        }
    }
}
