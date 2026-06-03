package lycanitestweaks.mixin.lycanitesmobspatches.creature.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import lycanitestweaks.network.PacketForceGoalAnimationUpdate;
import lycanitestweaks.network.PacketHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = EntityAmalgalich.class)
public abstract class EntityAmalgalich_ClientUpdateAnimationMixin extends BaseCreatureEntity {

    @Unique
    private float lycanitesTweaks$previousAnimationState = 0;

    public EntityAmalgalich_ClientUpdateAnimationMixin(World world) {
        super(world);
    }

    @WrapMethod(
            method = "getConsumptionAnimation",
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsEntityAmalgalich_getConsumptionAnimationClientRequestUpdate(Operation<Float> consumptionAnimation){
        float progress = consumptionAnimation.call();

        // SUCK
        if(progress == 1F) {
            if(progress != this.lycanitesTweaks$previousAnimationState) {
                PacketHandler.instance.sendToServer(new PacketForceGoalAnimationUpdate(this));
                this.lycanitesTweaks$previousAnimationState = progress;
            }
        }
        // Idle Standing
        else if(progress == 0F) {
            if(progress != this.lycanitesTweaks$previousAnimationState) {
                PacketHandler.instance.sendToServer(new PacketForceGoalAnimationUpdate(this));
                this.lycanitesTweaks$previousAnimationState = progress;
            }
        }
        // Crouching
        else if(progress > 0) {
            if(this.lycanitesTweaks$previousAnimationState != 0.5F) {
                PacketHandler.instance.sendToServer(new PacketForceGoalAnimationUpdate(this));
                this.lycanitesTweaks$previousAnimationState = 0.5F;
            }
        }

        return progress;
    }
}
