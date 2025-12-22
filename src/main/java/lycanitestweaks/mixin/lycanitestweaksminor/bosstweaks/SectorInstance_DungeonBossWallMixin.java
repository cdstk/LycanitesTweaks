package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.dungeon.instance.SectorConnector;
import com.lycanitesmobs.core.dungeon.instance.SectorInstance;
import lycanitestweaks.util.IDungeonTheme_AdditionalMixin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(SectorConnector.class)
public abstract class SectorInstance_DungeonBossWallMixin {

    @Shadow(remap = false) public BlockPos position;
    @Shadow(remap = false) public SectorInstance parentSector;
    @Shadow(remap = false) public EnumFacing facing;

    @WrapOperation(
            method = "buildEntrance",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/dungeon/instance/SectorInstance;getAirBlockForPos(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"),
            remap = false
    )
    private IBlockState lycanitesTweaks_lycanitesMobsSectorConnector_buildEntranceBossRoomWall(SectorInstance instance, BlockPos pos, Operation<IBlockState> original, @Local(argsOnly = true) Random random){
        if(this.parentSector != null && "bossroom".equals(this.parentSector.dungeonSector.type)){
            if(this.parentSector.theme instanceof IDungeonTheme_AdditionalMixin){
                boolean replaceAir = (this.facing == EnumFacing.EAST || this.facing == EnumFacing.WEST) ? pos.getX() == this.position.getX() : pos.getZ() == this.position.getZ();
                if(replaceAir) return ((IDungeonTheme_AdditionalMixin) this.parentSector.theme).lycanitesTweaks$getBossWall('B', random);
            }
        }
        return original.call(instance, pos);
    }

}
