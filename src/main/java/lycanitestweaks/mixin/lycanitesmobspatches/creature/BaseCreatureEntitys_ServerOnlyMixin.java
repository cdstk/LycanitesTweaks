package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityAsmodeus.class,
        EntityAmalgalich.class,
        EntityRahovart.class
})
public abstract class BaseCreatureEntitys_ServerOnlyMixin {

    @ModifyExpressionValue(
            method = "<init>",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;BOSS_DAMAGE_LIMIT:I"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsBaseCreatureEntitys_initSided(int bossDamageLimit, World world){
        return world.isRemote ? 0 : bossDamageLimit;
    }
}
