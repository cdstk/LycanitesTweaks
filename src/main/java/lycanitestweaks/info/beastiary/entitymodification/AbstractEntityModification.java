package lycanitestweaks.info.beastiary.entitymodification;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.SRPHandler;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleHorseVariant;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.NBTModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.StatusByte;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleAttacking;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleBaby;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleBatHanging;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleCreeperCharge;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleEvokerSpell;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleLeftHanded;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleParrotParty;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleSheared;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.VillagerProfessions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEntityModification {

    public static final String ENTITY_MODS_JSON = "bestiaryModifications";
    public static final String TYPE_JSON = "modType";
    public static final String KNOWLEDGE_JSON = "knowledgeRank";
    public static final String EXPERIENCE_JSON = "experienceRatio";

    private static final Map<Class<? extends Entity>, List<Class<? extends AbstractEntityModification>>> runtimeDefaults = new HashMap<>();
    public static void clearRunTimeMap() {
        runtimeDefaults.clear();
        if(ModLoadedUtil.iceandfire.isLoaded()) IceAndFireHandler.clearRunTimeMap();
        if(ModLoadedUtil.srp.isLoaded()) SRPHandler.clearRunTimeMap();
    }

    protected int knowledgeRank = 1;
    protected float experienceRatio = 0;

    // Bestiary Screen
    public abstract boolean isDefaultModification();
    public abstract boolean takesUserInput();
    public boolean refreshEntityData() { return false; }
    public abstract String getOptionLangKey();
    public abstract String getActionLangKey();

    // JSON
    public void generateDefaultValues(GenericEntityInfo entityInfo) { }
    public abstract void generateDefaultJSON(JsonObject json);

    // Screen Entity Visual
    public abstract void modifyEntity(Entity entity);
    public void modifyEntity(Entity entity, String value) { this.modifyEntity(entity); }

    public int getKnowledgeRankRequirement() { return this.knowledgeRank; }
    public float getKnowledgeExperienceRequirement() { return this.experienceRatio; }

    public static AbstractEntityModification loadFromJSON(JsonObject json) {
        AbstractEntityModification entityModification = null;
        if(json.has(NBTModification.TYPE_JSON)) {
            String type = json.get(TYPE_JSON).getAsString();

            switch (type) {
                case NBTModification.TYPE_VALUE: entityModification = new NBTModification(json); break;
                case StatusByte.TYPE_VALUE: entityModification = new StatusByte(json); break;
                case CycleIndexedVariant.TYPE_VALUE: entityModification = new CycleIndexedVariant(json); break;
                case CycleHorseVariant.TYPE_VALUE: entityModification = new CycleHorseVariant(); break;
                case ToggleAttacking.TYPE_VALUE: entityModification = new ToggleAttacking(); break;
                case ToggleBaby.TYPE_VALUE: entityModification = new ToggleBaby(); break;
                case ToggleBatHanging.TYPE_VALUE: entityModification = new ToggleBatHanging(); break;
                case ToggleCreeperCharge.TYPE_VALUE: entityModification = new ToggleCreeperCharge(); break;
                case ToggleEvokerSpell.TYPE_VALUE: entityModification = new ToggleEvokerSpell(); break;
                case ToggleLeftHanded.TYPE_VALUE: entityModification = new ToggleLeftHanded(); break;
                case ToggleParrotParty.TYPE_VALUE: entityModification = new ToggleParrotParty(); break;
                case ToggleSheared.TYPE_VALUE: entityModification = new ToggleSheared(); break;
                case VillagerProfessions.TYPE_VALUE: entityModification = new VillagerProfessions(); break;
            }

            if(entityModification == null && ModLoadedUtil.iceandfire.isLoaded()) {
                entityModification = IceAndFireHandler.loadFromJSON(json, type);
            }

            if(entityModification == null && ModLoadedUtil.srp.isLoaded()) {
                entityModification = SRPHandler.loadFromJSON(json, type);
            }

            if(entityModification != null) {
                if(json.has(KNOWLEDGE_JSON)) entityModification.knowledgeRank = json.get(KNOWLEDGE_JSON).getAsInt();
                if(json.has(EXPERIENCE_JSON)) entityModification.experienceRatio = json.get(EXPERIENCE_JSON).getAsFloat();
            }
        }
        return entityModification;
    }

    public static boolean shouldSkipModification(Class<? extends Entity> infoClazz, Class<? extends AbstractEntityModification> modificationClazz) {
        if(ModLoadedUtil.iceandfire.isLoaded() && IceAndFireHandler.shouldSkipModification(infoClazz, modificationClazz)) return true;

        return false;
    }

    // After FML Post Init and right before JSON is written
    public static void assignRuntimeDefaults(GenericEntityInfo entityInfo) {
        if(ForgeConfigHandler.genericBestiary.disableDefaultEntityMods) return;
        entityInfo.bestiaryModifiers.add(new NBTModification());

        if(runtimeDefaults.isEmpty()) initClassMap();

        runtimeDefaults.forEach((entityClazz, entityModifications) -> {
            if(entityClazz.isAssignableFrom(entityInfo.getEntityClass())) {
                entityModifications.forEach(modificationClazz -> {
                    try {
                        if(shouldSkipModification(entityInfo.getEntityClass(), modificationClazz)) return;
                        AbstractEntityModification entityMod = modificationClazz.newInstance();
                        entityMod.generateDefaultValues(entityInfo);
                        entityInfo.bestiaryModifiers.add(entityMod);
                    } catch (InstantiationException | IllegalAccessException e) {
                        LycanitesTweaks.LOGGER.log(Level.WARN, "Failed generate default JSONs for Entity Info: {} -> {}", entityInfo.getEntityId(), modificationClazz);
                    }
                });
            }
        });

        if(entityInfo.getEntityClass() == EntityVillager.class || entityInfo.getEntityClass() == EntityZombieVillager.class) {
            AbstractEntityModification entityMod = new VillagerProfessions();
            entityMod.generateDefaultValues(entityInfo);
            entityInfo.bestiaryModifiers.add(entityMod);
        }

        if(ModLoadedUtil.iceandfire.isLoaded()) {
            IceAndFireHandler.assignRuntimeDefaults(entityInfo);
        }
        if(ModLoadedUtil.srp.isLoaded()) {
            SRPHandler.assignRuntimeDefaults(entityInfo);
        }
    }

    // Map of Entity -> Modifiers to generate
    private static void initClassMap() {
        addToDefaults(AbstractIllager.class, ToggleLeftHanded.class);
        addToDefaults(AbstractSkeleton.class, ToggleAttacking.class, ToggleLeftHanded.class);

        addToDefaults(EntityAgeable.class, ToggleBaby.class);

        addToDefaults(EntityBat.class, ToggleBatHanging.class);
        addToDefaults(EntityCreeper.class, ToggleCreeperCharge.class);
        addToDefaults(EntityEnderman.class, ToggleAttacking.class);
        addToDefaults(EntityGhast.class, ToggleAttacking.class);
        addToDefaults(EntityHorse.class, CycleHorseVariant.class);
        addToDefaults(EntityIllusionIllager.class, ToggleAttacking.class);
        addToDefaults(EntityIronGolem.class, StatusByte.class);
        addToDefaults(EntityLlama.class, CycleIndexedVariant.class);
        addToDefaults(EntityOcelot.class, CycleIndexedVariant.class);
        addToDefaults(EntityParrot.class, ToggleParrotParty.class, CycleIndexedVariant.class);
        addToDefaults(EntityRabbit.class, StatusByte.class, CycleIndexedVariant.class);
        addToDefaults(EntitySheep.class, ToggleSheared.class, CycleIndexedVariant.class);
        addToDefaults(EntityShulker.class, ToggleAttacking.class, CycleIndexedVariant.class);
        addToDefaults(EntitySnowman.class, ToggleSheared.class);
        addToDefaults(EntitySpellcasterIllager.class, ToggleEvokerSpell.class);
        addToDefaults(EntitySquid.class, StatusByte.class);
        addToDefaults(EntityVex.class, ToggleAttacking.class, ToggleLeftHanded.class);
        addToDefaults(EntityVindicator.class, ToggleAttacking.class);
        addToDefaults(EntityWolf.class, ToggleAttacking.class, StatusByte.class);
        addToDefaults(EntityZombie.class, ToggleBaby.class);
    }

    @SafeVarargs
    private static void addToDefaults(Class<? extends EntityLiving> entityClazz, Class<? extends AbstractEntityModification>... modificationClasses) {
        runtimeDefaults.compute(entityClazz, ((clazz, classes) -> {
            if(classes == null) {
                classes = new ArrayList<>(ImmutableList.copyOf(modificationClasses));
            }
            return classes;
        }));
    }
}
