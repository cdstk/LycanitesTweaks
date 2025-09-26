package lycanitestweaks.handlers;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.client.keybinds.KeyHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID, value = Side.CLIENT)
public class ClientModRegistry {

    public static void init() {
        KeyHandler.init();
        MinecraftForge.EVENT_BUS.register(KeyHandler.class);
    }
}