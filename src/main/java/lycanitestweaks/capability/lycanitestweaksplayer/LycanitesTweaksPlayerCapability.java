package lycanitestweaks.capability.lycanitestweaksplayer;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.mobevent.MobEvent;
import com.lycanitesmobs.core.mobevent.MobEventManager;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketKeybindsKeyboundPetEntry;
import lycanitestweaks.network.PacketKeybindsSoulgazerToggle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.UUID;

public class LycanitesTweaksPlayerCapability implements ILycanitesTweaksPlayerCapability {

    // This kinda really only exists for keybind handling
    private boolean needsFullSync = true;

    private EntityPlayer player;
    private SOULGAZER_AUTO_ID soulgazerAuto = SOULGAZER_AUTO_ID.NONE;
    private boolean soulgazerManual = true;
    public PetEntry keyboundPetEntry;
    private UUID keyboundPetEntryUUID;
    private String savedMobEventName = "";
    private int savedMobEventDuration = 0;

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
        if(!this.player.getEntityWorld().isRemote){

            if(!this.savedMobEventName.isEmpty()) {
                if(this.savedMobEventDuration-- <= 0){
                    MobEvent mobEvent = MobEventManager.getInstance().getMobEvent(this.savedMobEventName);
                    if(mobEvent != null){
                        this.player.sendMessage(new TextComponentTranslation("savedevent.finished", mobEvent.getTitle()));
                    }
                    this.savedMobEventName = "";
                    this.savedMobEventDuration = 0;
                }
            }

            if(this.needsFullSync){
                if(!this.savedMobEventName.isEmpty()) {
                    MobEvent mobEvent = MobEventManager.getInstance().getMobEvent(this.savedMobEventName);
                    if(mobEvent != null){
                        this.player.sendMessage(new TextComponentTranslation("savedevent.started", mobEvent.getTitle(), this.savedMobEventDuration / 20));
                    }
                }
                if(this.keyboundPetEntryUUID != null){
                    ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(this.player);
                    if(extendedPlayer != null){
                        this.keyboundPetEntry =  extendedPlayer.petManager.getEntry(this.keyboundPetEntryUUID);
                    }
                }
                this.sync();
                this.needsFullSync = false;
            }
            else {
                this.keyboundPetEntryUUID = null;
            }
        }
    }

    @Override
    public void sync() {
        this.syncKeyboundPet();
        this.syncSoulgazerToggle();
    }

    @Override
    public void scheduleFullSync() {
        this.needsFullSync = true;
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


    @Override
    public byte getSoulgazerAutoToggle() {
        return this.soulgazerAuto.id;
    }

    @Override
    public void setSoulgazerAutoToggle(byte id) {
        this.soulgazerAuto = SOULGAZER_AUTO_ID.get(id);
    }

    @Override
    public void nextSoulgazerAutoToggle() {
        this.setSoulgazerAutoToggle((byte) (this.soulgazerAuto.id + 1));
        if(!this.player.getEntityWorld().isRemote) {
            this.player.sendStatusMessage(new TextComponentTranslation("item.soulgazer.description.keybind.auto." + this.soulgazerAuto.id), true);
            this.sync();
        }
    }

    @Override
    public boolean getSoulgazerManualToggle() {
        return this.soulgazerManual;
    }

    @Override
    public void setSoulgazerManualToggle(boolean toggle) {
        this.soulgazerManual = toggle;
    }

    @Override
    public void nextSoulgazerManualToggle() {
        this.setSoulgazerManualToggle(!this.soulgazerManual);
        if(!this.player.getEntityWorld().isRemote) {
            int manualID = this.getSoulgazerManualToggle() ? 1 : 2;
            this.player.sendStatusMessage(new TextComponentTranslation("item.soulgazer.description.keybind.manual." + manualID), true);
            this.sync();
        }
    }

    @Override
    public void setSavedMobEvent(String eventName, int duration) {
        this.savedMobEventName = eventName;
        this.savedMobEventDuration = duration;
    }

    @Override
    public boolean hasSavedMobEvent(String eventName) {
        return this.savedMobEventName.equals(eventName);
    }

    @Override
    public int getRemainingEventDuration(String eventName) {
        return this.savedMobEventName.equals(eventName) ? this.savedMobEventDuration : 0;
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

    private void syncSoulgazerToggle(){
        PacketKeybindsSoulgazerToggle soulgazerToggle = new PacketKeybindsSoulgazerToggle(this);
        if(this.player.getEntityWorld().isRemote) {
            PacketHandler.instance.sendToServer(soulgazerToggle);
        }
        else {
            EntityPlayerMP playerMP = (EntityPlayerMP) this.player;
            PacketHandler.instance.sendTo(soulgazerToggle, playerMP);
        }
    }

    @Override
    public void readNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = nbtTagCompound.getCompoundTag("LycanitesTweaksPlayer");

        if(extTagCompound.hasKey("AutoSoulgazer"))
            this.soulgazerAuto = SOULGAZER_AUTO_ID.get(extTagCompound.getByte("AutoSoulgazer"));
        if(extTagCompound.hasKey("ManualSoulgazer"))
            this.soulgazerManual = extTagCompound.getBoolean("ManualSoulgazer");
        if(extTagCompound.hasUniqueId("KeyboundEntryUUID")){
            this.keyboundPetEntryUUID = extTagCompound.getUniqueId("KeyboundEntryUUID");
        }
        if(extTagCompound.hasKey("SavedMobEventName"))
            this.savedMobEventName = extTagCompound.getString("SavedMobEventName");
        if(extTagCompound.hasKey("SavedMobEventDuration"))
            this.savedMobEventDuration = extTagCompound.getInteger("SavedMobEventDuration");
    }

    @Override
    public void writeNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = new NBTTagCompound();

        extTagCompound.setByte("AutoSoulgazer", this.getSoulgazerAutoToggle());
        extTagCompound.setBoolean("ManualSoulgazer", this.getSoulgazerManualToggle());
        if(this.getKeyboundPetID() != null)
            extTagCompound.setUniqueId("KeyboundEntryUUID", this.getKeyboundPetID());
        extTagCompound.setString("SavedMobEventName", this.savedMobEventName);
        extTagCompound.setInteger("SavedMobEventDuration", this.savedMobEventDuration);

        nbtTagCompound.setTag("LycanitesTweaksPlayer", extTagCompound);
    }
}
