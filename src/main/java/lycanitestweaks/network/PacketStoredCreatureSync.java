package lycanitestweaks.network;

import io.netty.buffer.ByteBuf;
import lycanitestweaks.capability.entitystorecreature.EntityStoreCreatureCapabilityHandler;
import lycanitestweaks.capability.entitystorecreature.IEntityStoreCreatureCapability;
import lycanitestweaks.storedcreatureentity.StoredCreatureEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketStoredCreatureSync implements IMessage {

    private boolean clientRequest = false;
    private UUID clientEntityUUID = null;

    private int hostEntityID = -1;
    private String creatureTypeName;
    private double entitySize;
    private int subspeciesIndex;
    private int variantIndex;

    public PacketStoredCreatureSync() {}
    public PacketStoredCreatureSync(IEntityStoreCreatureCapability storedCreatureCapability) {
        if(storedCreatureCapability.getHost().getEntityWorld().isRemote) {
            this.clientRequest = true;
            this.clientEntityUUID = storedCreatureCapability.getHost().getUniqueID();
        }
        else {
            StoredCreatureEntity storedCreature = storedCreatureCapability.getStoredCreatureEntity();
            this.hostEntityID = storedCreatureCapability.getHost().getEntityId();
            this.creatureTypeName = storedCreature.creatureTypeName;
            this.entitySize = storedCreature.entitySize;
            this.subspeciesIndex = storedCreature.subspeciesIndex;
            this.variantIndex = storedCreature.variantIndex;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.clientRequest = packet.readBoolean();

        if(this.clientRequest) {
            this.clientEntityUUID = packet.readUniqueId();
        }
        else {
            this.hostEntityID = packet.readInt();
            this.creatureTypeName = packet.readString(256);
            this.entitySize = packet.readDouble();
            this.subspeciesIndex = packet.readInt();
            this.variantIndex = packet.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeBoolean(this.clientRequest);
        if(this.clientRequest) {
            packet.writeUniqueId(this.clientEntityUUID);
        }
        else {
            packet.writeInt(this.hostEntityID);
            packet.writeString(this.creatureTypeName);
            packet.writeDouble(this.entitySize);
            packet.writeInt(this.subspeciesIndex);
            packet.writeInt(this.variantIndex);
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketStoredCreatureSync, IMessage> {

        @Override
        public IMessage onMessage(final PacketStoredCreatureSync message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketStoredCreatureSync message, MessageContext ctx) {
            if(!message.clientRequest) return;
            EntityPlayerMP player = ctx.getServerHandler().player;
            Entity entity = player.getServerWorld().getEntityFromUuid(message.clientEntityUUID);
            if(entity == null) return;

            IEntityStoreCreatureCapability storeCreatureCapability = entity.getCapability(EntityStoreCreatureCapabilityHandler.ENTITY_STORE_CREATURE, null);
            if(storeCreatureCapability != null) {
                PacketHandler.instance.sendTo(new PacketStoredCreatureSync(storeCreatureCapability), player);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketStoredCreatureSync, IMessage> {

        @Override
        public IMessage onMessage(PacketStoredCreatureSync message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.hostEntityID);
                if(entity == null || message.clientRequest) return;

                IEntityStoreCreatureCapability storeCreatureCapability = entity.getCapability(EntityStoreCreatureCapabilityHandler.ENTITY_STORE_CREATURE, null);
                if(storeCreatureCapability != null) {
                    StoredCreatureEntity storedCreature = storeCreatureCapability.getStoredCreatureEntity();
                    storedCreature.creatureTypeName = message.creatureTypeName;
                    storedCreature.entitySize = message.entitySize;
                    storedCreature.subspeciesIndex = message.subspeciesIndex;
                    storedCreature.variantIndex = message.variantIndex;
                    storeCreatureCapability.setStoredCreatureEntity(storedCreature);
                }
            });
            return null;
        }
    }
}
