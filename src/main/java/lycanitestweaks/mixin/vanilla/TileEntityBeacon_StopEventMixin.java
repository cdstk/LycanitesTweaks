package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TileEntityBeacon.class)
public abstract class TileEntityBeacon_StopEventMixin extends TileEntityLockable {

    @Inject(
            method = "addEffectsToPlayers",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;")
    )
    private void lycanitesTweaks_vanillaTileEntityBeacon_addEffectsToPlayersDespawnEventMobs(CallbackInfo ci, @Local AxisAlignedBB axisalignedbb){
        List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, axisalignedbb, LycanitesMobsWrapper.TEMPORARY_EVENT_MOB);
        list.forEach(LycanitesMobsWrapper::setInstantDespawn);

        // For Visual Effect
        list.forEach(entity -> entity.setPosition(
                this.pos.getX() + 0.5,
                this.pos.getY() + 1,
                this.pos.getZ() + 0.5)
        );

        if(!list.isEmpty()) {
            // Zombie Villager Curing done sound
            this.world.playEvent(null, 1027, this.pos, 0);
        }
    }
}
