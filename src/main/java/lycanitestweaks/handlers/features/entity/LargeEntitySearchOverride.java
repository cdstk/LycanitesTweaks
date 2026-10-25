package lycanitestweaks.handlers.features.entity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

// Instead of performance loss using World.MAX_ENTITY_RADIUS, super large entities skip the sub chunk check
public class LargeEntitySearchOverride {

    // Used by Client and Server
    private static final Cache<Entity, Boolean> ACTIVE_ENTITY_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS) // Accessed often by Client Renderer
            .build();

    public static Collection<Entity> getActiveEntities() {
        return ACTIVE_ENTITY_CACHE.asMap().keySet();
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntityLiving();
            // Every 5 seconds
            if(creature.ticksExisted % 100 == 0) {
                if(creature.isBossAlways()) {
                    ACTIVE_ENTITY_CACHE.put(creature, true);
                }
                // Should work if instant FirstSpawn is enabled
                else if(creature.sizeScale > CreatureManager.getInstance().config.randomSizeMax) {
                    ACTIVE_ENTITY_CACHE.put(creature, true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        ACTIVE_ENTITY_CACHE.invalidate(event.getEntityLiving());
    }
}
