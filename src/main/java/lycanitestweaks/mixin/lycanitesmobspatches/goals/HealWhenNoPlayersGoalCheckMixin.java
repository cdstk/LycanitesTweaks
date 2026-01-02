package lycanitestweaks.mixin.lycanitesmobspatches.goals;

import com.google.common.base.Predicate;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.actions.abilities.HealWhenNoPlayersGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HealWhenNoPlayersGoal.class)
public abstract class HealWhenNoPlayersGoalCheckMixin extends EntityAIBase {

    @Shadow(remap = false)
    public boolean firstPlayerTargetCheck;

    @Shadow(remap = false)
    BaseCreatureEntity host;

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;updateTick:J", ordinal = 0, remap = false)
    )
    public long lycanitesTweaks_lycanitesMobsHealWhenNoPlayersGoal_updateTaskTickTrue(long original){
        return 200L;
    }

    @Inject(
            method = "updateTask",
            at = @At("HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsHealWhenNoPlayersGoal_updateTaskVarDoesCheck(CallbackInfo ci){
        this.firstPlayerTargetCheck = this.firstPlayerTargetCheck || (this.host.updateTick % 200L == 0L);
    }

    @ModifyArg(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getNearbyEntities(Ljava/lang/Class;Lcom/google/common/base/Predicate;D)Ljava/util/List;", remap = false),
            index = 1
    )
    private Predicate<Entity> lycanitesTweaks_lycanitesMobsHealWhenNoPlayersGoal_updateTaskPlayerPredicate(Predicate<Entity> predicate){
        return entity -> !((EntityPlayer)entity).isSpectator();
    }
}
