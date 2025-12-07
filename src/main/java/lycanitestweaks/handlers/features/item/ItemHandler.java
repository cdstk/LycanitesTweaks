package lycanitestweaks.handlers.features.item;

import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.IItemStaffSummoningElementLevelMapMixin;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Level;

public class ItemHandler {

    // Using this as Forge Capabilities on ItemStack seems to attempt attaching the same stack a lot
    // Either should use Forge Caps are just settle with this disaster
    @SubscribeEvent
    public static void checkStaffElementLevels(LivingEquipmentChangeEvent event){
        if(event.getTo().getItem() instanceof IItemStaffSummoningElementLevelMapMixin) {
            if (event.getSlot() == EntityEquipmentSlot.MAINHAND || event.getSlot() == EntityEquipmentSlot.OFFHAND) {
                if(ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "Found LevelMapItem in slot:{}", event.getSlot());
                ((IItemStaffSummoningElementLevelMapMixin) event.getTo().getItem()).lycanitesTweaks$setItemStack(event.getTo());
            }
        }
    }

    @SubscribeEvent
    public static void onCraftEquipmentPart(PlayerEvent.ItemCraftedEvent event){
        ItemStack stack = event.crafting;
        if(stack.getItem() instanceof ItemEquipmentPart && ((ItemEquipmentPart) stack.getItem()).itemName.equals("diamondpaxel")){
            ((ItemEquipmentPart) stack.getItem()).setLevel(stack, ForgeConfigHandler.minorFeaturesConfig.diamondPaxelLevel);
        }
    }
}
