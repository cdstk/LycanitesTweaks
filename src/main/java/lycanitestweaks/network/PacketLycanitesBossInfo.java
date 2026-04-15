package lycanitestweaks.network;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import io.netty.buffer.ByteBuf;
import lycanitestweaks.util.IGuiBossOverlay_LycanitesBossMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.BossInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketLycanitesBossInfo implements IMessage {

    boolean clientRequest = false;
    private int entityID = -1;
    private UUID bossInfoUUID = null;

    public PacketLycanitesBossInfo() {}
    public PacketLycanitesBossInfo(EntityLiving entity, BossInfo bossInfo) {
        this.entityID = entity.getEntityId();
        this.bossInfoUUID = bossInfo.getUniqueId();
    }
    public PacketLycanitesBossInfo(EntityLiving entity) {
        this.clientRequest = true;
        this.entityID = entity.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        this.clientRequest = packet.readBoolean();
        this.entityID = packet.readInt();
        if(!this.clientRequest) {
            this.bossInfoUUID = packet.readUniqueId();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packet = new PacketBuffer(buf);
        packet.writeBoolean(this.clientRequest);
        packet.writeInt(this.entityID);
        if(!this.clientRequest) {
            packet.writeUniqueId(this.bossInfoUUID);
        }
    }

    public static class ServerHandler implements IMessageHandler<PacketLycanitesBossInfo, IMessage> {

        @Override
        public IMessage onMessage(final PacketLycanitesBossInfo message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketLycanitesBossInfo message, MessageContext ctx) {
            if(!message.clientRequest) return;
            EntityPlayerMP player = ctx.getServerHandler().player;
            Entity entity = player.world.getEntityByID(message.entityID);
            if(entity instanceof BaseCreatureEntity) {
                BossInfo bossInfo = ((BaseCreatureEntity) entity).getBossInfo();
                if(bossInfo != null) {
                    PacketHandler.instance.sendTo(new PacketLycanitesBossInfo((BaseCreatureEntity) entity, bossInfo), player);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientHandler implements IMessageHandler<PacketLycanitesBossInfo, IMessage> {

        @Override
        public IMessage onMessage(PacketLycanitesBossInfo message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if(message.clientRequest) return;
                Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityID);
                GuiBossOverlay bossOverlay = Minecraft.getMinecraft().ingameGUI.getBossOverlay();
                if(entity instanceof BaseCreatureEntity && bossOverlay instanceof IGuiBossOverlay_LycanitesBossMixin) {
                    ((IGuiBossOverlay_LycanitesBossMixin) bossOverlay).lycanitesTweaks$updateLycanitesBossInfo(message.bossInfoUUID, (BaseCreatureEntity) entity);
                }
            });
            return null;
        }
    }
}
