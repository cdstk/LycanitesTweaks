package lycanitestweaks.capability.lycanitestweaksplayer;

import com.lycanitesmobs.ExtendedWorld;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LycanitesTweaksPlayerCapabilityHandler {

    public static final ResourceLocation LT_PLAYER_KEY = new ResourceLocation(LycanitesTweaks.MODID, "player");

    @CapabilityInject(LycanitesTweaksPlayerCapability.class)
    public static Capability<ILycanitesTweaksPlayerCapability> LT_PLAYER;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(LycanitesTweaksPlayerCapability.class, new Storage(), LycanitesTweaksPlayerCapability::new);
    }

    public static class AttachCapabilityHandler {
        @SubscribeEvent()
        public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof EntityPlayer) {
                event.addCapability(LT_PLAYER_KEY, new LycanitesTweaksPlayerCapabilityHandler.Provider((EntityPlayer)event.getObject()));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final ILycanitesTweaksPlayerCapability instance;

        public Provider(EntityPlayer player) {
            this.instance = new LycanitesTweaksPlayerCapability(player);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == LT_PLAYER;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == LT_PLAYER ? LT_PLAYER.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) LT_PLAYER.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            LT_PLAYER.readNBT(instance, null, nbt);
        }
    }

    private static class Storage implements Capability.IStorage<LycanitesTweaksPlayerCapability> {

        @Override
        public NBTBase writeNBT(Capability<LycanitesTweaksPlayerCapability> capability, LycanitesTweaksPlayerCapability instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();

            instance.writeNBT(nbt);

            return nbt;
        }

        @Override
        public void readNBT(Capability<LycanitesTweaksPlayerCapability> capability, LycanitesTweaksPlayerCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;

            instance.readNBT(tags);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
            ltp.updateTick();
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ILycanitesTweaksPlayerCapability original = LycanitesTweaksPlayerCapability.getForPlayer(event.getOriginal());
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.getEntityPlayer());

        if(original instanceof LycanitesTweaksPlayerCapability && ltp instanceof LycanitesTweaksPlayerCapability) {
            ((LycanitesTweaksPlayerCapability) ltp).keyboundPetEntry = ((LycanitesTweaksPlayerCapability) original).keyboundPetEntry;
            ltp.setSoulgazerAutoToggle(original.getSoulgazerAutoToggle());
            ltp.setSoulgazerManualToggle(original.getSoulgazerManualToggle());
        }
    }

    // ==================================================
    //                    Player Changed Dimension
    // ==================================================
    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.player);
        if(ltp != null){
            ltp.setSavedMobEvent("", 0);
            ltp.scheduleFullSync();
        }
    }


    // ==================================================
    //                    Player Respawn
    // ==================================================
    @SubscribeEvent
    public static void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.player);
        if(ltp != null){
            ltp.scheduleFullSync();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerLoggedOutEvent event){
        World world = event.player.getEntityWorld();
        if(LycanitesTweaks.PROXY.isSinglePlayer()) return;
        if(world.isRemote) return;

        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(event.player);
        ExtendedWorld extendedWorld = ExtendedWorld.getForWorld(world);
        if(ltp != null && extendedWorld != null){
            // Server Side:
            if(extendedWorld.serverWorldEventPlayer != null && extendedWorld.serverWorldEventPlayer.mobEvent != null) {
                ltp.setSavedMobEvent(
                        extendedWorld.serverWorldEventPlayer.mobEvent.name,
                        (int) ((extendedWorld.serverWorldEventPlayer.mobEvent.duration - extendedWorld.serverWorldEventPlayer.ticks) * ForgeConfigHandler.minorFeaturesConfig.savedMobEventDurationAmount)
                );
            }
        }
    }
}
