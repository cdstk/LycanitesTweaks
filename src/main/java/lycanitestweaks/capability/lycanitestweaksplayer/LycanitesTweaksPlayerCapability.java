package lycanitestweaks.capability.lycanitestweaksplayer;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketKeybindsKeyboundPetEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.UUID;

public class LycanitesTweaksPlayerCapability implements ILycanitesTweaksPlayerCapability {

    // This kinda really only exists for keybind handling
    public boolean needsFullSync = true;

    private EntityPlayer player;
    private SOULGAZER_AUTO_ID soulgazerAuto = SOULGAZER_AUTO_ID.NONE;
    private boolean soulgazerManual = true;
    public PetEntry keyboundPetEntry;

    public enum SOULGAZER_AUTO_ID {
        NONE((byte)1), DAMAGE((byte)2), KILL((byte)3);
        public final byte id;
        SOULGAZER_AUTO_ID(byte i) { id = i; }
        public static SOULGAZER_AUTO_ID get(byte id) {
            return Arrays.stream(SOULGAZER_AUTO_ID.values())
                    .filter(toggleId -> toggleId.id == id)
                    .findFirst().orElse(NONE);
        }
    }

    LycanitesTweaksPlayerCapability(){}

    LycanitesTweaksPlayerCapability(@Nonnull EntityPlayer player){
        this.player = player;
    }

    public static ILycanitesTweaksPlayerCapability getForPlayer(EntityPlayer player) {
        if (player == null) {
            return null;
        }
        ILycanitesTweaksPlayerCapability ltp = player.getCapability(LycanitesTweaksPlayerCapabilityHandler.LT_PLAYER, null);
        if (ltp != null && ltp.getPlayer() != player) {
            ltp.setPlayer(player);
        }
        return ltp;
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void updateTick() {
        // Initial Network Sync:
        if(!this.player.getEntityWorld().isRemote && this.needsFullSync) {
            this.syncKeyboundPet();

            this.needsFullSync = false;
        }
    }

    @Override
    public void sync() {
        this.syncKeyboundPet();
    }

    // Used only by client using Vanilla Lycanites packets
    @Override
    public void setKeyboundPet(PetEntry petEntry) {
        this.keyboundPetEntry = petEntry;
        this.sync();
    }

    // Used only by client using Vanilla Lycanites packets
    @Override
    public void setKeyboundPetSpawning() {
        if(this.player.getEntityWorld().isRemote) {
            if(this.keyboundPetEntry != null && this.keyboundPetEntry.active) {
                if(this.player.isRiding() && this.player.getRidingEntity() == this.keyboundPetEntry.entity) return;

                this.keyboundPetEntry.setSpawningActive(!this.keyboundPetEntry.spawningActive);

                if(this.keyboundPetEntry.isRespawning) {
                    this.player.sendStatusMessage(new TextComponentTranslation(
                                    "message.keybound.active.respawning",
                                    this.keyboundPetEntry.respawnTime / 20),
                            true);
                }
                else {
                    if (this.keyboundPetEntry.spawningActive) {
                        this.player.sendStatusMessage(new TextComponentTranslation(
                                "message.keybound.active.spawning"),
                                true);
                    }
                    else {
                        this.player.sendStatusMessage(new TextComponentTranslation(
                                "message.keybound.active.nospawning"),
                                true);
                    }
                }

                ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
                extendedPlayer.sendPetEntryToServer(this.keyboundPetEntry);
            }
            else {
                this.player.sendStatusMessage(new TextComponentTranslation("message.keybound.active.none"), true);
            }
        }
    }

    @Override
    public void setKeyboundPetTeleport() {
        if(this.player.getEntityWorld().isRemote) {
            if(this.keyboundPetEntry != null) {
                if(this.keyboundPetEntry.entity == null || !this.keyboundPetEntry.entity.isEntityAlive()) {
                    this.player.sendStatusMessage(new TextComponentTranslation(
                            "message.keybound.teleport.none"),
                            true);
                }
                this.keyboundPetEntry.teleportEntity = true;
                ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
                extendedPlayer.sendPetEntryToServer(this.keyboundPetEntry);
            }
            else {
                this.player.sendStatusMessage(new TextComponentTranslation("message.keybound.active.none"), true);
            }
        }
    }

    @Override
    public UUID getKeyboundPetID() {
        if(this.keyboundPetEntry != null) return this.keyboundPetEntry.petEntryID;
        return null;
    }

    private void syncKeyboundPet(){
        PacketKeybindsKeyboundPetEntry keyboundPetEntry = new PacketKeybindsKeyboundPetEntry(this);
        if(this.player.getEntityWorld().isRemote) {
            PacketHandler.instance.sendToServer(keyboundPetEntry);
        }
        else {
            EntityPlayerMP playerMP = (EntityPlayerMP) this.player;
            PacketHandler.instance.sendTo(keyboundPetEntry, playerMP);
        }
    }
}
