package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityAstaroth;
import com.lycanitesmobs.core.entity.goals.GoalConditions;
import com.lycanitesmobs.core.entity.goals.actions.abilities.FireProjectilesGoal;
import com.lycanitesmobs.core.entity.navigate.ArenaNode;
import lycanitestweaks.entity.goals.ExtendedGoalConditions;
import lycanitestweaks.entity.goals.actions.TeleportToHostGoal;
import lycanitestweaks.entity.goals.actions.abilities.HealPortionWhenNoPlayersGoal;
import lycanitestweaks.entity.goals.actions.abilities.SummonLeveledMinionsGoal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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

import java.util.List;

@Mixin(EntityAsmodeus.class)
public abstract class EntityAsmodeusTweaksMixin extends BaseCreatureEntity {

    // Changes with no configs
    /*
        Summon Grigori -> Not flight restricted, level match
        Summon Trite -> Summon more
        Replaced -> Grell method summon to SummonGoal, Asmodeus wasn't using the saved grells for anything
        Replaced -> Hell Shield invulnerability with interactive shield hp
        Added -> Void Trite Minimum Phase 2

        IDEAS
     */

    @Unique
    private boolean lycanitesTweaks$phaseTransition = true;

    @Shadow(remap = false)
    public List<EntityAstaroth> astarothMinions;
    @Shadow(remap = false)
    public ArenaNode currentArenaNode;

    public EntityAsmodeusTweaksMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 2),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAsmodeus_initEntityAIHeal(EntityAIBase task){
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.healPortionNoPlayers) return new HealPortionWhenNoPlayersGoal(this).setCheckRange(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.healPortionNoPlayersRange);
        else return task;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 3),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAsmodeus_initEntityAIGrigori(EntityAIBase task){
        return (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange, 0).setMinionInfo("grigori").setSummonRate(200).setPerPlayer(true);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 5),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityAsmodeus_initEntityAITriteNormal(EntityAIBase task){
        return (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange, 0).setMinionInfo("trite").setSummonRate(60).setSummonCap(6).setPerPlayer(true).setConditions((new GoalConditions()).setBattlePhase(0));
    }

    @Inject(
            method = "initEntityAI",
            at = @At(value = "HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsEntityAsmodeus_initEntityAIAdditionalGoals(CallbackInfo ci){
        this.tasks.addTask(this.nextIdleGoalIndex, (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange, 0).setMinionInfo("grell").setSummonRate(200).setSummonCap(6).setPerPlayer(true).setConditions(new ExtendedGoalConditions().setMinimumBattlePhase(1)));
        this.tasks.addTask(this.nextIdleGoalIndex, (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange, 0).setMinionInfo("trite").setSummonRate(40).setSummonCap(9).setPerPlayer(true).setSubSpeciesIndex(1).setConditions(new ExtendedGoalConditions().setMinimumBattlePhase(1)));
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.chupacabraSummon)
            this.tasks.addTask(this.nextIdleGoalIndex, (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange, 0).setMinionInfo("chupacabra").setCustomName("Anarchical Hound").setSummonRate(600).setSummonCap(1).setVariantIndex(3).setSizeScale(2).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(2)));
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.additionalProjectileAdd) {
            this.tasks.addTask(this.nextIdleGoalIndex, (new FireProjectilesGoal(this)).setProjectile(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.additionalProjectileAll).setFireRate(2560).setVelocity(0.8F).setScale(6.0F).setAllPlayers(true));
            this.tasks.addTask(this.nextIdleGoalIndex, (new FireProjectilesGoal(this)).setProjectile(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.additionalProjectileTarget).setFireRate(3840).setVelocity(0.8F).setScale(6.0F));
        }
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;devilstarStreamTimeMax:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesDevilstarUpTime(int original){
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilstarStreamTickLength;
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;devilstarStreamChargeMax:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesDevilstarCooldown(int original){
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilstarCooldown;
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;hellshieldAstarothRespawnTimeMax:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesAstarothsRespawnP2(int original){
        this.lycanitesTweaks$phaseTransition = false;
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsRespawnTimePhase2;
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;rebuildAstarothRespawnTimeMax:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesAstarothsRespawnP3(int original){
        this.lycanitesTweaks$phaseTransition = false;
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsRespawnTimePhase3;
    }

    // Flag before enter world for boss info
    // TODO Fix this in a better way
    @ModifyArg(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;summonMinion(Lnet/minecraft/entity/EntityLivingBase;DD)V"),
            index = 0,
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesAstarothBoss(EntityLivingBase minion){
        if(minion instanceof EntityAstaroth) {
            if (ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSpawnedAsBoss) ((EntityAstaroth) minion).spawnedAsBoss = true;
            else ((EntityAstaroth) minion).forceBossHealthBar = true;
        }
        return minion;
    }

    // Arachnotron
    @Inject(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseTwoMinion(CallbackInfo ci, @Local EntityAstaroth minion){
        minion.setCustomNameTag("Arachnotron");
        if(minion.getBossInfo() != null) minion.bossInfo.setName(new TextComponentString(minion.getName()));
        minion.setSizeScale(1.8);
        minion.enablePersistence();
        this.firstSpawn = false;
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsUseBossDamageLimit) {
            minion.damageLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
            minion.damageMax = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
        }
        else {
            minion.damageLimit = 0;
            minion.damageMax = 0;
        }
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsTeleportAdjacent && this.currentArenaNode != null){
            BlockPos randomPos = this.currentArenaNode.getRandomAdjacentNode().pos;
            minion.moveToBlockPosAndAngles(randomPos, world.rand.nextFloat() * 360.0F, 0.0F);
            minion.motionX = (this.world.rand.nextDouble() - (double)0.5F);
            minion.motionZ = (this.world.rand.nextDouble() - (double)0.5F);
        }
        else minion.tasks.addTask(minion.nextTravelGoalIndex++, new TeleportToHostGoal(minion).setLostDistance(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange));
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 0),

            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseTwoAstarothCap(boolean original){
        int playerCount = Math.max(this.playerTargets.size(), 1); // Doesn't want to be local captured
        return (this.astarothMinions.size() < playerCount * ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonCapPhase2);
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 2),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseTwoAstarothSummonAll(int original){
        if(this.lycanitesTweaks$phaseTransition && ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonAllPhase2) {
            return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonCapPhase2;
        }
        return 1;
    }

    // Asakku
    @Inject(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseThreeMinion(CallbackInfo ci, @Local EntityAstaroth minion){
        minion.setCustomNameTag("Asakku");
        if(minion.getBossInfo() != null) minion.bossInfo.setName(new TextComponentString(minion.getName()));
        minion.setSizeScale(2.5);
        minion.setSubspecies(1);
        minion.enablePersistence();
        this.firstSpawn = false;
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsUseBossDamageLimit) {
            minion.damageLimit = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
            minion.damageMax = BaseCreatureEntity.BOSS_DAMAGE_LIMIT;
        }
        else {
            minion.damageLimit = 0;
            minion.damageMax = 0;
        }
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsTeleportAdjacent && this.currentArenaNode != null){
            BlockPos randomPos = this.currentArenaNode.getRandomAdjacentNode().pos;
            minion.moveToBlockPosAndAngles(randomPos, world.rand.nextFloat() * 360.0F, 0.0F);
            minion.motionX = (this.world.rand.nextDouble() - (double)0.5F);
            minion.motionZ = (this.world.rand.nextDouble() - (double)0.5F);
        }
        else minion.tasks.addTask(minion.nextTravelGoalIndex++, new TeleportToHostGoal(minion).setLostDistance(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.minionTeleportRange));
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 4),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseThreeAstarothCap(int original){
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonCapPhase3;
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 0, ordinal = 1),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesPhaseThreeAstarothSummonAll(int original, @Local(ordinal = 0) int playerCount){
        if(this.lycanitesTweaks$phaseTransition && ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonAllPhase3) {
            return 1 - (ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.astarothsSummonCapPhase3 * playerCount);
        }
        return original;
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 6),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesRemoveGrell(int original){
        return 0;
    }

    @ModifyArg(
            method = "attackRanged",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;<init>(DDD)V"),
            index = 1
    )
    public double lycanitesTweaks_lycanitesMobsEntityAsmodeus_attackRangedDevilGatlingOffset(double yIn){
        return this.getEyeHeight() * 0.5F;
    }

    @WrapWithCondition(
            method = "attackRanged",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;attackHitscan(Lnet/minecraft/entity/Entity;D)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_attackRangedToggleHitscan(EntityAsmodeus instance, Entity entity, double v, @Local(argsOnly = true) Entity target, @Local(argsOnly = true) float range){
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.disableRangedHitscan) {
            super.attackRanged(target, range);
            return false;
        }
        return true;
    }

    @ModifyConstant(
            method = "attackDevilstar",
            constant = @Constant(stringValue = "devilstar"),
            remap = false
    )
    public String lycanitesTweaks_lycanitesMobsEntityAsmodeus_attackDevilstarSetProjectile(String constant){
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilstarProjectile;
    }

    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;getBattlePhase()I", ordinal = 0),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesAnyPhaseDevilstar(int original){
        if(this.updateTick % 20L != 0L && ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilstarStreamAllPhases) return 0;
        return original;
    }

    @Inject(
            method = "updatePhases",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesAnyPhaseRepair(CallbackInfo ci){
        if(this.updateTick % 20 == 0L){
            // Heal:
            float healthNormalLimit = 0F;
            if(this.getBattlePhase() == 0 && ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.repairAllPhases) healthNormalLimit = 1.0F;
            else if(this.getBattlePhase() == 1 && ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.repairAllPhases) healthNormalLimit = 0.6F;
            else if(this.getBattlePhase() == 2) healthNormalLimit = 0.2F;

            if(!this.astarothMinions.isEmpty()) {
                float healAmount = (ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.repairHealPortion) ?
                        (ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.repairHeal * this.getMaxHealth()) :
                        (ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.repairHeal);
                healAmount *= this.astarothMinions.size();
                if (((this.getHealth() + healAmount) / this.getMaxHealth()) <= healthNormalLimit) this.heal(healAmount);
            }
        }
    }

    // Remove for better limit calculations
    @ModifyExpressionValue(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_updatePhasesRemoveOriginalHealing(boolean original){
        return true;
    }

    @ModifyExpressionValue(
            method = "isBlocking",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;getBattlePhase()I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityAsmodeus_isBlockingAnyPhase(int original){
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.hellshieldAllPhases)
            return 1;
        return original;
    }

//    @ModifyExpressionValue(
//            method = "isBlocking",
//            at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"),
//            remap = false
//    )
//    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_isBlockingMinionAlive(boolean original){
//        // compared value is inverted due to !
//        if (!this.astarothMinions.isEmpty()) {
//            for(EntityAstaroth minion : this.astarothMinions){
//                if(minion.isEntityAlive()) return false;
//            }
//        }
//        return true;
//    }

    @ModifyExpressionValue(
            method = "isDamageTypeApplicable",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;isBlocking()Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_isDamageTypeApplicableWhileBlocking(boolean original, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float damage){
        return false;
    }

    // Stop the stupid float above fire and swim in water
    @ModifyExpressionValue(
            method = "updateArenaMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_updateArenaMovement(boolean original, @Local BlockPos arenaPos){
        return original || this.world.getBlockState(arenaPos).getBlock().isPassable(this.world, arenaPos);
    }

    @Unique
    @Override
    public boolean canEntityBeSeen(Entity target) {
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.playerXrayTarget && target instanceof EntityPlayer) return true;
        return super.canEntityBeSeen(target);
    }

    @Unique
    @Override
    public float getDamageModifier(DamageSource damageSrc) {
        if(this.isBlocking()) {
            return 1F - ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.hellshieldDamageReduction;
        }
        return super.getDamageModifier(damageSrc);
    }

    @Unique
    @Override
    public void setBattlePhase(int phase) {
        if (this.getBattlePhase() != phase) {
            this.lycanitesTweaks$phaseTransition = true;
        }
        super.setBattlePhase(phase);
    }

    // Attempt to remove strong minions on chunk reload
    @Unique
    @Override
    public void onMinionUpdate(EntityLivingBase minion, long tick) {
        if(minion instanceof BaseCreatureEntity){
            if(((BaseCreatureEntity) minion).isBoss() || ((BaseCreatureEntity) minion).isRareVariant()) ((BaseCreatureEntity) minion).setTemporary(20);
        }
        super.onMinionUpdate(minion, tick);
    }
}
