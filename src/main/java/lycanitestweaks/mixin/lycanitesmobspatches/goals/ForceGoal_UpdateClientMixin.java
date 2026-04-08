package lycanitestweaks.mixin.lycanitesmobspatches.goals;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.actions.abilities.ForceGoal;
import lycanitestweaks.network.PacketForceGoalAnimationUpdate;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.IAIGoal_AnimatedMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForceGoal.class)
public abstract class ForceGoal_UpdateClientMixin implements IAIGoal_AnimatedMixin {

    @Shadow(remap = false) BaseCreatureEntity host;
    @Shadow(remap = false) public int abilityTime;
    @Shadow(remap = false) public int cooldownTime;

    @Unique
    private Integer lycanitesTweaks$cooldownFromNBT = null;

    @Inject(
            method = "startExecuting",
            at = @At(value = "TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsForceGoal_startExecutingNBTCooldown(CallbackInfo ci){
        if(this.lycanitesTweaks$cooldownFromNBT != null){
            this.cooldownTime = this.lycanitesTweaks$cooldownFromNBT;
            this.lycanitesTweaks$cooldownFromNBT = null;
        }
    }

    @Inject(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;playAttackSound()V", remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsForceGoal_updateTaskWindUpAnimation(CallbackInfo ci){
        PacketHandler.instance.sendToAllTracking(new PacketForceGoalAnimationUpdate(this.host, this.abilityTime), this.host);
    }

    @Inject(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/goals/actions/abilities/ForceGoal;abilityTime:I", ordinal = 1, remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsForceGoal_updateTaskStartAnimation(CallbackInfo ci){
        if (this.abilityTime == 0) PacketHandler.instance.sendToAllTracking(new PacketForceGoalAnimationUpdate(this.host, 0), this.host);
    }

    @Inject(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/goals/actions/abilities/ForceGoal;cooldownTime:I", ordinal = 2, remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsForceGoal_updateTaskEndAnimation(CallbackInfo ci){
        PacketHandler.instance.sendToAllTracking(new PacketForceGoalAnimationUpdate(this.host, 0), this.host);
    }

    @Unique
    @Override
    public void lycanitesTweaks$setCooldownFromNBT(int cooldown){
        this.lycanitesTweaks$cooldownFromNBT = cooldown;
    }
}
