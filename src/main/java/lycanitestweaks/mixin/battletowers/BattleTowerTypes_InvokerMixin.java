package lycanitestweaks.mixin.battletowers;

import atomicstryker.battletowers.common.AS_WorldGenTower;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = AS_WorldGenTower.TowerTypes.class)
public interface BattleTowerTypes_InvokerMixin {

    @Invoker(value = "getWallBlockID", remap = false)
    Block lycanitesTweaks$invokeGetWallBlockID();
}
