package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.info.beastiary.GenericEntityKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

public class PacketGenericEntityKnowledge implements IMessage {

	public String entityID;
	public int rank;
	public int experience;


	// ==================================================
	//                    Constructors
	// ==================================================
	public PacketGenericEntityKnowledge() {}
	public PacketGenericEntityKnowledge(GenericEntityKnowledge creatureKnowledge) {
		this.entityID = creatureKnowledge.entityID;
		this.rank = creatureKnowledge.rank;
		this.experience = creatureKnowledge.experience;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		try {
			this.entityID = packet.readString(32767);
			this.rank = packet.readInt();
			this.experience = packet.readInt();
		}
		catch(Exception e) {
			LycanitesTweaks.LOGGER.log( Level.WARN,"There was a problem decoding the packet: {}.", packet);
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeString(this.entityID);
		packet.writeInt(this.rank);
		packet.writeInt(this.experience);
	}

	public static class ServerHandler implements IMessageHandler<PacketGenericEntityKnowledge, IMessage> {

		@Override
		public IMessage onMessage(PacketGenericEntityKnowledge message, MessageContext ctx) {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientHandler implements IMessageHandler<PacketGenericEntityKnowledge, IMessage> {

		@Override
		public IMessage onMessage(PacketGenericEntityKnowledge message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(Minecraft.getMinecraft().player);
				if(ltp != null) {
					ltp.getBestiary().updateEntityKnowledge(
							new GenericEntityKnowledge(
									ltp.getBestiary(),
									message.entityID,
									message.rank,
									message.experience),
							false
					);
				}
			});
			return null;
		}
	}
}
