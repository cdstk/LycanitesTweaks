package lycanitestweaks.proxy;

import lycanitestweaks.handlers.ClientModRegistry;
import lycanitestweaks.network.PacketHandler;

public class ClientProxy extends CommonProxy {

    private int thirdPersonViewMount = 0;

    @Override
    public void preInit() {
        super.preInit();
        ClientModRegistry.init();
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

    @Override
    public int getMount3rdPersonView() {
        return this.thirdPersonViewMount;
    }

    @Override
    public void setMount3rdPersonView(int view) {
        this.thirdPersonViewMount = view;
    }
}