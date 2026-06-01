package lycanitestweaks.handlers;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.client.LycanitesAssetReloader;
import lycanitestweaks.client.keybinds.KeyHandler;
import lycanitestweaks.client.renderer.tile.RenderDevilGatlingGun;
import lycanitestweaks.client.renderer.tile.RenderHellShield;
import lycanitestweaks.client.renderer.tile.RenderHellfireCannon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID, value = Side.CLIENT)
public class ClientModRegistry {

    public static void init() {
        KeyHandler.init();
        MinecraftForge.EVENT_BUS.register(KeyHandler.class);
    }

    @SubscribeEvent
    public static void registerModelEvent(ModelRegistryEvent event) {
        registerModel(LycanitesTweaksRegistry.challengeSoulStaff, "inventory");
        registerModel(LycanitesTweaksRegistry.eventSoulStaff, "inventory");
        registerModel(LycanitesTweaksRegistry.chargeStaff, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkey, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkeyDiamond, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkeyEmerald, "inventory");

        registerModel(LycanitesTweaksRegistry.vileMatter, "inventory");
        registerModel(LycanitesTweaksRegistry.fantasticalFeast, "inventory");

        registerTESR(LycanitesTweaksRegistry.devilGatlingGun, "inventory", LycanitesAssetReloader.addGenericStackRenderer(new RenderDevilGatlingGun()));
        registerTESR(LycanitesTweaksRegistry.hellShield, "inventory", LycanitesAssetReloader.addGenericStackRenderer(new RenderHellShield()));
        registerTESR(LycanitesTweaksRegistry.hellfireCannon, "inventory", LycanitesAssetReloader.addGenericStackRenderer(new RenderHellfireCannon()));
    }

    private static void registerModel(Item item, String variant) {
        if(item.getRegistryName() != null)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    private static void registerTESR(Item item, String variant, TileEntityItemStackRenderer tesr) {
        item.setTileEntityItemStackRenderer(tesr);
        registerModel(item, variant);
    }
}