package lycanitestweaks.network;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import io.netty.buffer.ByteBuf;
import lycanitestweaks.util.IBaseCreatureEntity_AnimatedGoalMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketForceGoalAnimationUpdate implements IMessage {

    private int creatureEntityID = -1;
    private int abilityTime = 0;


    public PacketForceGoalAnimationUpdate() {}
    public PacketForceGoalAnimationUpdate(BaseCreatureEntity creature, int abilityTime) {
        this.creatureEntityID = creature.getEntityId();
        this.abilityTime = abilityTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.creatureEntityID = packet.readInt();
        this.abilityTime = packet.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeInt(this.creatureEntityID);
        packet.writeInt(this.abilityTime);
    }

    public static class ServerHandler implements IMessageHandler<PacketForceGoalAnimationUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketForceGoalAnimationUpdate message, MessageContext ctx) {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketForceGoalAnimationUpdate, IMessage> {

        @Override
        public IMessage onMessage(PacketForceGoalAnimationUpdate message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.creatureEntityID);
                if(entity instanceof IBaseCreatureEntity_AnimatedGoalMixin) {
                    ((IBaseCreatureEntity_AnimatedGoalMixin) entity).lycanitesTweaks$updateClientForceGoalTime(message.abilityTime);
                }
            });
            return null;
        }
    }
}
