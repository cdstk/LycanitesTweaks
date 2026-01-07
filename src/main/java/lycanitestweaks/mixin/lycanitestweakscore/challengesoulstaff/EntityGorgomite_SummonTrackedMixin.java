package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityGorgomite;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityGorgomite.class)
public abstract class EntityGorgomite_SummonTrackedMixin extends BaseCreatureEntity {

    @Shadow(remap = false) private int swarmLimit;

    public EntityGorgomite_SummonTrackedMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "allyUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityGorgomite;countAllies(D)I"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsEntityGorgomite_allyUpdateCountMinions(int nearbyCreatureCount){
        if(this.isMinion()) return this.swarmLimit;
        return this.getMinions(this.getClass()).size();
    }
}
