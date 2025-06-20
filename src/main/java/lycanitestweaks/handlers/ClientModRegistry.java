package lycanitestweaks.handlers;

import lycanitestweaks.LycanitesTweaks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID, value = Side.CLIENT)
public class ClientModRegistry {

    public static KeyBinding mount_change_view;

    public static void init() {
        ClientModRegistry.mount_change_view = new KeyBinding("key.mount_change_view.lycanitestweaks",
                KeyConflictContext.IN_GAME,
                KeyModifier.SHIFT,
                Keyboard.KEY_F5,
                "key.categories.misc.lycanitestweaks");
        ClientRegistry.registerKeyBinding(ClientModRegistry.mount_change_view);
    }

    @SubscribeEvent
    public static void registerModelEvent(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkey, 0, new ModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkey.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkeyDiamond, 0, new ModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkeyDiamond.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkeyEmerald, 0, new ModelResourceLocation(LycanitesTweaksRegistry.enchantedSoulkeyEmerald.getRegistryName(), "inventory"));
    }
}