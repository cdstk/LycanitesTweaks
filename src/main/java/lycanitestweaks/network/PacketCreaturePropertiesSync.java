package lycanitestweaks.network;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketCreaturePropertiesSync implements IMessage {

    private int creatureEntityID = -1;
    private NBTTagCompound extraMobBehaviourNBT = null;


    public PacketCreaturePropertiesSync() {}
    public PacketCreaturePropertiesSync(BaseCreatureEntity creature) {
        this.creatureEntityID = creature.getEntityId();
        this.extraMobBehaviourNBT = new NBTTagCompound();
        creature.extraMobBehaviour.writeToNBT(this.extraMobBehaviourNBT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.creatureEntityID = packet.readInt();
        if(this.creatureEntityID != -1){
            try {
                this.extraMobBehaviourNBT = packet.readCompoundTag();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeInt(this.creatureEntityID);
        if(this.extraMobBehaviourNBT != null)
            packet.writeCompoundTag(this.extraMobBehaviourNBT);
    }


    public static class ServerHandler implements IMessageHandler<PacketCreaturePropertiesSync, IMessage> {

        @Override
        public IMessage onMessage(PacketCreaturePropertiesSync message, MessageContext ctx) {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketCreaturePropertiesSync, IMessage> {

        @Override
        public IMessage onMessage(PacketCreaturePropertiesSync message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.creatureEntityID);
                if(entity instanceof BaseCreatureEntity && message.extraMobBehaviourNBT != null) {
                    ((BaseCreatureEntity) entity).extraMobBehaviour.readFromNBT(message.extraMobBehaviourNBT);
                }
            });
            return null;
        }
    }
}
