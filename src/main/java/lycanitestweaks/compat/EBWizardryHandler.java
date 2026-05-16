package lycanitestweaks.compat;

import com.google.gson.JsonObject;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityIceWraith;
import electroblob.wizardry.entity.living.EntityLightningWraith;
import electroblob.wizardry.entity.living.EntitySpiritHorse;
import electroblob.wizardry.entity.living.EntitySpiritWolf;
import electroblob.wizardry.entity.living.EntitySummonedCreature;
import electroblob.wizardry.entity.living.EntityWizard;
import electroblob.wizardry.entity.living.ISummonedCreature;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.entitymodification.ebwizardy.EBWWizardElement;
import lycanitestweaks.compat.entitymodification.ebwizardy.EBWWizardSkin;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.NewSpawn;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleLeftHanded;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EBWizardryHandler {

    private static final Map<Class<? extends Entity>, List<Class<? extends AbstractEntityModification>>> runtimeDefaults = new HashMap<>();
    public static void clearRunTimeMap() {
        runtimeDefaults.clear();
    }

    public static AbstractEntityModification loadFromJSON(JsonObject json, String type) {
        AbstractEntityModification entityModification = null;
        switch (type) {
            case EBWWizardElement.TYPE_VALUE: entityModification = new EBWWizardElement(); break;
            case EBWWizardSkin.TYPE_VALUE: entityModification = new EBWWizardSkin(); break;
        }
        return entityModification;
    }

    public static void modifyGenericEntityJSON(GenericEntityInfo entityInfo) {
        // Should only show
        // Ice Wraith, Lightning Wraith, Wizard, Evil Wizard, Remnant

        if(EntitySpiritHorse.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntitySpiritWolf.class.equals(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }

        if(EntityIceWraith.class.equals(entityInfo.getEntityClass())) {
            return;
        }
        else if(EntityLightningWraith.class.equals(entityInfo.getEntityClass())) {
            return;
        }
        else if(ISummonedCreature.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
        else if(EntitySummonedCreature.class.isAssignableFrom(entityInfo.getEntityClass())) {
            entityInfo.disableBestiaryEntry = true;
        }
    }

    public static void assignRuntimeDefaults(GenericEntityInfo entityInfo) {
        if(ForgeConfigHandler.genericBestiary.disableDefaultEntityMods) return;
        if(runtimeDefaults.isEmpty()) {
            List<Class<? extends AbstractEntityModification>> list = new ArrayList<>();
            list.add(ToggleLeftHanded.class);
            list.add(NewSpawn.class);
            runtimeDefaults.put(EntityWizard.class, list);
            runtimeDefaults.put(EntityEvilWizard.class, list);
        }

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
}
