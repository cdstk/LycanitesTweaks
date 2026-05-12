package lycanitestweaks.proxy;

import com.lycanitesmobs.LycanitesMobs;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {

    public void preInit() {
        LycanitesTweaksRegistry.init();
        PacketHandler.registerMessages(LycanitesTweaks.MODID);
        GenericEntityInfoManager.getInstance().preInit(LycanitesMobs.modInfo);
    }

    public void init(){

    }

    public void postInit() {
        GenericEntityInfoManager.getInstance().postInit(LycanitesMobs.modInfo);
    }

    public boolean isSinglePlayer(){
        return false;
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public boolean hasLangKey(String langKey) {
        return false;
    }

    public String formatLangKey(String langKey) {
        return langKey;
    }
}