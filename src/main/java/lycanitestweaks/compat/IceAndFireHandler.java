package lycanitestweaks.compat;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityBlackFrostDragon;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityDreadBeast;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.github.alexthe666.iceandfire.entity.EntityDreadScuttler;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntityShivaxiDragon;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.entitymodification.iceandfire.INFAttacking;
import lycanitestweaks.compat.entitymodification.iceandfire.INFDragonStage;
import lycanitestweaks.compat.entitymodification.iceandfire.INFFlying;
import lycanitestweaks.compat.entitymodification.iceandfire.INFGender;
import lycanitestweaks.compat.entitymodification.iceandfire.INFGiantSizeScale;
import lycanitestweaks.compat.entitymodification.iceandfire.INFGiantVariant;
import lycanitestweaks.compat.entitymodification.iceandfire.INFHydraHeads;
import lycanitestweaks.compat.entitymodification.iceandfire.INFTrollWeapon;
import lycanitestweaks.compat.entitymodification.iceandfire.INFVariant;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleBaby;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class IceAndFireHandler {


    public static boolean hasMobSkull() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[1.8,)");
    }

    public static boolean hasDreadHydra() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[1.9,)");
    }

    public static boolean hasLightningDragon() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[2,)");
    }

    public static boolean hasGhost() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[2.1,)");
    }

    public static boolean hasShivaxiDragon() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.iceandfire, "[2.2,)");
    }

    public static boolean isRLCraft() {
        return ModLoadedUtil.iceandfire.containsAuthor("Kotlin-Programmer");
    }

    public static boolean isROTN() {
        return ModLoadedUtil.iceandfire.getName().equals("Ice and Fire: RotN Edition");
    }

    // JSON
    private static final Map<Class<? extends Entity>, List<Class<? extends AbstractEntityModification>>> runtimeDefaults = new HashMap<>();
    private static final Map<Class<? extends Entity>, Set<Class<? extends AbstractEntityModification>>> runtimeConflicts = new HashMap<>();
    public static void clearRunTimeMap() {
        runtimeDefaults.clear();
        runtimeConflicts.clear();
    }

    public static AbstractEntityModification loadFromJSON(JsonObject json, String type) {
        AbstractEntityModification entityModification = null;
        switch (type) {
            case INFGiantSizeScale.TYPE_VALUE: entityModification = new INFGiantSizeScale(json); break;
            case INFVariant.TYPE_VALUE: entityModification = new INFVariant(json); break;
            case INFAttacking.TYPE_VALUE: entityModification = new INFAttacking(); break;
            case INFDragonStage.TYPE_VALUE: entityModification = new INFDragonStage(); break;
            case INFFlying.TYPE_VALUE: entityModification = new INFFlying(); break;
            case INFGender.TYPE_VALUE: entityModification = new INFGender(); break;
            case INFGiantVariant.TYPE_VALUE: entityModification = new INFGiantVariant(); break;
            case INFHydraHeads.TYPE_VALUE: entityModification = new INFHydraHeads(); break;
            case INFTrollWeapon.TYPE_VALUE: entityModification = new INFTrollWeapon(); break;
        }
        return entityModification;
    }

    public static void modifyGenericEntityJSON(GenericEntityInfo entityInfo) {
        if(EntityDragonEgg.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityDragonSkull.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityMyrmexEgg.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntityStoneStatue.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }

        if(hasMobSkull()) {
            if(EntityMobSkull.class.isAssignableFrom(entityInfo.getEntityClass())) {
                entityInfo.disableBestiaryEntry = true;
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
    }

    public static boolean shouldSkipModification(Class<? extends Entity> entityClazz, Class<? extends AbstractEntityModification> modificationClazz) {
        if(ForgeConfigHandler.genericBestiary.disableModdedConflicts) return false;

        if(runtimeConflicts.isEmpty()) {
            addToConflicts(EntityDeathWorm.class, ToggleBaby.class);
            addToConflicts(EntityDragonBase.class, ToggleBaby.class);
            addToConflicts(EntityMyrmexBase.class, ToggleBaby.class);
            addToConflicts(EntitySeaSerpent.class, ToggleBaby.class);

            if(hasDreadHydra()) {
                addToConflicts(EntityBlackFrostDragon.class, INFDragonStage.class, INFGender.class, INFVariant.class);
            }

            if(hasShivaxiDragon()) {
                addToConflicts(EntityShivaxiDragon.class, INFGender.class, INFVariant.class);
            }
        }

        for(Class<?extends Entity> conflictClazz : runtimeConflicts.keySet()) {
            if(conflictClazz.isAssignableFrom(entityClazz)) {
                if(runtimeConflicts.get(conflictClazz).contains(modificationClazz)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void initClassMap() {
        addToDefaults(EntityAmphithere.class, INFAttacking.class, INFFlying.class, INFVariant.class);
        addToDefaults(EntityCockatrice.class, INFAttacking.class, INFGender.class);
        addToDefaults(EntityCyclops.class, INFAttacking.class, INFVariant.class);
        addToDefaults(EntityDeathWorm.class, INFAttacking.class, INFGiantSizeScale.class, INFGiantVariant.class, INFVariant.class);
        addToDefaults(EntityDragonBase.class, INFAttacking.class, INFFlying.class, INFDragonStage.class, INFVariant.class, INFGender.class);
        addToDefaults(EntityGorgon.class, INFAttacking.class);
        addToDefaults(EntityHippocampus.class, INFVariant.class);
        addToDefaults(EntityHippogryph.class, INFAttacking.class, INFFlying.class, INFVariant.class);
        addToDefaults(EntityMyrmexBase.class, INFAttacking.class, INFVariant.class);
        addToDefaults(EntityMyrmexRoyal.class, INFFlying.class);
        addToDefaults(EntityPixie.class, INFVariant.class);
        addToDefaults(EntitySeaSerpent.class, INFAttacking.class, INFGiantSizeScale.class, INFGiantVariant.class, INFVariant.class);
        addToDefaults(EntitySiren.class, INFAttacking.class, INFVariant.class);
        addToDefaults(EntityStymphalianBird.class, INFAttacking.class, INFFlying.class);
        addToDefaults(EntityTroll.class, INFAttacking.class, INFTrollWeapon.class, INFVariant.class);

        if(IceAndFireHandler.hasDreadHydra()) {
            addToDefaults(EntityDreadBeast.class, INFAttacking.class, INFVariant.class);
            addToDefaults(EntityDreadGhoul.class, INFAttacking.class, INFVariant.class);
            addToDefaults(EntityDreadKnight.class, INFAttacking.class, INFVariant.class);
            addToDefaults(EntityDreadLich.class, INFAttacking.class, INFVariant.class);
            addToDefaults(EntityDreadQueen.class, INFAttacking.class);
            addToDefaults(EntityDreadScuttler.class, INFAttacking.class);
            addToDefaults(EntityDreadThrall.class, INFAttacking.class, INFVariant.class);
            addToDefaults(EntityHydra.class, INFAttacking.class, INFHydraHeads.class, INFVariant.class);
        }

        if(IceAndFireHandler.hasGhost()) {
            addToDefaults(EntityGhost.class, INFAttacking.class, INFVariant.class);
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

    @SafeVarargs
    private static void addToConflicts(Class<? extends EntityLiving> entityClazz, Class<? extends AbstractEntityModification>... modificationClasses) {
        runtimeConflicts.compute(entityClazz, ((clazz, classes) -> {
            if(classes == null) {
                classes = new HashSet<>(ImmutableList.copyOf(modificationClasses));
            }
            return classes;
        }));
    }
}
