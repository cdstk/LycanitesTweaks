package lycanitestweaks.mixin.lycanitesmobspatches.breakdoorai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityCryptkeeper;
import com.lycanitesmobs.core.entity.creature.EntityGeist;
import com.lycanitesmobs.core.entity.creature.EntityGhoul;
import com.lycanitesmobs.core.entity.creature.EntityJabberwock;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityCryptkeeper.class,
        EntityGeist.class,
        EntityGhoul.class,
        EntityJabberwock.class,
})
public abstract class BaseCreatureEntity_VillageNavAIMixin extends BaseCreatureEntity {

    public BaseCreatureEntity_VillageNavAIMixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 0)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_initEntityAIConfigVillageNav(EntityAITasks instance, int priority, EntityAIBase task, Operation<Void> original){
        // no op, configurable
    }
}
