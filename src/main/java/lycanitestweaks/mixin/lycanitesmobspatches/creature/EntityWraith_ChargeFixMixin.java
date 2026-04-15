package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EntityWraith.class)
public abstract class EntityWraith_ChargeFixMixin extends TameableCreatureEntity {

    public EntityWraith_ChargeFixMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityWraith;getNearbyEntities(Ljava/lang/Class;Lcom/google/common/base/Predicate;D)Ljava/util/List;", remap = false)
    )
    private List<Entity> lycanitesTweaks_lycanitesMobsEntityWraith_onLivingUpdateChargeSelfFix(List<Entity> original){
        original.remove(this);
        return original;
    }
}
