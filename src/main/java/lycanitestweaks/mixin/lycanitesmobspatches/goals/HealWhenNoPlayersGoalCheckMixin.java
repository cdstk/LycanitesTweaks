package lycanitestweaks.mixin.lycanitesmobspatches.goals;

import com.google.common.base.Predicate;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.goals.actions.abilities.HealWhenNoPlayersGoal;
import com.lycanitesmobs.core.entity.navigate.ArenaNode;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HealWhenNoPlayersGoal.class)
public abstract class HealWhenNoPlayersGoalCheckMixin extends EntityAIBase {

    @Shadow(remap = false)
    public boolean firstPlayerTargetCheck;

    @Shadow(remap = false)
    BaseCreatureEntity host;

    @Unique
    private ArenaNode lycanitesTweaks$previousArenaNode = null;
    @Unique
    private int lycanitesTweaks$previousNodeTimer = 0;

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

        if(ForgeConfigHandler.mixinPatchesConfig.healGoalAsmodeusJump && this.host instanceof EntityAsmodeus) {
            EntityAsmodeus asmodeus = (EntityAsmodeus) this.host;
            // Set timer
            if(asmodeus.arenaJumpingTime == asmodeus.arenaJumpingTimeMax) {
                this.lycanitesTweaks$previousNodeTimer = asmodeus.arenaJumpingTimeMax;
            }
            // Set as "previous"
            else if(this.lycanitesTweaks$previousNodeTimer <= 0) {
                this.lycanitesTweaks$previousArenaNode = asmodeus.currentArenaNode;
            }
            // Tick timer
            else {
                this.lycanitesTweaks$previousNodeTimer--;
            }
        }
    }

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getNearbyEntities(Ljava/lang/Class;Lcom/google/common/base/Predicate;D)Ljava/util/List;", remap = false)
    )
    private List<EntityPlayer> lycanitesTweaks_lycanitesMobsHealWhenNoPlayersGoal_updateTaskAddNearPrevNode(List<EntityPlayer> nearbyPlayers){
        if(this.lycanitesTweaks$previousNodeTimer > 0) {
            if(this.lycanitesTweaks$previousArenaNode != null && this.lycanitesTweaks$previousArenaNode.pos != null) {
                List<EntityPlayer> nearNode = this.host.world.getPlayers(EntityPlayer.class, EntitySelectors.CAN_AI_TARGET);
                nearNode.removeIf(player -> {
                    if(MathHelper.sqrt(player.getDistanceSq(this.lycanitesTweaks$previousArenaNode.pos)) > 64) {
                        return true;
                    }
                    if(!player.canEntityBeSeen(this.host)) {
                        return true;
                    }
                    return false;
                });
                nearbyPlayers.addAll(nearNode);
            }
        }
        return nearbyPlayers;
    }

    @ModifyArg(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getNearbyEntities(Ljava/lang/Class;Lcom/google/common/base/Predicate;D)Ljava/util/List;", remap = false),
            index = 1
    )
    private Predicate<Entity> lycanitesTweaks_lycanitesMobsHealWhenNoPlayersGoal_updateTaskPlayerPredicate(Predicate<Entity> predicate){
        return entity -> {
            if(((EntityPlayer)entity).isSpectator() && ForgeConfigHandler.mixinPatchesConfig.healGoalIgnoreSpectators)
                return false;
            if(!((EntityPlayer)entity).isCreative() && !((EntityPlayer) entity).canEntityBeSeen(this.host) && ForgeConfigHandler.mixinPatchesConfig.healGoalIgnoreWalls)
                return entity.posY <= this.host.posY + this.host.height && entity.posY >= this.host.posY;
            return true;
        };
    }
}
