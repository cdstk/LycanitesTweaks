package lycanitestweaks.mixin.vanilla;

import lycanitestweaks.util.IDamageSource_UnsetPropertiesMixin;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DamageSource.class)
public abstract class DamageSource_UnsetPropertiesMixin implements IDamageSource_UnsetPropertiesMixin {

    @Shadow private boolean isUnblockable;
    @Shadow private boolean isDamageAllowedInCreativeMode;
    @Shadow private boolean damageIsAbsolute;
    @Shadow private float hungerDamage;
    @Shadow private boolean fireDamage;
    @Shadow private boolean projectile;
    @Shadow private boolean difficultyScaled;
    @Shadow private boolean magicDamage;
    @Shadow private boolean explosion;

    @Override
    public DamageSource lycanitesTweaks$unsetProjectile() {
        this.projectile = false;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetExplosion() {
        this.explosion = false;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetDamageBypassesArmor()  {
        this.isUnblockable = false;
        if(!this.damageIsAbsolute)
            this.hungerDamage = 0.1F;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetDamageAllowedInCreativeMode() {
        this.isDamageAllowedInCreativeMode = false;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetDamageIsAbsolute() {
        this.damageIsAbsolute = false;
        if(!this.isUnblockable)
            this.hungerDamage = 0.1F;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetFireDamage() {
        this.fireDamage = false;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetDifficultyScaled() {
        this.difficultyScaled = false;
        return (DamageSource) (Object) this;
    }

    @Override
    public DamageSource lycanitesTweaks$unsetMagicDamage() {
        this.magicDamage = false;
        return (DamageSource) (Object) this;
    }
}
