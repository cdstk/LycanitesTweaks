package lycanitestweaks.mixin.infernalmobs;

import atomicstryker.infernalmobs.common.mods.MM_Blastoff;
import atomicstryker.infernalmobs.common.mods.MM_Gravity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        MM_Blastoff.class,
        MM_Gravity.class
})
public abstract class MobModifiers_RepulsionMixin {

    @ModifyExpressionValue(
            method = "tryAbility",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;canEntityBeSeen(Lnet/minecraft/entity/Entity;)Z")
    )
    private boolean lycanitesTweaks_infernalMobsMobModifiers_tryAbilityRepulsionNullify(boolean canEntityBeSeen, @Local(name = "target") EntityLivingBase target){
        if(canEntityBeSeen) {
            PotionBase repulsion = ObjectManager.getEffect("repulsion");
            if(repulsion != null && target.isPotionActive(repulsion)) {
                canEntityBeSeen = false;
            }
        }
        if(canEntityBeSeen) {
            PotionBase weight = ObjectManager.getEffect("weight");
            if(weight != null && target.isPotionActive(weight)) {
                canEntityBeSeen = false;
            }
        }
        return canEntityBeSeen;
    }
}
