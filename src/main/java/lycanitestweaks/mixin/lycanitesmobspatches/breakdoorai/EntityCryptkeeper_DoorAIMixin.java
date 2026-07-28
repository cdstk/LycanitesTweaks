package lycanitestweaks.mixin.lycanitesmobspatches.breakdoorai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityCryptkeeper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityCryptkeeper.class
})
public abstract class EntityCryptkeeper_DoorAIMixin extends BaseCreatureEntity {

    public EntityCryptkeeper_DoorAIMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 2)
    )
    private void lycanitesTweaks_lycanitesMobsBaseEntityCryptkeeper_initEntityAIConfigDoorBreak(EntityAITasks instance, int priority, EntityAIBase task, Operation<Void> original){
        // no op, configurable
    }
}
