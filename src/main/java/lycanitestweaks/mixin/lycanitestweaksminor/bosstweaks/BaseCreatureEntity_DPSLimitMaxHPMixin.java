package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.CreatureStats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_DPSLimitMaxHPMixin extends EntityLiving {

    // "Boss DPS Limit Scale With Max HP" in Minor Features Config

    @Shadow(remap = false)
    public CreatureStats creatureStats;
    @Shadow(remap = false)
    public int damageMax;
    @Shadow(remap = false)
    public float damageLimit;

    @Unique
    private boolean lycanitesTweaks$testingStartingLevel = false;

    public BaseCreatureEntity_DPSLimitMaxHPMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(
            method = "getLevel",
            at = @At("RETURN"),
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getLevelOneMaxHP(int level){
        return this.lycanitesTweaks$testingStartingLevel ? 1 : level;
    }

    @Inject(
            method = "isEntityInvulnerable",
            at = @At("HEAD")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_isEntityInvulnerableDPSLimitScaled(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if(this.damageLimit != 0F) {
            this.lycanitesTweaks$testingStartingLevel = true;
            double currentMax = this.getMaxHealth();
            double startingMax = this.creatureStats.getHealth();
            this.lycanitesTweaks$testingStartingLevel = false;

            double dpsLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT * currentMax / startingMax;
            if (dpsLimit > BaseCreatureEntity.BOSS_DAMAGE_LIMIT) {
                this.damageMax = (int) dpsLimit;
                this.damageLimit = (float) dpsLimit;
            }
        }
    }
}
