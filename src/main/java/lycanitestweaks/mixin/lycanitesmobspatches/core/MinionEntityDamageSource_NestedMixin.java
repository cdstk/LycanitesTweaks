package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.damagesources.MinionEntityDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MinionEntityDamageSource.class)
public abstract class MinionEntityDamageSource_NestedMixin extends EntityDamageSource {

    public MinionEntityDamageSource_NestedMixin(String damageTypeIn, @Nullable Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
    }

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsMinionEntityDamageSource_initCopyNested(DamageSource minionDamageSource, Entity owner, boolean playerCredit, CallbackInfo ci){
        if(minionDamageSource.canHarmInCreative())
            this.setDamageAllowedInCreativeMode();
        if(minionDamageSource.isProjectile())
            this.setProjectile();
        if(minionDamageSource.isFireDamage())
            this.setFireDamage();
        if(minionDamageSource.isExplosion())
            this.setExplosion();
        if(minionDamageSource.isDifficultyScaled())
            this.setDifficultyScaled();
        if(minionDamageSource instanceof EntityDamageSource && ((EntityDamageSource) minionDamageSource).getIsThornsDamage())
            this.setIsThornsDamage();
    }
}
