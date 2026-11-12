package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.damagesource;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_OwnerCreditMixin {

    @Shadow(remap = false) protected boolean dropsRequirePlayerDamage;

    @Inject(
            method = "attackEntityFrom",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;dropsRequirePlayerDamage:Z", ordinal = 0, remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityFromTameCredit(DamageSource damageSrc, float damageAmount, CallbackInfoReturnable<Boolean> cir){
        if(damageSrc.getTrueSource() instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) damageSrc.getTrueSource();
            if(this.dropsRequirePlayerDamage && creature.getOwner() instanceof EntityPlayer) {
                this.dropsRequirePlayerDamage = !(creature instanceof TameableCreatureEntity) || !((TameableCreatureEntity) creature).fromSummoningPedestal;
            }
        }
    }
}
