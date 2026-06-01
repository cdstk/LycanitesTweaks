package lycanitestweaks.proxy;

import lycanitestweaks.client.ClientEventListener;
import lycanitestweaks.client.LycanitesAssetReloader;
import lycanitestweaks.client.gui.overlays.AmalgalichBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.AsmodeusBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.LycanitesBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.RahovartBossInfoOverlay;
import lycanitestweaks.client.gui.overlays.SpawnedAsBossInfoOverlay;
import lycanitestweaks.client.renderer.entity.RenderBossSummonCrystal;
import lycanitestweaks.client.renderer.entity.layers.LayerObjHellShield;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.handlers.ClientModRegistry;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Map;

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
        final Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        RenderPlayer render;
        render = skinMap.get("default");
        render.addLayer(LycanitesAssetReloader.addGenericRenderLayer(new LayerObjHellShield()));

        render = skinMap.get("slim");
        render.addLayer(LycanitesAssetReloader.addGenericRenderLayer(new LayerObjHellShield()));
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
        LycanitesBossInfoOverlay.initClientReferences();
        IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if(resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager)resourceManager).registerReloadListener(LycanitesAssetReloader.getInstance());
        }
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