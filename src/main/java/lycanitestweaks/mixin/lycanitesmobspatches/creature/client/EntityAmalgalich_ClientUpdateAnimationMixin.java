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
    private boolean lycanitesTweaks$previousAnimationState = false;

    public EntityAmalgalich_ClientUpdateAnimationMixin(World world) {
        super(world);
    }

    @WrapMethod(
            method = "extraAnimation01",
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityAmalgalich_extraAnimation01ClientRequestUpdate(Operation<Boolean> extraAnimation01){
        boolean animation = extraAnimation01.call();
        if(animation != this.lycanitesTweaks$previousAnimationState) {
            PacketHandler.instance.sendToServer(new PacketForceGoalAnimationUpdate(this, -1));
            this.lycanitesTweaks$previousAnimationState = animation;
        }
        return animation;
    }
}
