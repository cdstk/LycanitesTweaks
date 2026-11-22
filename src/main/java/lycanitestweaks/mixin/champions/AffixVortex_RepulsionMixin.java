package lycanitestweaks.mixin.champions;

import c4.champions.common.affix.affix.AffixVortex;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AffixVortex.class)
public abstract class AffixVortex_RepulsionMixin {

    @ModifyExpressionValue(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lc4/champions/common/affix/affix/AffixVortex;isValidAffixTarget(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/entity/EntityLivingBase;Z)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_championsAffixVortex_onUpdateRepulsionNullify(boolean isValidAffixTarget, @Local EntityLivingBase target){
        if(isValidAffixTarget) {
            PotionBase repulsion = ObjectManager.getEffect("repulsion");
            if(repulsion != null && target.isPotionActive(repulsion)) {
                isValidAffixTarget = false;
            }
        }
        if(isValidAffixTarget) {
            PotionBase weight = ObjectManager.getEffect("weight");
            if(weight != null && target.isPotionActive(weight)) {
                isValidAffixTarget = false;
            }
        }
        return isValidAffixTarget;
    }
}
