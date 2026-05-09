package lycanitestweaks.capability.playermoblevel;

import lycanitestweaks.LycanitesTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerMobLevelCapabilityHandler {

    public static final ResourceLocation PLAYER_MOB_LEVEL_KEY = new ResourceLocation(LycanitesTweaks.MODID, "moblevel");

    @CapabilityInject(PlayerMobLevelCapability.class)
    public static Capability<IPlayerMobLevelCapability> PLAYER_MOB_LEVEL;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(PlayerMobLevelCapability.class, new Storage(), PlayerMobLevelCapability::new);
    }

    public static class AttachCapabilityHandler {
        @SubscribeEvent
        public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof EntityPlayer) {
                event.addCapability(PLAYER_MOB_LEVEL_KEY, new PlayerMobLevelCapabilityHandler.Provider((EntityPlayer) event.getObject()));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final IPlayerMobLevelCapability instance;

        public Provider(EntityPlayer player) {
            this.instance = new PlayerMobLevelCapability(player);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == PLAYER_MOB_LEVEL;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == PLAYER_MOB_LEVEL ? PLAYER_MOB_LEVEL.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) PLAYER_MOB_LEVEL.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            PLAYER_MOB_LEVEL.readNBT(instance, null, nbt);
        }
    }

    private static class Storage implements Capability.IStorage<PlayerMobLevelCapability> {

        @Override
        public NBTBase writeNBT(Capability<PlayerMobLevelCapability> capability, PlayerMobLevelCapability instance, EnumFacing side) {
            if(instance == null) return null;

            NBTTagCompound extTagCompound = new NBTTagCompound();
            instance.writeNBT(extTagCompound);
            return extTagCompound;
        }

        @Override
        public void readNBT(Capability<PlayerMobLevelCapability> capability, PlayerMobLevelCapability instance, EnumFacing side, NBTBase nbt) {
            if((instance == null) || !(nbt instanceof NBTTagCompound)) return;

            instance.readNBT((NBTTagCompound)nbt);
        }
    }
    
    @SubscribeEvent
    public static void checkPlayerEquipmentLevels(LivingEquipmentChangeEvent event){
        IPlayerMobLevelCapability pml = event.getEntityLiving().getCapability(PlayerMobLevelCapabilityHandler.PLAYER_MOB_LEVEL, null);
        if(pml == null) {
            return;
        }

        if(event.getSlot() != EntityEquipmentSlot.MAINHAND){
            pml.setNonMainLevels(event.getTo(), event.getSlot().getSlotIndex());
        }
        else {
            pml.setMainHandLevels(event.getTo());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = event.player;
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
            if(pml != null) pml.updateTick();
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
        if(pml != null) pml.setDeathCooldown(6000);
    }

    // Relog can bypass death cooldown, but death cooldown is intended as beneficial handicap
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        IPlayerMobLevelCapability original = PlayerMobLevelCapability.getForPlayer(event.getOriginal());
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(event.getEntityPlayer());
        if(pml != null && original != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            original.writeNBT(nbt);
            pml.readNBT(nbt);

            pml.setDeathCooldown(original.getDeathCooldown());
        }
    }

    // ==================================================
    //                    Player Changed Dimension
    // ==================================================
    @SubscribeEvent
    public static void onPlayerChangedDimensionEvent(PlayerChangedDimensionEvent event) {
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(event.player);
        if(pml instanceof PlayerMobLevelCapability) ((PlayerMobLevelCapability) pml).needsFullSync = true;
    }


    // ==================================================
    //                    Player Respawn
    // ==================================================
    @SubscribeEvent
    public static void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(event.player);
        if(pml instanceof PlayerMobLevelCapability) ((PlayerMobLevelCapability) pml).needsFullSync = true;
    }
}
