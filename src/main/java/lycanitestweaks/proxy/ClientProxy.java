package lycanitestweaks.proxy;

import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.client.renderer.entity.RenderBossSummonCrystal;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.handlers.ClientModRegistry;
import lycanitestweaks.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        ClientModRegistry.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityBossSummonCrystal.class, RenderBossSummonCrystal::new);
        MinecraftForge.EVENT_BUS.register(ClientEventListener.class);
        PacketHandler.registerClientMessages();
    }

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}