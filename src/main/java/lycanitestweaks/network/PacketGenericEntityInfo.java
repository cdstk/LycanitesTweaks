package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class PacketGenericEntityInfo implements IMessage {

	private String entityID = "";
	private boolean potentialSpawn = false;


	// ==================================================
	//                    Constructors
	// ==================================================
	public PacketGenericEntityInfo() {}
	public PacketGenericEntityInfo(GenericEntityInfo entityInfo) {
		this.entityID = entityInfo.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		this.potentialSpawn = packet.readBoolean();
		this.entityID = packet.readString(32767);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer packet = new PacketBuffer(buf);
		packet.writeBoolean(this.potentialSpawn);
		packet.writeString(this.entityID);
	}

	public static class ServerHandler implements IMessageHandler<PacketGenericEntityInfo, IMessage> {

		@Override
		public IMessage onMessage(final PacketGenericEntityInfo message, final MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private static void handle(PacketGenericEntityInfo message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(message.entityID);
			if(entityInfo != null) {

				potentialSpawnCheck:
				for (EnumCreatureType creatureType : EnumCreatureType.values()) {
					List<Biome.SpawnListEntry> spawnEntries = player.getServerWorld().getChunkProvider().getPossibleCreatures(creatureType, player.getPosition());
					spawnEntries = net.minecraftforge.event.ForgeEventFactory.getPotentialSpawns(player.getServerWorld(), creatureType, player.getPosition(), spawnEntries);
					if(spawnEntries == null) continue;

					for(Biome.SpawnListEntry spawnEntry : spawnEntries) {
						if (spawnEntry.entityClass.equals(entityInfo.getEntityClass())) {
							message.potentialSpawn = true;
							break potentialSpawnCheck;
						}
					}
				}
				PacketHandler.instance.sendTo(message, player);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class ClientHandler implements IMessageHandler<PacketGenericEntityInfo, IMessage> {

		@Override
		public IMessage onMessage(PacketGenericEntityInfo message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(message.entityID);
				entityInfo.potentialSpawn = message.potentialSpawn;
			});
			return null;
		}
	}
}
