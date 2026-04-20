package lycanitestweaks.capability.toggleableitem;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.item.base.ItemPassive;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToggleableItemHandler {

    public static final ResourceLocation TOGGLE_ITEM_KEY = new ResourceLocation(LycanitesTweaks.MODID, "toggleableitem");

    @CapabilityInject(ToggleableItem.class)
    public static Capability<ToggleableItem> TOGGLE_ITEM;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ToggleableItem.class, new Storage(), ToggleableItem::new);
    }

    public static class AttachCapabilityHandler {
        @SubscribeEvent()
        public static void onAttachPlayerCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
            if(!LycanitesTweaks.completedLoading) return;

            if(event.getObject().hasCapability(TOGGLE_ITEM, null)) return;

            Item item = event.getObject().getItem();
            if(!(item instanceof ItemPassive)) return;
            if(!((ItemPassive) item).isToggleable()) return;

            event.addCapability(TOGGLE_ITEM_KEY, new Provider(event.getObject()));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final ToggleableItem instance;

        public Provider(ItemStack itemStack) {
            this.instance = new ToggleableItem(itemStack);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == TOGGLE_ITEM;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == TOGGLE_ITEM ? TOGGLE_ITEM.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) TOGGLE_ITEM.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            TOGGLE_ITEM.readNBT(instance, null, nbt);
        }
    }

    private static class Storage implements Capability.IStorage<ToggleableItem> {

        @Override
        public NBTBase writeNBT(Capability<ToggleableItem> capability, ToggleableItem instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();

            instance.writeNBT(nbt);

            return nbt;
        }

        @Override
        public void readNBT(Capability<ToggleableItem> capability, ToggleableItem instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;

            instance.readNBT(tags);
        }
    }
}
