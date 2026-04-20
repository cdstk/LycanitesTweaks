package lycanitestweaks.client.keybinds;

import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketKeybindSoulgazerAutoNext;
import lycanitestweaks.network.PacketKeybindSoulgazerManualNext;
import lycanitestweaks.network.PacketToggleableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {

	private static final List<BaubleKeyBinding> BAUBLE_KEY_BINDINGS = new ArrayList<>();

	public static KeyBinding SPAWN_KEYBOUND_PET = new KeyBinding("key.spawn_keybound_pet.lycanitestweaks",
			KeyConflictContext.IN_GAME,
			KeyModifier.SHIFT,
			Keyboard.KEY_B,
			"key.categories.misc.lycanitestweaks");

	public static KeyBinding TELEPORT_KEYBOUND_PET = new KeyBinding("key.teleport_keybound_pet.lycanitestweaks",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			-99, // Right Mouse
			"key.categories.misc.lycanitestweaks");

	public static KeyBinding TOGGLE_SOULGAZER_AUTO = new KeyBinding("key.toggle_soulgazer_auto.lycanitestweaks",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			Keyboard.KEY_G,
			"key.categories.misc.lycanitestweaks");

	public static KeyBinding TOGGLE_SOULGAZER_MANUAL = new KeyBinding("key.toggle_soulgazer_manual.lycanitestweaks",
			KeyConflictContext.IN_GAME,
			KeyModifier.CONTROL,
			Keyboard.KEY_H,
			"key.categories.misc.lycanitestweaks");

	public static KeyBinding TOGGLE_AMULET_BAUBLE = createKeyBinding("toggleAmuletBauble", Keyboard.KEY_NONE, 0);
	public static KeyBinding TOGGLE_RING1_BAUBLE = createKeyBinding("toggleRing1Bauble", Keyboard.KEY_NONE, 1);
	public static KeyBinding TOGGLE_RING2_BAUBLE = createKeyBinding("toggleRing2Bauble", Keyboard.KEY_NONE, 2);
	public static KeyBinding TOGGLE_BELT_BAUBLE = createKeyBinding("toggleBeltBauble", Keyboard.KEY_NONE, 3);
	public static KeyBinding TOGGLE_HEAD_BAUBLE = createKeyBinding("toggleHeadBauble", Keyboard.KEY_NONE, 4);
	public static KeyBinding TOGGLE_BODY_BAUBLE = createKeyBinding("toggleBodyBauble", Keyboard.KEY_NONE, 5);
	public static KeyBinding TOGGLE_CHARM_BAUBLE = createKeyBinding("toggleCharmBauble", Keyboard.KEY_NONE, 6);
	public static KeyBinding TOGGLE_ALL_BAUBLES = createKeyBinding("toggleAllBaubles", Keyboard.KEY_DECIMAL, -1);

	public static void init() {
		ClientRegistry.registerKeyBinding(SPAWN_KEYBOUND_PET);
		ClientRegistry.registerKeyBinding(TELEPORT_KEYBOUND_PET);
		if(ForgeConfigHandler.integrationConfig.soulgazerBauble) {
			ClientRegistry.registerKeyBinding(TOGGLE_SOULGAZER_AUTO);
			ClientRegistry.registerKeyBinding(TOGGLE_SOULGAZER_MANUAL);
		}

		if(ModLoadedUtil.baubles.isLoaded()) {
			BAUBLE_KEY_BINDINGS.forEach(ClientRegistry::registerKeyBinding);
		}
	}

	@SubscribeEvent
	public static void onGameplayKeyPress(InputEvent.KeyInputEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player == null) return;
        if (KeyHandler.SPAWN_KEYBOUND_PET.isPressed()) {
            ILycanitesTweaksPlayerCapability lpt = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if (lpt != null) lpt.setKeyboundPetSpawning();
        }
        if(KeyHandler.TELEPORT_KEYBOUND_PET.isPressed()){
            ILycanitesTweaksPlayerCapability lpt = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if(lpt != null) lpt.setKeyboundPetTeleport();
        }
        if(KeyHandler.TOGGLE_SOULGAZER_AUTO.isPressed()) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if (ltp != null) PacketHandler.instance.sendToServer(new PacketKeybindSoulgazerAutoNext());
        }
        if(KeyHandler.TOGGLE_SOULGAZER_MANUAL.isPressed()) {
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            if (ltp != null) PacketHandler.instance.sendToServer(new PacketKeybindSoulgazerManualNext());
        }
		BAUBLE_KEY_BINDINGS.forEach(baubleKeyBinding -> {
			if(baubleKeyBinding.isPressed() && ModLoadedUtil.baubles.isLoaded()) {
				PacketHandler.instance.sendToServer(new PacketToggleableItem(true, baubleKeyBinding.slot));
			}
		});
    }

	private static BaubleKeyBinding createKeyBinding(String description, int keyCode, int slot) {
		BaubleKeyBinding baubleKey = new BaubleKeyBinding(description, keyCode, slot);
        BAUBLE_KEY_BINDINGS.add(baubleKey);
		return baubleKey;
	}

	private static class BaubleKeyBinding extends KeyBinding {

		public int slot;

		public BaubleKeyBinding(String description, int keyCode, int slot) {
			super("key.lycanitestweaks." + description,
					KeyConflictContext.IN_GAME,
					KeyModifier.NONE,
					keyCode,
					"key.categories.misc.lycanitestweaks"
			);
			this.slot = slot;
		}
	}
}
