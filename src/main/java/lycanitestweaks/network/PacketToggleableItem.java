package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.capability.toggleableitem.IToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import lycanitestweaks.compat.BaublesHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketToggleableItem implements IMessage {

    private boolean isClient;
    private boolean baubleOrEnabled; // isBauble
    private int slotOrMode; // Slot
    private ItemStack itemStack = ItemStack.EMPTY;

    public PacketToggleableItem() {}

    public PacketToggleableItem(boolean baubleOrEnabled, int slotOrMode) {
        this(true, ItemStack.EMPTY,baubleOrEnabled, slotOrMode);
    }

    public PacketToggleableItem(boolean isClient, ItemStack itemStack, boolean baubleOrEnabled, int slotOrMode) {
        this.isClient = isClient;
        this.baubleOrEnabled = baubleOrEnabled;
        this.slotOrMode = slotOrMode;
        this.itemStack = itemStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.isClient = packet.readBoolean();
        this.baubleOrEnabled = packet.readBoolean();
        this.slotOrMode = packet.readInt();
        try {
            if(!this.isClient)
                this.itemStack = packet.readItemStack();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeBoolean(this.isClient);
        packet.writeBoolean(this.baubleOrEnabled);
        packet.writeInt(this.slotOrMode);

        if(!this.isClient)
            packet.writeItemStack(this.itemStack);
    }

    public static class ServerHandler implements IMessageHandler<PacketToggleableItem, IMessage> {

        @Override
        public IMessage onMessage(final PacketToggleableItem message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketToggleableItem message, MessageContext ctx) {
            if(!message.isClient) return;
            if(message.itemStack != ItemStack.EMPTY) return;

            if (message.baubleOrEnabled && ModLoadedUtil.baubles.isLoaded()) {
                BaublesHandler.toggleBaublePassive(ctx.getServerHandler().player, message.slotOrMode);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketToggleableItem, IMessage> {

        @Override
        public IMessage onMessage(PacketToggleableItem message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if(message.isClient) return;
                if(message.itemStack == ItemStack.EMPTY) return;
                IToggleableItem toggleableItem = ToggleableItem.getForItemStack(message.itemStack);
                if(toggleableItem != null) {
                    toggleableItem.toggleAbility(message.baubleOrEnabled);
                    toggleableItem.toggleMode(message.slotOrMode);
                    toggleableItem.sync(message.itemStack, Minecraft.getMinecraft().player);
                }
            });
            return null;
        }
    }
}
