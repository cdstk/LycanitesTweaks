package lycanitestweaks.compat;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import com.lycanitesmobs.ObjectManager;
import lycanitestweaks.capability.toggleableitem.IToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketToggleableBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

    public static int getBaubleCount(EntityPlayer player, Item item) {
        int count = 0;

        IBaublesItemHandler baubleItems = BaublesApi.getBaublesHandler(player);
        if(baubleItems != null) {
            for (int i = 0; i < baubleItems.getSlots(); i++) {
                ItemStack bauble = baubleItems.getStackInSlot(i);
                if(bauble.getItem() == item)
                    count++;
            }
        }

        return count;
    }

    // Order should be (Keybind) -> Server -> Sync -> Client
    public static void toggleBaublePassive(EntityPlayer player, int slot) {
        if(player.world.isRemote) return;

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
                        syncBauble(player, i, toggle);
                    }
                }
                player.sendStatusMessage(new TextComponentTranslation("message.bauble.toggle.all", String.valueOf(count), toggle ? "I" : "O"), true);
            }
            else {
                ItemStack bauble = baubleItems.getStackInSlot(slot);
                IToggleableItem toggleableItem = ToggleableItem.getForItemStack(bauble);
                if(toggleableItem != null) {
                    toggleableItem.toggleAbility(!toggleableItem.isAbilityToggled(), bauble, player);
                    syncBauble(player, slot, toggleableItem.isAbilityToggled());
                    player.sendStatusMessage(new TextComponentTranslation("message.bauble.toggle.specific", toggleableItem.isAbilityToggled() ? "I" : "O", bauble.getDisplayName()), true);
                }
            }
        }
    }

    public static void setClientToggle(EntityPlayer player, int slot, boolean toggle) {
        if(!player.world.isRemote) return;
        if(slot < 0) return;

        IBaublesItemHandler baubleItems = BaublesApi.getBaublesHandler(player);
        if(baubleItems != null && slot < baubleItems.getSlots()) {
            ItemStack bauble = baubleItems.getStackInSlot(slot);
            IToggleableItem toggleableItem = ToggleableItem.getForItemStack(bauble);
            if(toggleableItem != null) {
                toggleableItem.toggleAbility(toggle);
            }
        }
    }

    private static void syncBauble(EntityPlayer player, int slot, boolean toggle) {
        PacketToggleableBauble packet = new PacketToggleableBauble(slot, toggle);
        if(player instanceof EntityPlayerMP) PacketHandler.instance.sendTo(packet, (EntityPlayerMP) player);
        PacketHandler.instance.sendToAllTracking(packet, player);
    }
}
