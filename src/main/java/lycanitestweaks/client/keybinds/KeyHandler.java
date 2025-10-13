package lycanitestweaks.client.keybinds;

import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketKeybindSoulgazerAutoNext;
import lycanitestweaks.network.PacketKeybindSoulgazerManualNext;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {

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

	public static void init() {
		ClientRegistry.registerKeyBinding(SPAWN_KEYBOUND_PET);
		ClientRegistry.registerKeyBinding(TELEPORT_KEYBOUND_PET);
		if(ForgeConfigHandler.integrationConfig.soulgazerBauble) {
			ClientRegistry.registerKeyBinding(TOGGLE_SOULGAZER_AUTO);
			ClientRegistry.registerKeyBinding(TOGGLE_SOULGAZER_MANUAL);
		}
	}

	@SubscribeEvent
	public static void onKeyPress(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if (player.getEntityWorld().isRemote){
				if(KeyHandler.SPAWN_KEYBOUND_PET.isPressed()){
					ILycanitesTweaksPlayerCapability lpt = LycanitesTweaksPlayerCapability.getForPlayer(player);
					if(lpt != null) lpt.setKeyboundPetSpawning();
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
			}
		}
	}
}
