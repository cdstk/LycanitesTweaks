package lycanitestweaks.compat;

import atomicstryker.battletowers.common.AS_EntityGolem;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.entitymodification.battletowers.ASBTAwake;
import lycanitestweaks.compat.entitymodification.battletowers.ASBTVariant;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleTowersHandler {

    private static final Map<Class<? extends Entity>, List<Class<? extends AbstractEntityModification>>> runtimeDefaults = new HashMap<>();
    public static void clearRunTimeMap() {
        runtimeDefaults.clear();
    }

    public static AbstractEntityModification loadFromJSON(JsonObject json, String type) {
        AbstractEntityModification entityModification = null;
        switch (type) {
            case ASBTAwake.TYPE_VALUE: entityModification = new ASBTAwake(); break;
            case ASBTVariant.TYPE_VALUE: entityModification = new ASBTVariant(); break;
        }
        return entityModification;
    }

    public static void assignRuntimeDefaults(GenericEntityInfo entityInfo) {
        if(ForgeConfigHandler.genericBestiary.disableDefaultEntityMods) return;
        if(runtimeDefaults.isEmpty()) {
            List<Class<? extends AbstractEntityModification>> list = new ArrayList<>();
            list.add(ASBTAwake.class);
            list.add(ASBTVariant.class);
            runtimeDefaults.put(AS_EntityGolem.class, list);
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
