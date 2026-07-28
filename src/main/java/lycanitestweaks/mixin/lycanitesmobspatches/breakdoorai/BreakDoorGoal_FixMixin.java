package lycanitestweaks.mixin.lycanitesmobspatches.breakdoorai;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.goals.actions.BreakDoorGoal;
import com.lycanitesmobs.core.entity.goals.actions.DoorInteractGoal;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BreakDoorGoal.class)
public abstract class BreakDoorGoal_FixMixin extends DoorInteractGoal {

    public BreakDoorGoal_FixMixin(EntityLiving par1EntityLiving) {
        super(par1EntityLiving);
    }

    @ModifyReturnValue(
            method = "shouldExecute",
            at = @At("RETURN")
    )
    private boolean lycanitesTweaks_lycanitesMobsBreakDoorGoal_shouldExecuteForgeChecks(boolean shouldExecute){
        if(shouldExecute) {
            BlockPos doorPosition = new BlockPos(this.entityPosX, this.entityPosY, this.entityPosZ);
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.host.world, this.host)
                    || !this.host.world.getBlockState(doorPosition).getBlock().canEntityDestroy(this.host.world.getBlockState(doorPosition), this.host.world, doorPosition, this.host)
                    || !net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this.host, doorPosition, this.host.world.getBlockState(doorPosition))) {
                return false;
            }
            return true;
        }
        return false;
    }

    @ModifyConstant(
            method = "updateTask",
            constant = @Constant(intValue = 1010)
    )
    private int lycanitesTweaks_lycanitesMobsBreakDoorGoal_updateTaskBreakProgressSound(int constant){
        return 1019;
    }

    @ModifyConstant(
            method = "updateTask",
            constant = @Constant(intValue = 1012)
    )
    private int lycanitesTweaks_lycanitesMobsBreakDoorGoal_updateTaskBreakDoneSound(int constant){
        return 1021;
    }

    @ModifyArg(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V", ordinal = 2),
            index = 2
    )
    private int lycanitesTweaks_lycanitesMobsBreakDoorGoal_updateTaskDropDoorType(int type){
        return Block.getIdFromBlock(this.targetDoor);
    }
}
