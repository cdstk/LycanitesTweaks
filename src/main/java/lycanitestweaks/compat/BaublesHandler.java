package lycanitestweaks.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.lycanitesmobs.ObjectManager;
import lycanitestweaks.capability.toggleableitem.IToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

public class BaublesHandler {

    public static boolean hasSoulgazerBauble(EntityPlayer player){
        int baubleFound = BaublesApi.isBaubleEquipped(player, ObjectManager.getItem("soulgazer"));
        return (baubleFound != -1);
    }

    public static boolean hasItemAsBauble(EntityPlayer player, Item item){
        int baubleFound = BaublesApi.isBaubleEquipped(player, item);
        return (baubleFound != -1);
    }

    public static void toggleBaublePassive(EntityPlayer player, int slot) {
        IBaublesItemHandler baubleItems = BaublesApi.getBaublesHandler(player);
        if(baubleItems != null) {
            int count = 0;
            if(slot == -1) {
                boolean toggle = false;
                for (int i = 0; i < baubleItems.getSlots(); i++) {
                    ItemStack bauble = baubleItems.getStackInSlot(i);
                    IToggleableItem toggleableItem = ToggleableItem.getForItemStack(bauble);
                    if(toggleableItem != null) {
                        count++;
                        if(count == 1) toggle = !toggleableItem.isAbilityToggled();
                        toggleableItem.toggleAbility(toggle, bauble, player);
                    }
                }
                player.sendStatusMessage(new TextComponentTranslation("message.bauble.toggle.all", String.valueOf(count), toggle ? "I" : "O"), true);
            }
            else {
                ItemStack bauble = baubleItems.getStackInSlot(slot);
                IToggleableItem toggleableItem = ToggleableItem.getForItemStack(bauble);
                if(toggleableItem != null) {
                    toggleableItem.toggleAbility(!toggleableItem.isAbilityToggled(), bauble, player);
                    player.sendStatusMessage(new TextComponentTranslation("message.bauble.toggle.specific", toggleableItem.isAbilityToggled() ? "I" : "O", bauble.getDisplayName()), true);
                }
            }
        }
    }
}
