package lycanitestweaks.mixin.lycanitesmobspatches.goals;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.BaseGoal;
import com.lycanitesmobs.core.entity.goals.actions.abilities.EffectAuraGoal;
import lycanitestweaks.util.IAIGoal_AnimatedMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectAuraGoal.class)
public abstract class EffectAuraGoal_UpdateClientMixin extends BaseGoal implements IAIGoal_AnimatedMixin {

    @Shadow(remap = false) public int cooldownTime;

    @Unique
    private Integer lycanitesTweaks$cooldownFromNBT = null;

    public EffectAuraGoal_UpdateClientMixin(BaseCreatureEntity setHost) {
        super(setHost);
    }

    @Inject(
            method = "startExecuting",
            at = @At(value = "TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEffectAuraGoal_startExecutingNBTCooldown(CallbackInfo ci){
        if(this.lycanitesTweaks$cooldownFromNBT != null){
            this.cooldownTime = this.lycanitesTweaks$cooldownFromNBT;
            this.lycanitesTweaks$cooldownFromNBT = null;
        }
    }

    @Unique
    @Override
    public void lycanitesTweaks$setCooldownFromNBT(int cooldown){
        this.lycanitesTweaks$cooldownFromNBT = cooldown;
    }
}
