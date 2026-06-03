package lycanitestweaks.network;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import io.netty.buffer.ByteBuf;
import lycanitestweaks.util.IBaseCreatureEntity_AnimatedGoalMixin;
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

public class PacketForceGoalAnimationUpdate implements IMessage {

    private boolean toServer = false;
    private UUID uuid;

    private int creatureEntityID = -1;
    private int abilityTime = 0;

    public PacketForceGoalAnimationUpdate() {}
    public PacketForceGoalAnimationUpdate(BaseCreatureEntity creature) {
        this.toServer = true;
        this.uuid = creature.getUniqueID();
    }
    public PacketForceGoalAnimationUpdate(BaseCreatureEntity creature, int abilityTime) {
        this.toServer = false;
        this.creatureEntityID = creature.getEntityId();
        this.abilityTime = abilityTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.toServer = packet.readBoolean();

        if(this.toServer) {
            this.uuid = packet.readUniqueId();
        }
        else {
            this.creatureEntityID = packet.readInt();
            this.abilityTime = packet.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeBoolean(this.toServer);

        if(this.toServer) {
            packet.writeUniqueId(this.uuid);
        }
        else {
            packet.writeInt(this.creatureEntityID);
            packet.writeInt(this.abilityTime);
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketForceGoalAnimationUpdate, IMessage> {

        @Override
        public IMessage onMessage(final PacketForceGoalAnimationUpdate message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketForceGoalAnimationUpdate message, MessageContext ctx) {
            if(!message.toServer) return;

            EntityPlayerMP player = ctx.getServerHandler().player;
            Entity entity = player.getServerWorld().getEntityFromUuid(message.uuid);
            if(entity instanceof IBaseCreatureEntity_AnimatedGoalMixin && entity instanceof BaseCreatureEntity) {
                PacketHandler.instance.sendTo(
                        new PacketForceGoalAnimationUpdate(
                                (BaseCreatureEntity) entity,
                                ((IBaseCreatureEntity_AnimatedGoalMixin) entity).lycanitesTweaks$getServerForceGoalTime()),
                        player
                );
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketForceGoalAnimationUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketForceGoalAnimationUpdate message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if(message.toServer) return;

                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.creatureEntityID);
                if(entity instanceof IBaseCreatureEntity_AnimatedGoalMixin) {
                    ((IBaseCreatureEntity_AnimatedGoalMixin) entity).lycanitesTweaks$updateClientForceGoalTime(message.abilityTime);
                }
            });
            return null;
        }
    }
}
