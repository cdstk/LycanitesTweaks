package lycanitestweaks.mixin.srparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityRanracAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityRanrac;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectilePullball;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityProjectilePullball.class)
public abstract class EntityProjectilePullball_RepulsionMixin {

    @ModifyExpressionValue(
            method = "pRanrac",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isEntityAlive()Z")
    )
    private boolean lycanitesTweaks_srparasitesEntityProjectilePullball_pRanracRepulsion(boolean isEntityAlive, @Local EntityLivingBase mob, @Local EntityRanrac ra){
        PotionBase repulsion = ObjectManager.getEffect("repulsion");
        if(repulsion != null) {
            return isEntityAlive && !ra.getAttackTarget().isPotionActive(repulsion);
        }
        return isEntityAlive;
    }

    @ModifyExpressionValue(
            method = "aRanrac",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isEntityAlive()Z")
    )
    private boolean lycanitesTweaks_srparasitesEntityProjectilePullball_aRanracRepulsion(boolean isEntityAlive, @Local EntityLivingBase mob, @Local EntityRanracAdapted ra){
        PotionBase repulsion = ObjectManager.getEffect("repulsion");
        if(repulsion != null) {
            return isEntityAlive && !ra.getAttackTarget().isPotionActive(repulsion);
        }
        return isEntityAlive;
    }
}
