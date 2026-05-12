package lycanitestweaks.proxy;

import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.client.gui.overlays.AmalgalichBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.AsmodeusBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.LycanitesBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.RahovartBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.SpawnedAsBossInfoOverlay;
import lycanitestweaks.client.renderer.entity.RenderBossSummonCrystal;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.handlers.ClientModRegistry;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        ClientModRegistry.init();
        RenderingRegistry.registerEntityRenderingHandler(EntityBossSummonCrystal.class, RenderBossSummonCrystal::new);
        MinecraftForge.EVENT_BUS.register(ClientEventListener.class);

        if (ForgeConfigHandler.clientFeaturesMixinConfig.lycanitesBossInfoOverlay)
            MinecraftForge.EVENT_BUS.register(SpawnedAsBossInfoOverlay.class);
        if (ForgeConfigHandler.clientFeaturesMixinConfig.rahovartInfoOverlay)
            MinecraftForge.EVENT_BUS.register(RahovartBossInfoOverlay.class);
        if (ForgeConfigHandler.clientFeaturesMixinConfig.asmodeusInfoOverlay)
            MinecraftForge.EVENT_BUS.register(AsmodeusBossInfoOverlay.class);
        if (ForgeConfigHandler.clientFeaturesMixinConfig.amalgalichInfoOverlay)
            MinecraftForge.EVENT_BUS.register(AmalgalichBossInfoOverlay.class);

        PacketHandler.registerClientMessages();
        GenericEntityInfoManager.getInstance().setClientSide(true);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
        LycanitesBossInfoOverlay.initClientReferences();
    }

    @Override
    public boolean isSinglePlayer() {
        return Minecraft.getMinecraft().isSingleplayer();
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public boolean hasLangKey(String langKey) {
        return I18n.hasKey(langKey);
    }

    @Override
    public String formatLangKey(String langKey) {
        return I18n.format(langKey);
    }
}