package lycanitestweaks.capability.entitystorecreature;


import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketStoredCreatureSync;
import lycanitestweaks.storedcreatureentity.StoredCreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class EntityStoreCreatureCapability implements IEntityStoreCreatureCapability {

    // Based on ExtendedPlayer's usage of PetManager

    public static final String TAG_NAME = "StoredCreature";

    Entity host;
    StoredCreatureEntity storedCreatureEntity;

    public EntityStoreCreatureCapability() {}

    public EntityStoreCreatureCapability(Entity host){
        this.host = host;
        this.storedCreatureEntity = new StoredCreatureEntity(host, "");
    }

    @Override
    public Entity getHost() {
        return this.host;
    }

    @Override
    public void setHost(Entity entity) {
        this.host = entity;
        this.storedCreatureEntity.host = entity;
    }

    @Override
    public void clientRequestSync() {
        if(this.host.getEntityWorld().isRemote) {
            PacketHandler.instance.sendToServer(new PacketStoredCreatureSync(this));
        }
    }

    @Override
    public void sync() {
        if(!this.host.getEntityWorld().isRemote) {
            PacketHandler.instance.sendToAllTracking(new PacketStoredCreatureSync(this), this.host);
        }
    }

    @Override
    public StoredCreatureEntity getStoredCreatureEntity() {
        return this.storedCreatureEntity;
    }

    @Override
    public void setStoredCreatureEntity(StoredCreatureEntity storedCreatureEntity) {
        this.storedCreatureEntity = storedCreatureEntity;
        this.sync();
    }

    @Override
    public void readNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = nbtTagCompound.getCompoundTag(TAG_NAME);

        this.storedCreatureEntity.readFromNBT(extTagCompound);
        this.sync();
    }

    @Override
    public void writeNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound extTagCompound = new NBTTagCompound();

        this.storedCreatureEntity.writeToNBT(extTagCompound);

        nbtTagCompound.setTag(TAG_NAME, extTagCompound);
        this.sync();
    }
}
