package lycanitestweaks.entity.item;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.entitystorecreature.EntityStoreCreatureCapabilityHandler;
import lycanitestweaks.capability.entitystorecreature.IEntityStoreCreatureCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.storedcreatureentity.StoredCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class EntityEncounterSummonCrystal extends EntityBossSummonCrystal {

    public EntityEncounterSummonCrystal(World worldIn) {
        super(worldIn);
        this.setSearchDistance(128F);
        this.explosionStrength = 0F;
        this.setVariantType(-1);
    }

    // Reduced EnderCrystal
    @Override
    public void onUpdate(){
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.innerRotation;
        if(!this.world.isRemote && this.ticksExisted % 20 == 0){
            if(!(this.world.isAnyPlayerWithinRangeAt(this.posX, this.posY, this.posZ, this.getSearchDistance()))) this.setDead();
            if(ForgeConfigHandler.majorFeaturesConfig.escConfig.encounterCrystalDespawnChance != 0 && this.rand.nextInt(ForgeConfigHandler.majorFeaturesConfig.escConfig.encounterCrystalDespawnChance) == 0)
                this.setDead();
        }
    }

    public static boolean trySpawnEncounterCrystal(World world, EntityLiving entityLiving){
        if(entityLiving == null || world.isRemote) return false;
        if(world.countEntities(EntityEncounterSummonCrystal.class) > EnumCreatureType.MONSTER.getMaxNumberOfCreature()) return false;

        if(ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "Trying to Creating Encounter Crystal with: {}", entityLiving);

        return world.spawnEntity(storeCreatureEntity(world, entityLiving, entityLiving.getPosition()));
    }

    private static EntityEncounterSummonCrystal storeCreatureEntity(World world, EntityLiving entityLiving, BlockPos blockPos){
        EntityEncounterSummonCrystal crystal = new EntityEncounterSummonCrystal(world);
        crystal.setPosition(blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F);
        IEntityStoreCreatureCapability storeCreature = crystal.getCapability(EntityStoreCreatureCapabilityHandler.ENTITY_STORE_CREATURE, null);

        if(storeCreature != null) {
            if(entityLiving instanceof BaseCreatureEntity) {
                storeCreature.setStoredCreatureEntity(StoredCreatureEntity.createFromEntity(crystal, (BaseCreatureEntity) entityLiving)
                    .setPersistant(((BaseCreatureEntity) entityLiving).isPersistant())
                    .setSpawnAsBoss(true)
                    .setTemporary(((BaseCreatureEntity) entityLiving).temporaryDuration));
            }
            else storeCreature.setStoredCreatureEntity(StoredCreatureEntity.createFromEntity(crystal, entityLiving));
            crystal.setVariantType(-1);
        }
        return crystal;
    }
}
