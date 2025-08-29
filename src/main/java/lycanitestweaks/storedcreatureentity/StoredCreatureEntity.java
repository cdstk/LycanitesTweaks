package lycanitestweaks.storedcreatureentity;

import com.lycanitesmobs.ExtendedWorld;
import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ItemDrop;
import com.lycanitesmobs.core.info.Subspecies;
import com.lycanitesmobs.core.info.Variant;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.RLTweakerHandler;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class StoredCreatureEntity {

    // Based on LycanitesMobs PetEntry, does not write all nbt to disk, meant to be simple to spawn with levels and variant

    // Either BaseCreatureEntity typename or ResourceLocation resourceName
    // Can be null but should be set at some point
    public String creatureTypeName;

    /** The entity that this entry belongs to. **/
    public Entity host;
    /** The current entity instance that this entry is using. **/
    public Entity entity;
    /** The current entity NBT data. **/
    public NBTTagCompound entityNBT;
    /** Entity Level **/
    public int entityLevel = 1;
    /** Entity Experience **/
    public int entityExperience = 0;

    /** The name to use for the entity. Leave empty/null "" for no name. **/
    public String entityName = "";
    /** The Subspecies to use for the entity. **/
    public int subspeciesIndex = 0;
    /** The Variant to use for the entity. **/
    public int variantIndex = 0;
    /** The size scale to use for the entity. **/
    public double entitySize = 1.0D;

    // Dungeon MobSpawn data
    /** Whether the spawned mob will be persistent and wont naturally despawn. Can be: default (SpawnInfo), true or false. **/
    public boolean isPersistant = false;
    /** If set, the spawned mob will be set as temporary where it will forcefully despawn after the specified time (in ticks). Useful for Mob Events. **/
    public int temporary = 0;
    /** If true, the spawned mob will fixate on the player that triggered the spawn, always attacking that player. **/
    public boolean fixate = false;
    /** If set, the spawned mob will set the location it spawned at as a home position and will stay within this distance (in blocks) of that position. **/
    public double home = -1;
    /** If true, this mob is to be treated like a boss taking less damage from other mobs, having a damage taken cap, etc. This does not show the boss health bar. Default: false. **/
    public boolean spawnAsBoss = false;
    /** A list of item drops to add to a mob spawned by this MobSpawn. **/
    public List<ItemDrop> mobDrops = new ArrayList<>();
    /** For boss spawning, if above 0, the spawned mob will protect blocks within this range. **/
    public int blockProtection = 0;

    public NBTTagCompound extraMobBehaviourNBT = null;

    // ==================================================
    //                 Create from Entity
    // ==================================================
    /** Returns a new StoredCreatureEntity based off the provided creature and host. **/
    public static StoredCreatureEntity createFromEntity(Entity host, BaseCreatureEntity entity) {
        CreatureInfo creatureInfo = entity.creatureInfo;
        StoredCreatureEntity storedCreatureEntity = new StoredCreatureEntity(host, creatureInfo.getName());
        if (entity.hasCustomName()) {
            storedCreatureEntity.setEntityName(entity.getCustomNameTag());
        }
        storedCreatureEntity.setEntitySubspecies(entity.getSubspeciesIndex());
        storedCreatureEntity.setEntityVariant(entity.getVariantIndex());
        storedCreatureEntity.setEntitySize(entity.sizeScale);

        storedCreatureEntity.setLevel(entity.getLevel());
        storedCreatureEntity.setExperience(entity.getExperience());

        // Set these manually, these are the Dungeon Config values
        /*
        storedCreatureEntity.setPersistant(entity.isPersistant())
                .setFixate(entity.hasFixateTarget())
                .setHome(entity.getHomeDistanceMax())
                .setSpawnAsBoss(entity.spawnedAsBoss)
                .setTemporary(entity.temporaryDuration)
                .setMobDropsList(entity.savedDrops)
                .setBlockProtection(entity.spawnedWithBlockProtection);
        */

        storedCreatureEntity.saveEntityNBT(); // Appears necessary so first spawn levels are used
        return storedCreatureEntity;
    }

    /** Returns a new StoredCreatureEntity based off the provided entity and host. **/
    public static StoredCreatureEntity createFromEntity(Entity host, EntityLiving entity) {
        return new StoredCreatureEntity(host, EntityList.getKey(entity).toString());
    }

    // ==================================================
    //                     Constructor
    // ==================================================
	public StoredCreatureEntity(Entity host, String creatureTypeName) {
        this.host = host;
        this.setCreatureTypeName(creatureTypeName);
	}

    public StoredCreatureEntity setCreatureTypeName(String type){
        this.creatureTypeName = type;
        return this;
    }

    // ==================================================
    //                      Entry
    // ==================================================
    public StoredCreatureEntity setEntityName(String name) {
        this.entityName = name;
        return this;
    }

    public StoredCreatureEntity setEntitySubspecies(int index) {
        this.subspeciesIndex = index;
        return this;
    }

    public StoredCreatureEntity setEntityVariant(int index) {
        this.variantIndex = index;
        return this;
    }

    public StoredCreatureEntity setEntitySize(double size) {
        this.entitySize = size;
        return this;
    }

    public void setLevel(int level) {
        if (this.entity != null && this.entity instanceof BaseCreatureEntity)
            ((BaseCreatureEntity)this.entity).setLevel(level);
        this.entityLevel = level;
    }

    public int getLevel() {
        if (this.entity != null && this.entity instanceof BaseCreatureEntity)
            this.entityLevel = ((BaseCreatureEntity)this.entity).getLevel();
        return this.entityLevel;
    }

    public int getExperience() {
        if (this.entity != null && this.entity instanceof BaseCreatureEntity)
            this.entityExperience = ((BaseCreatureEntity)this.entity).getExperience();
        return this.entityExperience;
    }

    public void setExperience(int experience) {
        if (this.entity != null && this.entity instanceof BaseCreatureEntity)
            ((BaseCreatureEntity)this.entity).setExperience(experience);
        this.entityExperience = experience;
    }

    public CreatureInfo getCreatureInfo() {
        if (this.creatureTypeName == null || this.creatureTypeName.isEmpty())
            return null;
        return CreatureManager.getInstance().getCreature(this.creatureTypeName);
    }

    // ==================================================
    //                      Dungeon
    // ==================================================
    public StoredCreatureEntity setPersistant(boolean isPersistant){
        this.isPersistant = isPersistant;
        return this;
    }

    public StoredCreatureEntity setTemporary(int temporary){
        this.temporary = temporary;
        return this;
    }

    public StoredCreatureEntity setFixate(boolean fixate){
        this.fixate = fixate;
        return this;
    }

    public StoredCreatureEntity setHome(double home){
        this.home = home;
        return this;
    }

    public StoredCreatureEntity setSpawnAsBoss(boolean spawnAsBoss){
        this.spawnAsBoss = spawnAsBoss;
        return this;
    }

    public StoredCreatureEntity setMobDropsList(List<ItemDrop> mobDrops){
        this.mobDrops = mobDrops;
        return this;
    }

    public StoredCreatureEntity setBlockProtection(int blockProtection){
        this.blockProtection = blockProtection;

        if(ModLoadedUtil.isRLTweakerLoaded() && this.blockProtection > 0){
            RLTweakerHandler.setRLTweakerBossRange(host, this.blockProtection);
        }

        return this;
    }

    public StoredCreatureEntity setExtraMobBehaviour(NBTTagCompound nbt){
        this.extraMobBehaviourNBT = nbt;
        return this;
    }


    // ==================================================
    //                     Copy Entry
    // ==================================================
    /** Makes this entry copy all information from another entry, useful for updating entries. **/
    public void copy(PetEntry copyEntry) {
        this.setEntityName(copyEntry.entityName);
        this.setEntitySubspecies(copyEntry.subspeciesIndex);
        this.setEntityVariant(copyEntry.variantIndex);
        this.setEntitySize(copyEntry.entitySize);
        if (copyEntry.summonSet != null)
            this.creatureTypeName = copyEntry.summonSet.summonType;
    }

    public void copy(StoredCreatureEntity copyEntry) {
        this.setEntityName(copyEntry.entityName);
        this.setEntitySubspecies(copyEntry.subspeciesIndex);
        this.setEntityVariant(copyEntry.variantIndex);
        this.setEntitySize(copyEntry.entitySize);
        this.creatureTypeName = copyEntry.creatureTypeName;

        this.setPersistant(copyEntry.isPersistant)
                .setFixate(copyEntry.fixate)
                .setHome(copyEntry.home)
                .setSpawnAsBoss(copyEntry.spawnAsBoss)
                .setTemporary(copyEntry.temporary)
                .setMobDropsList(copyEntry.mobDrops)
                .setBlockProtection(copyEntry.blockProtection);
    }


    // ==================================================
    //                       Name
    // ==================================================
    public String getDisplayName() {
        String displayName;
        if(this.getCreatureInfo() == null) {
            displayName = this.creatureTypeName;
        }
        else {
            displayName = this.getCreatureInfo().getTitle();
        }
        if (this.entityName != null && !this.entityName.isEmpty()) {
            displayName = this.entityName + " (" + displayName + ")";
        }
        return displayName;
    }


    // ==================================================
    //                    Spawn Entity
    // ==================================================
    /** Spawns and sets this entry's entity if it isn't active already. **/
    public void spawnEntity(EntityLivingBase target) {
        if (this.entity != null || this.host == null || this.creatureTypeName.isEmpty())
            return;
        try {
            if(CreatureManager.getInstance().getCreature(this.creatureTypeName) == null){
                this.entity = EntityList.createEntityByIDFromName(new ResourceLocation(this.creatureTypeName), this.host.getEntityWorld());
            }
            else {
                this.entity = CreatureManager.getInstance().getCreature(this.creatureTypeName).getEntityClass().getConstructor(new Class[]{World.class}).newInstance(this.host.getEntityWorld());
            }
        }
        catch (Exception e) {
            LycanitesTweaks.LOGGER.log(Level.WARN,"[Stored Creature Entity] Unable to find an entity class. Type: {}", this.creatureTypeName);
            //e.printStackTrace();
        }

        if (!(this.entity instanceof EntityLiving))
            return;

        // Load NBT Data:
        this.loadEntityNBT();

        // calc PML after NBT Levels are loaded
        if(target instanceof EntityPlayer){
            IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer((EntityPlayer) target);
            if(pml != null && this.host instanceof EntityBossSummonCrystal && this.entity instanceof BaseCreatureEntity){
                switch (((EntityBossSummonCrystal) this.host).getVariantType()){
                    case 1:
                        if(PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.AltarBossMini)){
                            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.AltarBossMini) || Helpers.hasSoulgazerEquiped(target))
                                this.setLevel(this.getLevel() + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.AltarBossMini, (BaseCreatureEntity)this.entity));
                        }
                        break;
                    case 2:
                        if(PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.DungeonBoss)){
                            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.DungeonBoss) || Helpers.hasSoulgazerEquiped(target))
                                this.setLevel(this.getLevel() + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.DungeonBoss, (BaseCreatureEntity)this.entity));
                        }
                        break;
                    case -1:
                        if(PlayerMobLevelsConfig.getPmlBonusCategories().containsKey(PlayerMobLevelsConfig.BonusCategory.EncounterEvent)){
                            if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.EncounterEvent) || Helpers.hasSoulgazerEquiped(target))
                                this.setLevel(this.getLevel() + pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.EncounterEvent, (BaseCreatureEntity)this.entity));
                        }
                        break;
                    default:
                }
            }
        }

        // Spawn Location:
        this.entity.setLocationAndAngles(this.host.posX, this.host.posY, this.host.posZ, this.host.rotationYaw, 0.0F);

        // Entity Name and Appearance:
        if (!this.entityName.isEmpty()) {
            this.entity.setCustomNameTag(this.entityName);
        }
        if(this.isPersistant) {
            ((EntityLiving) this.entity).enablePersistence();
        }

        if (this.entity instanceof BaseCreatureEntity) {
            BaseCreatureEntity entityCreature = (BaseCreatureEntity)this.entity;

            // Better Spawn Location and Angle:
//            float randomAngle = 45F + (45F * this.host.getEntityWorld().rand.nextFloat());
//            if (this.host.getEntityWorld().rand.nextBoolean())
//                randomAngle = -randomAngle;
//            BlockPos spawnPos = entityCreature.getFacingPosition(this.host, -1, randomAngle);
//            if (!this.entity.getEntityWorld().isSideSolid(spawnPos, EnumFacing.UP))
//                this.entity.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), this.host.rotationYaw, 0.0F);
//            else {
//                spawnPos = entityCreature.getFacingPosition(this.host, -1, -randomAngle);
//                if (this.entity.getEntityWorld().isSideSolid(spawnPos, EnumFacing.UP))
//                    this.entity.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), this.host.rotationYaw, 0.0F);
//            }

            entityCreature.setRevengeTarget(target);
            if(this.fixate) entityCreature.setFixateTarget(target);
            if(this.home >= 0) entityCreature.setHome((int)entityCreature.posX, (int)entityCreature.posY, (int)entityCreature.posZ, (float)this.home);
            if(this.temporary > 0) entityCreature.setTemporary(this.temporary);
            if(!this.mobDrops.isEmpty()) {
                entityCreature.drops.clear();
                for (ItemDrop itemDrop : this.mobDrops) {
                    entityCreature.addSavedItemDrop(itemDrop);
                }
            }
            entityCreature.spawnedAsBoss = this.spawnAsBoss;

            if (this.blockProtection > 0) {
                entityCreature.spawnedWithBlockProtection = this.blockProtection;
                ExtendedWorld.getForWorld(entityCreature.getEntityWorld()).bossUpdate(entityCreature);
            }

            if (entityCreature.extraMobBehaviour != null && this.extraMobBehaviourNBT != null) {
                entityCreature.extraMobBehaviour.readFromNBT(this.extraMobBehaviourNBT);
            }

            // Entity Appearance:
            entityCreature.setLevel(this.entityLevel);
            entityCreature.setExperience(this.entityExperience);
            entityCreature.setSizeScale(this.entitySize);
            entityCreature.setSubspecies(this.subspeciesIndex);
            entityCreature.applyVariant(this.variantIndex);

            entityCreature.firstSpawn = false;
        }
        this.onSpawnEntity(this.entity);
        this.host.getEntityWorld().spawnEntity(this.entity);
    }

    /** Called when the entity for this entry is spawned just before it is added to the world. **/
    public void onSpawnEntity(Entity entity) {
        // This can be used on extensions of this class for NBT data, etc.
    }


    // ==================================================
    //                    Despawn Entity
    // ==================================================
    /** Despawns this entry's entity if it isn't already. This entry will still be active even if the entity is despawned so that it may be spawned again in the future. **/
    public void despawnEntity() {
        if (this.entity == null)
            return;
        this.onDespawnEntity(this.entity);
        this.saveEntityNBT();
        this.entity.setDead();
        this.entity = null;
    }

    /** Called when the entity for this entry is despawned. **/
    public void onDespawnEntity(Entity entity) {
        // This can be used on extensions of this class for NBT data, etc.
    }


    // ==================================================
    //                        NBT
    // ==================================================
    // ========== Read ===========
    /** Reads StoredCreatureEntity from NBTTag. Should be called by owners of IEntityStoreCreature capability **/
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if(nbtTagCompound.hasKey("creatureTypeName"))
            this.creatureTypeName = nbtTagCompound.getString("creatureTypeName");

        // Load Entity:
        if (nbtTagCompound.hasKey("EntityName"))
            this.setEntityName(nbtTagCompound.getString("EntityName"));

        if (nbtTagCompound.hasKey("SubspeciesID")) {
            this.setEntitySubspecies(Subspecies.getIndexFromOld(nbtTagCompound.getInteger("SubspeciesID")));
            this.setEntityVariant(Variant.getIndexFromOld(nbtTagCompound.getInteger("SubspeciesID")));
        }
        if (nbtTagCompound.hasKey("Subspecies"))
            this.setEntitySubspecies(nbtTagCompound.getInteger("Subspecies"));
        if (nbtTagCompound.hasKey("Variant"))
            this.setEntityVariant(nbtTagCompound.getInteger("Variant"));
        if (nbtTagCompound.hasKey("EntitySize"))
            this.setEntitySize(nbtTagCompound.getDouble("EntitySize"));

        if(nbtTagCompound.hasKey("isPersistant"))
            this.setPersistant(nbtTagCompound.getBoolean("isPersistant"));
        if(nbtTagCompound.hasKey("temporary"))
            this.setTemporary(nbtTagCompound.getInteger("temporary"));
        if(nbtTagCompound.hasKey("fixate"))
            this.setFixate(nbtTagCompound.getBoolean("fixate"));
        if(nbtTagCompound.hasKey("home"))
            this.setHome(nbtTagCompound.getDouble("home"));
        if(nbtTagCompound.hasKey("spawnAsBoss"))
            this.setSpawnAsBoss(nbtTagCompound.getBoolean("spawnAsBoss"));
        if(nbtTagCompound.hasKey("Drops")) {
            NBTTagList nbtDropList = nbtTagCompound.getTagList("Drops", 10);
            for(int i = 0; i < nbtDropList.tagCount(); i++) {
                NBTTagCompound dropNBT = nbtDropList.getCompoundTagAt(i);
                ItemDrop drop = new ItemDrop(dropNBT);
                this.mobDrops.add(drop);
            }
        }
        if(nbtTagCompound.hasKey("blockProtection"))
            this.setBlockProtection(nbtTagCompound.getInteger("blockProtection"));

        if(nbtTagCompound.hasKey("ExtraBehaviour"))
            this.extraMobBehaviourNBT = nbtTagCompound.getCompoundTag("ExtraBehaviour");

        if (nbtTagCompound.hasKey("EntityNBT"))
            this.entityNBT = nbtTagCompound.getCompoundTag("EntityNBT");
        this.loadEntityNBT();
    }

    // ========== Write ==========
    /** Writes StoredCreatureEntity to NBTTag. **/
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setString("creatureTypeName", this.creatureTypeName);

        // Save Entity:
        nbtTagCompound.setString("EntityName", this.entityName);
        nbtTagCompound.setInteger("Subspecies", this.subspeciesIndex);
        nbtTagCompound.setInteger("Variant", this.variantIndex);
        nbtTagCompound.setDouble("EntitySize", this.entitySize);

        nbtTagCompound.setBoolean("isPersistant", this.isPersistant);
        nbtTagCompound.setInteger("temporary", this.temporary);
        nbtTagCompound.setBoolean("fixate", this.fixate);
        nbtTagCompound.setDouble("home", this.home);
        nbtTagCompound.setBoolean("spawnAsBoss", this.spawnAsBoss);
        NBTTagList nbtDropList = new NBTTagList();
        for(ItemDrop drop : this.mobDrops) {
            NBTTagCompound dropNBT = new NBTTagCompound();
            if(drop.writeToNBT(dropNBT)) {
                nbtDropList.appendTag(dropNBT);
            }
        }
        nbtTagCompound.setTag("Drops", nbtDropList);
        nbtTagCompound.setInteger("blockProtection", this.blockProtection);
        if(this.extraMobBehaviourNBT != null) nbtTagCompound.setTag("ExtraBehaviour", this.extraMobBehaviourNBT);

        this.saveEntityNBT();
        nbtTagCompound.setTag("EntityNBT", this.entityNBT);
    }

    // ========== Save Entity NBT ==========
    /** If this StoredCreatureEntity currently has an active entity, this will save that entity's NBT data to this StoredCreatureEntity's record of it. **/
    public void saveEntityNBT() {
        if (this.entityNBT == null) {
            this.entityNBT = new NBTTagCompound();
        }

        this.entityNBT.setInteger("MobLevel", this.getLevel());
        this.entityNBT.setInteger("Experience", this.getExperience());

		// Creature Base:
        if (this.entity instanceof BaseCreatureEntity) {
            BaseCreatureEntity baseCreatureEntity = (BaseCreatureEntity)this.entity;
            baseCreatureEntity.inventory.writeToNBT(this.entityNBT);

            NBTTagCompound extTagCompound = new NBTTagCompound();
            baseCreatureEntity.extraMobBehaviour.writeToNBT(extTagCompound);
            this.entityNBT.setTag("ExtraBehaviour", extTagCompound);

            if (this.entity instanceof AgeableCreatureEntity) {
                AgeableCreatureEntity ageableCreatureEntity = (AgeableCreatureEntity)this.entity;
                this.entityNBT.setInteger("Age", ageableCreatureEntity.getGrowingAge());
            }
        }
        if(this.entity != null){
            // Update Pet Name:
            if (this.entity.hasCustomName()) {
                this.entityName = this.entity.getCustomNameTag();
            }

            this.entity.writeToNBT(this.entityNBT);
        }
    }

    // ========== Load Entity NBT ==========
    /** If this StoredCreatureEntity is spawning a new entity, this will load any saved entity NBT data onto it. **/
    public void loadEntityNBT() {
        if (this.entityNBT == null)
            return;

        if (this.entityNBT.hasKey("MobLevel")) {
            this.setLevel(this.entityNBT.getInteger("MobLevel"));
        }
        if (this.entityNBT.hasKey("Experience")) {
            this.setExperience(this.entityNBT.getInteger("Experience"));
        }

        if (this.entity instanceof BaseCreatureEntity) {
            BaseCreatureEntity baseCreatureEntity = (BaseCreatureEntity)this.entity;
            baseCreatureEntity.inventory.readFromNBT(this.entityNBT);
            if (this.entity instanceof AgeableCreatureEntity) {
                AgeableCreatureEntity ageableCreatureEntity = (AgeableCreatureEntity)this.entity;
                if (this.entityNBT.hasKey("Age"))
                    ageableCreatureEntity.setGrowingAge(this.entityNBT.getInteger("Age"));
                else
                    ageableCreatureEntity.setGrowingAge(0);
            }
        }
    }
}
