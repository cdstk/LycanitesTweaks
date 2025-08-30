package lycanitestweaks.client;

import com.lycanitesmobs.core.item.GenericFoodItem;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import com.lycanitesmobs.core.item.special.ItemSoulkey;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.client.keybinds.KeyHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventListener {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(event.getEntityPlayer());
        Item item = event.getItemStack().getItem();

        if(pml != null) {
            if (item instanceof ItemSoulgazer) {
                if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().isEmpty()) {
                    event.getToolTip().addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(
                            I18n.format("item.soulgazer.description.pmlsoulgazer"), ItemBase.DESCRIPTION_WIDTH));
                }
            }
        }

        if(ForgeConfigHandler.integrationConfig.soulgazerBauble){
            if (item instanceof ItemSoulgazer) {
                ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.getEntityPlayer());
                if (ltp != null) {
                    int autoID = ltp.getSoulgazerAutoToggle();
                    int manualID = ltp.getSoulgazerManualToggle() ? 1 : 2;
                    event.getToolTip().add(
                            I18n.format("item.soulgazer.description.keybind.auto." + autoID)
                            + I18n.format("item.lycanitestweaks.tooltip.keybind", KeyHandler.TOGGLE_SOULGAZER_AUTO.getDisplayName())
                    );
                    event.getToolTip().add(
                            I18n.format("item.soulgazer.description.keybind.manual." + manualID)
                            + I18n.format("item.lycanitestweaks.tooltip.keybind", KeyHandler.TOGGLE_SOULGAZER_MANUAL.getDisplayName())
                    );
                }
            }
        }

        if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.babyAgeGapple){
            if(item instanceof ItemAppleGold && event.getItemStack().getMetadata() > 0){
                event.getToolTip().add(I18n.format("item.appleGold.description.baby"));
            }
        }

        if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.sizeChangeFoods){
            if(item instanceof GenericFoodItem){
                if(((GenericFoodItem) item).itemName.equals("battle_burrito")){
                    event.getToolTip().add(I18n.format("item.battle_burrito.description.sizechange"));
                }
                else if (((GenericFoodItem) item).itemName.equals("explorers_risotto")) {
                    event.getToolTip().add(I18n.format("item.explorers_risotto.description.sizechange"));
                }
            }
        }

        if(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.soulkeysSetVariant){
            if(item instanceof ItemSoulkey){
                event.getToolTip().add(I18n.format("item.soulkey.description.setvariant"));
            }
        }
    }
}
