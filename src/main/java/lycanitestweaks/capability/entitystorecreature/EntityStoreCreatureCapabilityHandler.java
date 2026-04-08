package lycanitestweaks.capability.entitystorecreature;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityStoreCreatureCapabilityHandler {

    public static final ResourceLocation ENTITY_STORE_CREATURE_KEY = new ResourceLocation(LycanitesTweaks.MODID, "entitystorecreature");

    @CapabilityInject(EntityStoreCreatureCapability.class)
    public static Capability<IEntityStoreCreatureCapability> ENTITY_STORE_CREATURE;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(EntityStoreCreatureCapability.class, new Storage(), EntityStoreCreatureCapability::new);
    }

    public static class AttachCapabilityHandler {
        @SubscribeEvent
        public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof EntityBossSummonCrystal) {
                event.addCapability(ENTITY_STORE_CREATURE_KEY, new EntityStoreCreatureCapabilityHandler.Provider(event.getObject()));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final IEntityStoreCreatureCapability instance;

        public Provider(Entity entity) {
            this.instance = new EntityStoreCreatureCapability(entity);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == ENTITY_STORE_CREATURE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == ENTITY_STORE_CREATURE ? ENTITY_STORE_CREATURE.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) ENTITY_STORE_CREATURE.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            ENTITY_STORE_CREATURE.readNBT(instance, null, nbt);
        }
    }

    private static class Storage implements Capability.IStorage<EntityStoreCreatureCapability> {

        @Override
        public NBTBase writeNBT(Capability<EntityStoreCreatureCapability> capability, EntityStoreCreatureCapability instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();

            instance.writeNBT(nbt);

            return nbt;
        }

        @Override
        public void readNBT(Capability<EntityStoreCreatureCapability> capability, EntityStoreCreatureCapability instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;

            instance.readNBT(tags);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(!event.getWorld().isRemote) return;
        if(event.getEntity() == null) return;

        IEntityStoreCreatureCapability storeCreatureCapability = event.getEntity().getCapability(ENTITY_STORE_CREATURE, null);
        if(storeCreatureCapability != null) {
            storeCreatureCapability.clientRequestSync();
        }
    }
}
