package lycanitestweaks.compat;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPrimitive;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPure;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityBiomass;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityTendril;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWave;
import com.dhanantry.scapeandrunparasites.entity.monster.EntityWaveShock;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityBanoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityNoglaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOroncoTen;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHost;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHostII;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityLesh;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerWolf;
import com.dhanantry.scapeandrunparasites.entity.monster.hijacked.EntityHiBlaze;
import com.dhanantry.scapeandrunparasites.entity.monster.hijacked.EntityHiSkeleton;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityKol;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityRathol;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityDorpa;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfDragonE;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfVillager;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityNogla;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityWymo;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityEsor;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityGanro;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntitySoo;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityTenn;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityDropPod;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileHomming;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.entitymodification.srparasites.SRPAttacking;
import lycanitestweaks.compat.entitymodification.srparasites.SRPBurrow;
import lycanitestweaks.compat.entitymodification.srparasites.SRPCrawling;
import lycanitestweaks.compat.entitymodification.srparasites.SRPFlying;
import lycanitestweaks.compat.entitymodification.srparasites.SRPStatus;
import lycanitestweaks.compat.entitymodification.srparasites.SRPVariant;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.NewSpawn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SRPHandler {

    public static boolean beyondDotNineEleven() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.srp, "(1.9.11,]");
    }

    public static boolean upToDotNineTwentyOne() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.srp, "[,1.9.21]");
    }

    // JSON
    private static final Map<Class<? extends Entity>, List<Class<? extends AbstractEntityModification>>> runtimeDefaults = new HashMap<>();
    public static void clearRunTimeMap() {
        runtimeDefaults.clear();
    }

    public static AbstractEntityModification loadFromJSON(JsonObject json, String type) {
        AbstractEntityModification entityModification = null;
        switch (type) {
            case SRPVariant.TYPE_VALUE: entityModification = new SRPVariant(json); break;
            case SRPStatus.TYPE_VALUE: entityModification = new SRPStatus(json); break;
            case SRPAttacking.TYPE_VALUE: entityModification = new SRPAttacking(); break;
            case SRPBurrow.TYPE_VALUE: entityModification = new SRPBurrow(); break;
            case SRPCrawling.TYPE_VALUE: entityModification = new SRPCrawling(); break;
            case SRPFlying.TYPE_VALUE: entityModification = new SRPFlying(); break;
        }
        return entityModification;
    }

    public static void modifyGenericEntityJSON(GenericEntityInfo entityInfo) {
        // Kill does not count
        if(EntityProjectileHomming.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityTendril.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityWave.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        // Stupid af
        else if(EntityBiomass.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityDropPod.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        // Moving Flesh
        else if(EntityLesh.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        // Dreadnaut Tentacle
        else if(EntityOroncoTen.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        // Worker
        else if(EntityKol.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(beyondDotNineEleven()) {
            if(EntityWaveShock.class.equals(entityInfo.getEntityClass())) {
                entityInfo.disableBestiaryEntry = true;
            }
            if(upToDotNineTwentyOne()) {
                // Architect
                if (EntityTenn.class.equals(entityInfo.getEntityClass())) {
                    entityInfo.disableBestiaryEntry = true;
                }
                // Seeker
                else if (EntitySoo.class.equals(entityInfo.getEntityClass())) {
                    entityInfo.disableBestiaryEntry = true;
                }
                else if (EntityFerWolf.class.equals(entityInfo.getEntityClass())) {
                    entityInfo.disableBestiaryEntry = true;
                }
                else if (EntityHiSkeleton.class.equals(entityInfo.getEntityClass())) {
                    entityInfo.disableBestiaryEntry = true;
                }
                else if (EntityHiBlaze.class.equals(entityInfo.getEntityClass())) {
                    entityInfo.disableBestiaryEntry = true;
                }
            }
        }
    }

    public static void assignRuntimeDefaults(GenericEntityInfo entityInfo) {
        if(ForgeConfigHandler.genericBestiary.disableDefaultEntityMods) return;
        if(runtimeDefaults.isEmpty()) initClassMap();

        runtimeDefaults.forEach((entityClazz, entityModifications) -> {
            if(entityClazz.isAssignableFrom(entityInfo.getEntityClass())) {
                entityModifications.forEach(modificationClazz -> {
                    try {
                        AbstractEntityModification entityMod = modificationClazz.newInstance();
                        entityMod.generateDefaultValues(entityInfo);
                        entityInfo.bestiaryModifiers.add(entityMod);
                    } catch (InstantiationException | IllegalAccessException e) {
                        LycanitesTweaks.LOGGER.log(Level.WARN, "Failed generate default JSONs for Entity Info: {} -> {}", entityInfo.getEntityId(), modificationClazz);
                    }
                });
            }
        });
    }

    private static void initClassMap() {
        addToDefaults(EntityParasiteBase.class, SRPAttacking.class, NewSpawn.class);

        addToDefaults(EntityInfEnderman.class, SRPCrawling.class);
        addToDefaults(EntityInfDragonE.class, SRPFlying.class);
        addToDefaults(EntityHost.class, SRPBurrow.class);
        addToDefaults(EntityHostII.class, SRPBurrow.class);

        addToDefaults(EntityPAdapted.class, SRPVariant.class);
        addToDefaults(EntityPPrimitive.class, SRPVariant.class);
        addToDefaults(EntityPPure.class, SRPVariant.class);

        // Inborn
//        addToDefaults(EntityButhol.class, SRPVariant.class);
//        addToDefaults(EntityGothol.class, SRPVariant.class);
        addToDefaults(EntityMudo.class, SRPVariant.class);
        addToDefaults(EntityNuuh.class, SRPVariant.class);
        addToDefaults(EntityRathol.class, SRPVariant.class);
        addToDefaults(EntityDorpa.class, SRPVariant.class);

        // Misc
        addToDefaults(EntityInfHuman.class, SRPVariant.class);
        addToDefaults(EntityInfVillager.class, SRPVariant.class);
        addToDefaults(EntityPheon.class, SRPVariant.class);
        addToDefaults(EntityVesta.class, SRPVariant.class);

        addToDefaults(EntityBanoAdapted.class, SRPStatus.class);
        addToDefaults(EntityCanraAdapted.class, SRPStatus.class);
        addToDefaults(EntityNoglaAdapted.class, SRPStatus.class);
        addToDefaults(EntityCanra.class, SRPStatus.class);
        addToDefaults(EntityNogla.class, SRPStatus.class);
        addToDefaults(EntityEsor.class, SRPStatus.class);
        addToDefaults(EntityGanro.class, SRPStatus.class);
        addToDefaults(EntityOrch.class, SRPStatus.class);

        if(beyondDotNineEleven()) {
            if(upToDotNineTwentyOne()) {
                addToDefaults(EntityWymo.class, SRPBurrow.class);
            }
        }
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
