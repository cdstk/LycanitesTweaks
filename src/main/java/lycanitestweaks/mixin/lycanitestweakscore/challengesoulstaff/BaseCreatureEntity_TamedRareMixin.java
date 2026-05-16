package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_TamedRareMixin {

    // Changes for proper function and balance based on Temporary Summon from Staff

    @Shadow(remap = false) public abstract boolean isBoundPet();
    @Shadow(remap = false) public abstract boolean isTamed();

    @ModifyExpressionValue(
            method = "setVariant",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 2),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntity_setVariantRareBoundPenalty(boolean isRare){
        return isRare && !this.isBoundPet(); // If somehow bound, don't set Boss DPS Limit
    }

    @ModifyExpressionValue(
            method = "canAttackEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;isTamed()Z", ordinal = 1),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsTameableCreatureEntity_canAttackEntityBoss(boolean original, EntityLivingBase targetEntity) {
        // Allow other mobs to attack player rares
        if(targetEntity instanceof BaseCreatureEntity && ((BaseCreatureEntity) targetEntity).isTamed()) return true;
        return original;
    }

        @ModifyReturnValue(
            method = "canBeTargetedBy",
            at = @At(value = "RETURN", ordinal = 1),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canBeTargetedByTamed(boolean original, EntityLivingBase entity){
        // Allow other mobs to target player rares
        if(entity instanceof BaseCreatureEntity) return this.isTamed() != ((BaseCreatureEntity) entity).isTamed();
        return original;
    }

    @ModifyConstant(
            method = "damageEntity",
            constant = @Constant(floatValue = 0.25F)
    )
    public float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_damageEntityTamed(float constant){
        // Deal full damage to player rares
        return (this.isTamed()) ? 1F : constant;
    }
}
