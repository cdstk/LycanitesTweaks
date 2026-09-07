package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks.controllanding;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.util.IBaseCreatureEntity_FlightCommandMixin;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_FlightCommandMixin extends EntityLiving implements IBaseCreatureEntity_FlightCommandMixin {

    @Shadow(remap = false)
    public abstract boolean isFlying();
    @Shadow(remap = false)
    public abstract boolean isSafeToLand();

    @Unique
    private IBaseCreatureEntity_FlightCommandMixin.COMMAND lycanitesTweaks$flightCommand = COMMAND.NONE;

    public BaseCreatureEntity_FlightCommandMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onLivingUpdateSetFlightCommand(CallbackInfo ci){
        BaseCreatureEntity creature = (BaseCreatureEntity) (Object) this;
        if(!this.getEntityWorld().isRemote && creature instanceof TameableCreatureEntity) {
            TameableCreatureEntity pet = (TameableCreatureEntity) creature;
            COMMAND flightCommand = COMMAND.NONE;

            if(LycanitesEntityUtil.playerPetAutoFly(pet)) {
                flightCommand = COMMAND.FLY;
            }
            else if(LycanitesEntityUtil.playerPetAutoLand(pet)) {
                flightCommand = COMMAND.LAND;
            }

            if(LycanitesEntityUtil.playerWantsPetToFly(pet)) {
                flightCommand = COMMAND.FLY;
            }
            else if (LycanitesEntityUtil.playerWantsPetToLand(pet)) {
                flightCommand = COMMAND.LAND;
            }

            if(flightCommand != this.lycanitesTweaks$getFlightCommand()) {
                this.lycanitesTweaks$setFlightCommand(flightCommand);
            }
        }
    }

    @Inject(
            method = "rollWanderChance",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_rollWanderChancePlayerForced(CallbackInfoReturnable<Boolean> cir){
        if(this.lycanitesTweaks$getFlightCommand() == COMMAND.LAND) {
            if (this.isSafeToLand()) {
                cir.setReturnValue(false);
            }
            else {
                cir.setReturnValue(true);
            }
        }
        else if(this.lycanitesTweaks$getFlightCommand() == COMMAND.FLY && this.isFlying()) {
            if (this.isSafeToLand()) {
                cir.setReturnValue(true);
            }
            else {
                cir.setReturnValue(false);
            }
        }
    }

    @WrapMethod(
            method = "getWanderPosition",
            remap = false
    )
    private BlockPos lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getWanderPositionLanding(BlockPos wanderPosition, Operation<BlockPos> original){
        if(this.lycanitesTweaks$getFlightCommand() == COMMAND.LAND) {
            BlockPos.MutableBlockPos groundPos = new BlockPos.MutableBlockPos(this.getPosition());
            while(groundPos.getY() > 0 && this.getEntityWorld().getBlockState(groundPos).getBlock() == Blocks.AIR) {
                groundPos.setY(groundPos.getY() - 1);
            }
            if(this.getEntityWorld().getBlockState(groundPos).getMaterial().isSolid()) {
                groundPos.setY(groundPos.getY() + 1);
                return groundPos;
            }
        }
        else if(this.lycanitesTweaks$getFlightCommand() == COMMAND.FLY) {
            BlockPos airPos = this.getPosition().up();
            while(airPos.getY() < this.posY + this.height && this.getEntityWorld().getBlockState(airPos).getBlock() == Blocks.AIR) {
                airPos = airPos.up();
            }
            return airPos;
        }
        return wanderPosition;
    }

    @Unique
    @Override
    public void lycanitesTweaks$setFlightCommand(COMMAND command) {
        this.lycanitesTweaks$flightCommand = command;
    }

    @Unique
    @Override
    public COMMAND lycanitesTweaks$getFlightCommand() {
        return this.lycanitesTweaks$flightCommand;
    }
}
