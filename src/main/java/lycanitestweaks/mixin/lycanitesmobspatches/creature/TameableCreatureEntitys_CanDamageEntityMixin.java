package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBeholder;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        EntityBeholder.class,
        EntityCacodemon.class,
})
public abstract class TameableCreatureEntitys_CanDamageEntityMixin extends TameableCreatureEntity {

    public TameableCreatureEntitys_CanDamageEntityMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "isDamageEntityApplicable",
            at = @At(value = "RETURN", ordinal = 0),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntitys_isDamageEntityApplicableTamed(boolean original, Entity entity){
        return this.getPlayerOwner() != ((TameableCreatureEntity) entity).getPlayerOwner() && super.isDamageEntityApplicable(entity);
    }
}
