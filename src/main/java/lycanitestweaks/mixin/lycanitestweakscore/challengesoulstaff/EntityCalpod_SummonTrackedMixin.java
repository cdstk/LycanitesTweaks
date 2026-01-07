package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityCalpod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCalpod.class)
public abstract class EntityCalpod_SummonTrackedMixin extends BaseCreatureEntity {

    @Shadow(remap = false) private int swarmLimit;

    public EntityCalpod_SummonTrackedMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "allyUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityCalpod;countAllies(D)I"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsEntityCalpod_allyUpdateCountMinions(int nearbyCreatureCount){
        if(this.isMinion()) return this.swarmLimit;
        return this.getMinions(this.getClass()).size();
    }
}
