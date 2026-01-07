package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.info.CreatureManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCacodemon.class)
public abstract class EntityCacodemon_SummonTrackedMixin extends RideableCreatureEntity {

    public EntityCacodemon_SummonTrackedMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "allyUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityCacodemon;nearbyCreatureCount(Ljava/lang/Class;D)I"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsEntityCacodemon_allyUpdateCountMinions(int nearbyCreatureCount){
        return this.getMinions(CreatureManager.getInstance().getCreature("wraith").getEntityClass()).size();
    }
}
