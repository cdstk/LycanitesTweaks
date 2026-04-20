package lycanitestweaks.handlers;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.client.keybinds.KeyHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
        registerModel(LycanitesTweaksRegistry.chargestaff, "inventory");
        registerModel(LycanitesTweaksRegistry.devilgatlinggun, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkey, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkeyDiamond, "inventory");
        registerModel(LycanitesTweaksRegistry.enchantedSoulkeyEmerald, "inventory");

        registerModel(LycanitesTweaksRegistry.vileMatter, "inventory");
        registerModel(LycanitesTweaksRegistry.fantasticalFeast, "inventory");
    }

    private static void registerModel(Item item, String variant) {
        if(item.getRegistryName() != null)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}