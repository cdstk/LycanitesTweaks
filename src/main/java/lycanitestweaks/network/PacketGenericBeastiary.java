package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.info.beastiary.GenericBestiary;
import lycanitestweaks.info.beastiary.GenericEntityKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class PacketGenericBeastiary implements IMessage {

	// TODO Fix Original Lycanites Packet crashing when reaching size limit
	private static final int BATCH_SIZE = 200; // Lycanites documents possible OOM

	private boolean firstBatch = false;
	private int entryAmount = 0;
	private String[] creatureNames;
	private int[] ranks;
	private int[] experience;


	// ==================================================
	//                    Constructors
	// ==================================================
	public PacketGenericBeastiary() {}
	public PacketGenericBeastiary(List<GenericEntityKnowledge> knowledgeList) {
		this.entryAmount = knowledgeList.size();
		if(this.entryAmount > 0) {
			this.creatureNames = new String[this.entryAmount];
			this.ranks = new int[this.entryAmount];
			this.experience = new int[this.entryAmount];

			int i = 0;
			for(GenericEntityKnowledge entityKnowledge : knowledgeList) {
				this.creatureNames[i] = entityKnowledge.entityID;
				this.ranks[i] = entityKnowledge.rank;
				this.experience[i] = entityKnowledge.experience;
				i++;
			}
		}
	}

	public static void sendAllEntriesToClient(GenericBestiary bestiary, EntityPlayerMP player) {
		List<GenericEntityKnowledge> knowledgeBatch = new ArrayList<>();
		boolean first = true;

		for(GenericEntityKnowledge entityKnowledge : bestiary.entityKnowledgeMap.values()) {
			knowledgeBatch.add(entityKnowledge);
			if(knowledgeBatch.size() >= BATCH_SIZE) {
				sendKnowledgeBatch(new ArrayList<>(knowledgeBatch), player, first);
				first = false;

				knowledgeBatch.clear();
			}
		}

		sendKnowledgeBatch(new ArrayList<>(knowledgeBatch), player, first);
	}

	private static void sendKnowledgeBatch(List<GenericEntityKnowledge> knowledgeList, EntityPlayerMP player, boolean firstBatch) {
		PacketGenericBeastiary message = new PacketGenericBeastiary(knowledgeList);
		message.firstBatch = firstBatch;
		PacketHandler.instance.sendTo(message, player);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		this.firstBatch = packet.readBoolean();
		this.entryAmount = packet.readInt();
		if(this.entryAmount >= BATCH_SIZE) {
			this.entryAmount = BATCH_SIZE;
			LycanitesTweaks.LOGGER.log(Level.WARN, "Received {} or more creature entries, something went wrong with the Beastiary packet! Additional entries will be skipped to prevent OOM!", BATCH_SIZE);
		}
		if(this.entryAmount > 0) {
			this.creatureNames = new String[this.entryAmount];
			this.ranks = new int[this.entryAmount];
			this.experience = new int[this.entryAmount];
			for(int i = 0; i < this.entryAmount; i++) {
				this.creatureNames[i] = packet.readString(32767);
				this.ranks[i] = packet.readInt();
				this.experience[i] = packet.readInt();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeBoolean(this.firstBatch);
		packet.writeInt(this.entryAmount);
		if(this.entryAmount > 0) {
			for(int i = 0; i < this.entryAmount; i++) {
				packet.writeString(this.creatureNames[i]);
				packet.writeInt(this.ranks[i]);
				packet.writeInt(this.experience[i]);
			}
		}
	}

	public static class ServerHandler implements IMessageHandler<PacketGenericBeastiary, IMessage> {

		@Override
		public IMessage onMessage(PacketGenericBeastiary message, MessageContext ctx) {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientHandler implements IMessageHandler<PacketGenericBeastiary, IMessage> {

		@Override
		public IMessage onMessage(PacketGenericBeastiary message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(Minecraft.getMinecraft().player);
				if(ltp != null) {
					if(message.firstBatch) ltp.getBestiary().entityKnowledgeMap.clear();
					for (int i = 0; i < message.entryAmount; i++) {
						String creatureName = message.creatureNames[i];
						int rank = message.ranks[i];
						int experience = message.experience[i];
						GenericEntityKnowledge creatureKnowledge = new GenericEntityKnowledge(ltp.getBestiary(), creatureName, rank, experience);
						ltp.getBestiary().entityKnowledgeMap.put(creatureKnowledge.entityID, creatureKnowledge);
					}
				}
			});
			return null;
		}
	}
}
