package lycanitestweaks.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketHandler {

    public static SimpleNetworkWrapper instance = null;

    public static void registerMessages(String channelName) {
        instance = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        instance.registerMessage(PacketPlayerMobLevelsStats.ServerHandler.class, PacketPlayerMobLevelsStats.class, 1, Side.SERVER);
        instance.registerMessage(PacketExtendedPlayerSelectedCreature.ServerHandler.class, PacketExtendedPlayerSelectedCreature.class, 2, Side.SERVER);
    }

    @SideOnly(Side.CLIENT)
    public static void registerClientMessages() {
        instance.registerMessage(PacketPlayerMobLevelsStats.ClientHandler.class, PacketPlayerMobLevelsStats.class, 1, Side.CLIENT);
    }
}
