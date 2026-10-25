package lycanitestweaks.mixin.largelycanites;

import com.google.common.base.Predicate;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.handlers.features.entity.LargeEntitySearchOverride;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.List;

// TODO Merge Early and Late after making FermiumBooter 1.6.0 Dependency
@Mixin(World.class)
public abstract class World_LargeEntitySearchMixin {

    @Shadow protected abstract boolean isChunkLoaded(int x, int z, boolean allowEmpty);

    @ModifyReturnValue(
            method = "getEntitiesInAABBexcluding",
            at = @At("RETURN")
    )
    private List<Entity> lycanitesTweaks_vanillaWorld_getEntitiesInAABBexcludingAddLarge(List<Entity> original, @Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity > predicate, @Local List<Entity> list){
        for(Entity entity : LargeEntitySearchOverride.getActiveEntities()) {
            if (this.isChunkLoaded(entity.chunkCoordX, entity.chunkCoordZ, true)) {
                if (entity != entityIn && entity.getEntityBoundingBox().intersects(boundingBox)) {
                    if (predicate == null || predicate.apply(entity)) {
                        list.add(entity);
                    }
                }
            }
        }
        return original;
    }
}
