package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityIgnibus.class)
public abstract class EntityIgnibus_ProjOffsetFixMixin {

    @ModifyConstant(
            method = "mountAbility",
            constant = @Constant(doubleValue = 10.0D),
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsEntityIgnibus_mountAbilityShootFromPlayer(double constant){
        return 1.0D;
    }
}
