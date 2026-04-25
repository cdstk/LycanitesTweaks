package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldEntitySpawner.class)
public abstract class WorldEntitySpawner_SkyPlacementMixin {

    @ModifyExpressionValue(
            method = "getRandomChunkPosition",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getHeight(Lnet/minecraft/util/math/BlockPos;)I")
    )
    private static int lycanitesTweaks_vanillaWorldEntitySpawner_getRandomChunkPositionSky(int chunkHeight, World worldIn){
        if(worldIn.rand.nextInt(ForgeConfigHandler.minorFeaturesConfig.skyMonsterReplaceRate) == 0) {
            return worldIn.getHeight();
        }
        return chunkHeight;
    }
}
