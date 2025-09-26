package lycanitestweaks.client.keybinds;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {

	public static KeyBinding MOUNT_CHANGE_VIEW = new KeyBinding("key.mount_change_view.lycanitestweaks",
			 KeyConflictContext.IN_GAME,
			 KeyModifier.SHIFT,
			 Keyboard.KEY_F5,
			"key.categories.misc.lycanitestweaks");

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

	public static void init() {
		ClientRegistry.registerKeyBinding(MOUNT_CHANGE_VIEW);
		ClientRegistry.registerKeyBinding(SPAWN_KEYBOUND_PET);
		ClientRegistry.registerKeyBinding(TELEPORT_KEYBOUND_PET);
	}

	// ty Ice and Fire
	@SubscribeEvent
	public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player.getRidingEntity() != null) {
			if(player.getRidingEntity() instanceof RideableCreatureEntity){
				int currentView = LycanitesTweaks.PROXY.getMount3rdPersonView();
				float scale = Math.max(1, player.getRidingEntity().width / 3F);
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
					if (currentView == 1) GlStateManager.translate(scale * 0.5F, 0F, -scale * 3F);
					else if (currentView == 2) GlStateManager.translate(0, 0F, -scale * 3F);
					else if (currentView == 3) GlStateManager.translate(scale * 0.5F, 0F, -scale * 0.5F);
				}
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
					if (currentView == 1) GlStateManager.translate(-scale  * 1.2F, 0F, 5);
					else if(currentView == 2) GlStateManager.translate(scale  * 1.2F, 0F, 5);
					else if(currentView == 3) GlStateManager.translate(0, 0F, scale * 3F);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onKeyPress(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntityLiving();
			if (player.getEntityWorld().isRemote){
				if (KeyHandler.MOUNT_CHANGE_VIEW.isPressed()) {
					int currentView = LycanitesTweaks.PROXY.getMount3rdPersonView();
					if (currentView + 1 > 3) currentView = 0;
					else currentView++;
					LycanitesTweaks.PROXY.setMount3rdPersonView(currentView);
				}
				if(KeyHandler.SPAWN_KEYBOUND_PET.isPressed()){
					ILycanitesTweaksPlayerCapability lpt = LycanitesTweaksPlayerCapability.getForPlayer(player);
					if(lpt != null) lpt.setKeyboundPetSpawning();
				}
				if(KeyHandler.TELEPORT_KEYBOUND_PET.isPressed()){
					ILycanitesTweaksPlayerCapability lpt = LycanitesTweaksPlayerCapability.getForPlayer(player);
					if(lpt != null) lpt.setKeyboundPetTeleport();
				}
			}
		}
	}
}
