package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.goals.actions.abilities.EffectAuraGoal;
import com.lycanitesmobs.core.entity.goals.actions.abilities.ForceGoal;
import lycanitestweaks.network.PacketForceGoalAnimationUpdate;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.IAIGoal_AnimatedMixin;
import lycanitestweaks.util.IBaseCreatureEntity_AnimatedGoalMixin;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityAmalgalich.class)
public abstract class EntityAmalgalich_UpdateClientAnimatedGoalMixin extends BaseCreatureEntity implements IBaseCreatureEntity_AnimatedGoalMixin {

    @Shadow(remap = false) private ForceGoal consumptionGoalP0;
    @Shadow(remap = false) private ForceGoal consumptionGoalP2;
    @Shadow(remap = false) private int consumptionDuration;
    @Shadow(remap = false) private int consumptionWindUp;
    @Shadow(remap = false) private int consumptionAnimationTime;

    @Unique
    private static final String COOLDOWN_TIME_NBT = "ConsumptionCooldown";

    @Unique
    private EffectAuraGoal lycanitesTweaks$consumptionAuraP0;
    @Unique
    private EffectAuraGoal lycanitesTweaks$consumptionAuraP2;

    public EntityAmalgalich_UpdateClientAnimatedGoalMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 8),
            index = 1
    )
    private EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIConsumeRef0(EntityAIBase task){
        if(task instanceof EffectAuraGoal) {
            this.lycanitesTweaks$consumptionAuraP0 = (EffectAuraGoal) task;
        }
        return task;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 13),
            index = 1
    )
    private EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIConsumeRef2(EntityAIBase task){
        if(task instanceof EffectAuraGoal) {
            this.lycanitesTweaks$consumptionAuraP2 = (EffectAuraGoal) task;
        }
        return task;
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAmalgalich_readFromNBTConsumeCooldown(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        if (nbtTagCompound.hasKey(COOLDOWN_TIME_NBT)) {
            int cooldown = nbtTagCompound.getInteger(COOLDOWN_TIME_NBT);
            ForceGoal consumption = null;
            EffectAuraGoal aura = null;
            if(this.consumptionGoalP0 != null && this.consumptionGoalP0.shouldExecute()) consumption = this.consumptionGoalP0;
            else if(this.consumptionGoalP2 != null && this.consumptionGoalP2.shouldExecute()) consumption = this.consumptionGoalP2;
            if(this.lycanitesTweaks$consumptionAuraP0 != null && this.lycanitesTweaks$consumptionAuraP0.shouldExecute()) aura = this.lycanitesTweaks$consumptionAuraP0;
            else if(this.lycanitesTweaks$consumptionAuraP2 != null && this.lycanitesTweaks$consumptionAuraP2.shouldExecute()) aura = this.lycanitesTweaks$consumptionAuraP2;

            if(consumption instanceof IAIGoal_AnimatedMixin) ((IAIGoal_AnimatedMixin) consumption).lycanitesTweaks$setCooldownFromNBT(cooldown);
            if(aura instanceof IAIGoal_AnimatedMixin) ((IAIGoal_AnimatedMixin) aura).lycanitesTweaks$setCooldownFromNBT(cooldown + this.consumptionWindUp);
        }
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAmalgalich_writeEntityToNBTConsumeCooldown(NBTTagCompound nbtTagCompound, CallbackInfo ci) {
        ForceGoal consumption = null;
        if(this.consumptionGoalP0 != null && this.consumptionGoalP0.shouldExecute()) consumption = this.consumptionGoalP0;
        else if(this.consumptionGoalP2 != null && this.consumptionGoalP2.shouldExecute()) consumption = this.consumptionGoalP2;

        int savedCooldown = consumption == null ? 400 : consumption.cooldownTime;
        savedCooldown = Math.max(savedCooldown, 0);
        nbtTagCompound.setInteger(COOLDOWN_TIME_NBT, savedCooldown);
    }

    @Unique
    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);

        ForceGoal consumption = null;
        if(this.consumptionGoalP0 != null && this.consumptionGoalP0.shouldExecute()) consumption = this.consumptionGoalP0;
        else if(this.consumptionGoalP2 != null && this.consumptionGoalP2.shouldExecute()) consumption = this.consumptionGoalP2;

        if(consumption != null) PacketHandler.instance.sendTo(new PacketForceGoalAnimationUpdate(this, consumption.abilityTime), player);
    }

    @Unique
    @Override
    public void lycanitesTweaks$updateClientForceGoalTime(int abilityTime){
        this.extraAnimation01 = abilityTime != 0;
        this.consumptionAnimationTime = this.consumptionDuration - abilityTime;
    }
}
