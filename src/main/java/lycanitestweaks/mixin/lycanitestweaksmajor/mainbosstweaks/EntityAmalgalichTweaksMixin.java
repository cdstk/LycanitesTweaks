package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.block.BlockFireBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.creature.EntityEpion;
import com.lycanitesmobs.core.entity.creature.EntityGrue;
import com.lycanitesmobs.core.entity.goals.actions.AttackRangedGoal;
import com.lycanitesmobs.core.entity.goals.actions.abilities.EffectAuraGoal;
import com.lycanitesmobs.core.entity.goals.actions.abilities.ForceGoal;
import lycanitestweaks.entity.goals.ExtendedGoalConditions;
import lycanitestweaks.entity.goals.actions.abilities.HealPortionWhenNoPlayersGoal;
import lycanitestweaks.entity.goals.actions.abilities.SummonLeveledMinionsGoal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.potion.PotionConsumed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAmalgalich.class)
public abstract class EntityAmalgalichTweaksMixin extends BaseCreatureEntity {

    // Changes with no configs
    /*
        Summon Banshee -> Not flight restricted, level match
        Summon Repear -> All phases
        Summon Geist -> More and minimum phase 2
        Summon Epion -> Minimum phase 2 and option to spawn single Crimson

        IDEAS
     */

    @Shadow(remap = false)
    private ForceGoal consumptionGoalP0;
    @Shadow(remap = false)
    private ForceGoal consumptionGoalP2;

    @Shadow(remap = false)
    public abstract void onMinionDeath(EntityLivingBase minion, DamageSource damageSource);

    // Third Phase:
    @Unique
    public int lycanitesTweaks$lobDarklingsBurstTime = 0;
    @Unique
    public int lycanitesTweaks$lobDarklingsBurstCharge = 0;

    public EntityAmalgalichTweaksMixin(World world) {
        super(world);
    }

    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(intValue = 400)
    )
    public int lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAICooldownSet(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionGoalCooldown;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 8),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIConsptionAuraP0(EntityAIBase task) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionEffect && ForgeConfigHandler.server.effectsConfig.registerConsumed) {
            ((EffectAuraGoal) task).setEffect(PotionConsumed.INSTANCE);
        }
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionAllPhases) {
            ((EffectAuraGoal) task).setConditions(new ExtendedGoalConditions().setBattlePhase(-1));
        }
        return task;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 13),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIConsptionAuraP2(EntityAIBase task) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionEffect && ForgeConfigHandler.server.effectsConfig.registerConsumed) {
            ((EffectAuraGoal) task).setEffect(PotionConsumed.INSTANCE);
        }
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionAllPhases) {
            ((EffectAuraGoal) task).setConditions(new ExtendedGoalConditions().setBattlePhase(10));
        }
        return task;
    }

    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(stringValue = "spectralbolt", ordinal = 0)
    )
    public String lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIReplaceProjectileAll(String constant) {
        return ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.mainProjectileAll;
    }

    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(stringValue = "spectralbolt", ordinal = 1)
    )
    public String lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIReplaceProjectileTargeted(String constant) {
        return ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.mainProjectileTarget;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 2),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIHeal(EntityAIBase task) {
        if(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.healPortionNoPlayers)
            return new HealPortionWhenNoPlayersGoal(this)
                    .setCheckRange(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.healPortionNoPlayersRange);
        else return task;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 3),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIBanshee(EntityAIBase task) {
        return (new SummonLeveledMinionsGoal(this))
                .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                .setMinionInfo("banshee").setSummonRate(200).setPerPlayer(true);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 7),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIReaper(EntityAIBase task) {
        return (new SummonLeveledMinionsGoal(this))
                .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                .setMinionInfo("reaper").setSummonRate(100).setSummonCap(8).setPerPlayer(true);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 10),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIGheist(EntityAIBase task) {
        return (new SummonLeveledMinionsGoal(this))
                .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                .setMinionInfo("geist").setSummonRate(100).setSummonCap(8).setPerPlayer(true).setConditions(new ExtendedGoalConditions().setMinimumBattlePhase(1));
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 11),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIEpion(EntityAIBase task) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.crimsonEpion)
            return (new SummonLeveledMinionsGoal(this))
                    .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                    .setForceOnce(true).setMinionInfo("epion").setCustomName("Crimson Epion").setSummonRate(600).setSummonCap(1).setVariantIndex(3).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(1));
        return (new SummonLeveledMinionsGoal(this))
                .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                .setMinionInfo("epion").setSummonRate(100).setSummonCap(3).setPerPlayer(true).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(1));
    }

    @WrapWithCondition(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 14)
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAILobDarklingsModify(EntityAITasks instance, int idleGoalIndex, EntityAIBase entityAI){
        return !ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsModify;
    }

    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(stringValue = "lobdarklings")
    )
    public String lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAILobDarklingsReplacement(String constant){
        return ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsReplacement;
    }

    @Inject(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;initEntityAI()V")
    )
    public void lycanitesTweaks_lycanitesMobsEntityAmalgalich_initEntityAIAdditionalGoals(CallbackInfo ci, @Local int consumptionGoalCooldown) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.targetedAttack)
            this.tasks.addTask(this.nextCombatGoalIndex++, new AttackRangedGoal(this).setSpeed(1.0D).
                    setStaminaTime(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.targetedProjectileGoalCooldown).
                    setStaminaDrainRate(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.targetedProjectileStaminaDrainRate).
                    setRange(90.0F).setChaseTime(0).setCheckSight(false));
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.grueSummon)
            this.tasks.addTask(this.nextIdleGoalIndex, (new SummonLeveledMinionsGoal(this))
                    .setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.minionTeleportRange, 0)
                    .setForceOnce(true).setMinionInfo("grue").setCustomName("Night Shade").setSummonRate(600).setSummonCap(1).setVariantIndex(3).setSizeScale(2).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(2)));

        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionAllPhases) {
            this.consumptionGoalP0.setPhase(-1);
            this.consumptionGoalP2.setPhase(10);
        }
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("RETURN")
    )
    public void lycanitesTweaks_lycanitesMobsEntityAmalgalich_onLivingUpdateLobDarklingsModified(CallbackInfo ci){
        if(!this.getEntityWorld().isRemote) {
            // ===== Third Phase - Lob Darklings  =====
            if(this.getBattlePhase() == 2) {
                // Lob Darklings - Fire:
                if(this.lycanitesTweaks$lobDarklingsBurstTime > 0) {
                    this.lycanitesTweaks$lobDarklingsBurstTime--;
                    if(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsTickRate == 0){
                        for(int i = 0; i < ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsTickLength; i++){
                            this.lycanitesTweaks$lobDarklings();
                        }
                        lycanitesTweaks$lobDarklingsBurstTime = 0;
                    }
                    else if(this.updateTick % ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsTickRate == 0) {
                        this.lycanitesTweaks$lobDarklings();
                    }
                }
                // Lob Darklings - Recharge:
                else if(this.lycanitesTweaks$lobDarklingsBurstCharge > 0) {
                    this.lycanitesTweaks$lobDarklingsBurstCharge--;
                }
                // Lob Darklings - Charged
                else {
                    this.lycanitesTweaks$lobDarklingsBurstCharge = ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsCooldown;
                    this.lycanitesTweaks$lobDarklingsBurstTime = ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsTickLength;
                }
            }
        }
    }


    @Inject(
            method = "updateBattlePhase",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityAmalgalich_updateBattlePhaseKillBossMinions(CallbackInfo ci) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionAllPhases)
            return;
        if (this.getBattlePhase() < 1) {
            this.getMinions(EntityEpion.class)
                    .forEach(minion -> {
                        EntityEpion epion = (EntityEpion) minion;
                        if (epion.isRareVariant()) {
                            if (epion.getHealth() > epion.getMaxHealth() * 0.1F) {
                                epion.setHealth(epion.getHealth() - (epion.getMaxHealth() * 0.1F));
                            } else {
                                this.onTryToDamageMinion(epion, -1);
                                epion.setDead();
                            }
                        }
                    });
            if (this.getBattlePhase() < 2) {
                this.getMinions(EntityGrue.class)
                        .forEach(minion -> {
                            EntityGrue grue = (EntityGrue) minion;
                            if (grue.isRareVariant()) {
                                if (grue.getHealth() > grue.getMaxHealth() * 0.1F) {
                                    grue.setHealth(grue.getHealth() - (grue.getMaxHealth() * 0.1F));
                                } else {
                                    this.onTryToDamageMinion(grue, -1);
                                    grue.setDead();
                                }
                            }
                        });
            }
        }
    }

    // Don't play attack sound, same sfx consumption lol
    // Based on Asmodeus
    @Inject(
            method = "attackRanged",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityAmalgalich_attackRanged(Entity target, float range, CallbackInfo ci) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.targetedAttack) {
            for (int i = 0; i < 5; i++) {
                this.fireProjectile(
                        ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.targetedProjectile,
                        target, range, 0.0F,
                        new Vec3d(0.0F, (double) this.getEyeHeight() * 0.7D, 0.0F),
                        0.1F, 4.0F, 2.0F);
            }
            if (!this.isBlocking() || this.canAttackWhileBlocking()) {
                this.triggerAttackCooldown();
            }
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "extraAnimation01",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAmalgalich;getBattlePhase()I", ordinal = 0),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAmalgalich_extraAnimation01(int original) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionAllPhases) return 0;
        return original;
    }

    @ModifyConstant(
            method = "onMinionDeath",
            constant = @Constant(intValue = 10),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAmalgalich_onMinionDeath(int constant, @Local(argsOnly = true) EntityLivingBase minion, @Local(argsOnly = true) DamageSource damageSource) {
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionKillEpionChance < 1.0F) {
            if (damageSource.getTrueSource() instanceof EntityAmalgalich && this.getRNG().nextFloat() < ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionKillEpionChance)
                return 0;
        }
        return ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.customEpionExtinguishWidth;
    }

    @ModifyConstant(
            method = "onTryToDamageMinion",
            constant = @Constant(floatValue = 1000F),
            remap = false
    )
    public float lycanitesTweaks_lycanitesMobsEntityAmalgalich_onTryToDamageMinionAlways(float damageLimit){
        return 1F;
    }

    @Inject(
            method = "onTryToDamageMinion",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;setDead()V", remap = true),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityAmalgalich_onTryToDamageMinionEpionDeath(EntityLivingBase minion, float damageAmount, CallbackInfo ci){
        if(minion.isEntityAlive()) this.onMinionDeath(minion, new EntityDamageSource("mob", this));
    }

    @ModifyConstant(
            method = "onTryToDamageMinion",
            constant = @Constant(floatValue = 25F),
            remap = false
    )
    public float lycanitesTweaks_lycanitesMobsEntityAmalgalich_onTryToDamageMinionHealAmount(float healAmount){
        if (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionKillHealPortion) {
            return (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionKillHeal * this.getMaxHealth());
        } else
            return (ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.consumptionKillHeal);
    }

    // Stop the stupid float above fire and swim in water
    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAmalgalich_onLivingUpdate(boolean original, @Local BlockPos arenaPos) {
        return original || this.world.getBlockState(arenaPos).getBlock().isPassable(this.world, arenaPos);
    }

    @Unique
    @Override
    public boolean canEntityBeSeen(Entity target) {
        if(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.playerXrayTarget && target instanceof EntityPlayer) return true;
        return super.canEntityBeSeen(target);
    }

    // Thanks Iqury
    @Unique
    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (!this.getEntityWorld().isRemote) {
            int extinguishWidth = 8;
            int extinguishHeight = 2;
            for(int x = (int)this.posX - extinguishWidth; x <= (int)this.posX + extinguishWidth; ++x) {
                for(int y = (int)this.posY - extinguishHeight; y <= (int)this.posY + 2; ++y) {
                    for(int z = (int)this.posZ - extinguishWidth; z <= (int)this.posZ + extinguishWidth; ++z) {
                        Block block = this.getEntityWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                        if (block instanceof BlockFireBase || block instanceof BlockFire) {
                            BlockPos placePos = new BlockPos(x, y, z);
                            this.getEntityWorld().setBlockToAir(placePos);
                        }
                    }
                }
            }
        }
    }

    // Attempt to remove strong minions on chunk reload
    @Unique
    @Override
    public void onMinionUpdate(EntityLivingBase minion, long tick) {
        super.onMinionUpdate(minion, tick);
        if(minion instanceof BaseCreatureEntity && !ForgeConfigHandler.mixinPatchesConfig.minionNBTSaving){
            BaseCreatureEntity creature = (BaseCreatureEntity) minion;
            if((creature.isBoss() || creature.isRareVariant())) {
                creature.setTemporary(20);
            }
        }
    }

    // ========== Lob Darklings ==========
    @Unique
    public void lycanitesTweaks$lobDarklings() {
        float angle = (this.getAttackTarget() != null)
                ? (this.world.rand.nextFloat() - 0.5F) * (float)Math.toRadians(ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsArc)
                : this.world.rand.nextFloat() * 360F;

        this.fireProjectile(
                ForgeConfigHandler.majorFeaturesConfig.amalgalichConfig.lobDarklingsReplacement,
                this.getAttackTarget(),
                this.world.rand.nextFloat() * 10F,
                angle,
                Vec3d.ZERO,
                0.8F,
                2F,
                0F
        );
    }
}
