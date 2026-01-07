package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAbtu;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAbtu.class)
public abstract class EntityAbtu_SummonTrackedMixin extends TameableCreatureEntity {

    @Shadow(remap = false) int swarmLimit;

    public EntityAbtu_SummonTrackedMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "allyUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAbtu;nearbyCreatureCount(Ljava/lang/Class;D)I"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsEntityAbtu_allyUpdateCountMinions(int nearbyCreatureCount){
        if(this.isMinion()) return this.swarmLimit;
        return this.getMinions(this.getClass()).size();
    }
}
