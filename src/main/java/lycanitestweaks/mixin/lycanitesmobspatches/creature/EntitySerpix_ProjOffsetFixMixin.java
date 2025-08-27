package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.creature.EntitySerpix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntitySerpix.class)
public abstract class EntitySerpix_ProjOffsetFixMixin {

    @ModifyConstant(
            method = "attackRanged",
            constant = @Constant(doubleValue = 10.0D),
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsEntityIgnibus_mountAbilityShootFromPlayer(double constant){
        return 1.0D;
    }
}
