package lycanitestweaks.network;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.pets.PetEntry;
import io.netty.buffer.ByteBuf;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketKeybindsKeyboundPetEntry implements IMessage {

    private UUID keyboundPetEntryID;
    private boolean isIDNull;

    public PacketKeybindsKeyboundPetEntry() {}
    public PacketKeybindsKeyboundPetEntry(LycanitesTweaksPlayerCapability ltp) {
        this.keyboundPetEntryID = ltp.getKeyboundPetID();
        this.isIDNull = (this.keyboundPetEntryID == null);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.isIDNull = packet.readBoolean();
        if(!this.isIDNull) this.keyboundPetEntryID = packet.readUniqueId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeBoolean(this.isIDNull);
        if(!this.isIDNull) packet.writeUniqueId(this.keyboundPetEntryID);
    }


    public static class ServerHandler implements IMessageHandler<PacketKeybindsKeyboundPetEntry, IMessage> {

        @Override
        public IMessage onMessage(final PacketKeybindsKeyboundPetEntry message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketKeybindsKeyboundPetEntry message, MessageContext ctx) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(ctx.getServerHandler().player);
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(ctx.getServerHandler().player);
            if(ltp != null && extendedPlayer != null){
                PetEntry petEntry = extendedPlayer.petManager.getEntry(message.keyboundPetEntryID);
                if(petEntry != null) ltp.setKeyboundPet(petEntry);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketKeybindsKeyboundPetEntry, IMessage> {

        @Override
        public IMessage onMessage(PacketKeybindsKeyboundPetEntry message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(Minecraft.getMinecraft().player);
                ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(Minecraft.getMinecraft().player);
                if(ltp != null && extendedPlayer != null){
                    PetEntry petEntry = extendedPlayer.petManager.getEntry(message.keyboundPetEntryID);
                    if(petEntry != null) ltp.setKeyboundPet(petEntry);
                }
            });
            return null;
        }
    }
}
