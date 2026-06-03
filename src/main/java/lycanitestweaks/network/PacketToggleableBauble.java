package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.compat.BaublesHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketToggleableBauble implements IMessage {

    private int slot;
    private boolean toggle = false;

    public PacketToggleableBauble() {}

    public PacketToggleableBauble(int slot) {
        this.slot = slot;
    }

    public PacketToggleableBauble(int slot, boolean toggle) {
        this(slot);
        this.toggle = toggle;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.slot = packet.readInt();
        this.toggle = packet.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeInt(this.slot);
        packet.writeBoolean(this.toggle);
    }

    public static class ServerHandler implements IMessageHandler<PacketToggleableBauble, IMessage> {

        @Override
        public IMessage onMessage(final PacketToggleableBauble message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketToggleableBauble message, MessageContext ctx) {
            if (ModLoadedUtil.baubles.isLoaded()) {
                BaublesHandler.toggleBaublePassive(ctx.getServerHandler().player, message.slot);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketToggleableBauble, IMessage> {

        @Override
        public IMessage onMessage(PacketToggleableBauble message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (ModLoadedUtil.baubles.isLoaded()) {
                    BaublesHandler.setClientToggle(Minecraft.getMinecraft().player, message.slot, message.toggle);
                }
            });
            return null;
        }
    }
}
