package lycanitestweaks.mixin.vanilla;

import com.google.common.base.Predicate;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.features.entity.LargeEntitySearchOverride;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(World.class)
public abstract class World_LargeEntitySearchMixin {

    @Shadow protected abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);

    @ModifyReturnValue(
            method = "getEntitiesWithinAABBExcludingEntity",
            at = @At("RETURN")
    )
    private List<Entity> lycanitesTweaks_vanillaWorld_getEntitiesInAABBexcludingAddLargeToRLTweaker(List<Entity> list, @Nullable Entity entityIn, AxisAlignedBB boundingBox){
        // Add to RLTweaker's less Collisions Hook
        if(ModLoadedUtil.rltweaker.isLoaded()) {
            this.lycanitesTweaks$addLargeEntitiesToList(entityIn, boundingBox, EntitySelectors.NOT_SPECTATING, list);
        }
        return list;
    }

    @ModifyReturnValue(
            method = "getEntitiesInAABBexcluding",
            at = @At("RETURN")
    )
    private List<Entity> lycanitesTweaks_vanillaWorld_getEntitiesInAABBexcludingAddLarge(List<Entity> list, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity > predicate){
        this.lycanitesTweaks$addLargeEntitiesToList(entityIn, boundingBox, predicate, list);
        return list;
    }

    @Unique
    private void lycanitesTweaks$addLargeEntitiesToList(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity > predicate, List<Entity> list) {
        for(Entity entity : LargeEntitySearchOverride.getActiveEntities()) {
            if (!list.contains(entity) && this.isChunkLoaded(entity.chunkCoordX, entity.chunkCoordZ, true)) {
                if (entity != entityIn && entity.getEntityBoundingBox().intersects(boundingBox)) {
                    if (predicate == null || predicate.apply(entity)) {
                        list.add(entity);
                    }
                }
            }
        }
    }
}
