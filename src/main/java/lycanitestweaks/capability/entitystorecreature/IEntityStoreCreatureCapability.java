package lycanitestweaks.capability.entitystorecreature;

import lycanitestweaks.storedcreatureentity.StoredCreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public interface IEntityStoreCreatureCapability {

    Entity getHost();
    void setHost(Entity entity);

    void clientRequestSync();
    void sync();

    StoredCreatureEntity getStoredCreatureEntity();
    void setStoredCreatureEntity(StoredCreatureEntity storedCreatureEntity);

    void readNBT(NBTTagCompound nbtTagCompound);
    void writeNBT(NBTTagCompound nbtTagCompound);

    }
