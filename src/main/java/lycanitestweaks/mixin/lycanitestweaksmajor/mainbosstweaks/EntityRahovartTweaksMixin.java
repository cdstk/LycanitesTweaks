package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.client.AssetManager;
import com.lycanitesmobs.core.block.BlockFireBase;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireOrb;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import lycanitestweaks.entity.goals.ExtendedGoalConditions;
import lycanitestweaks.entity.goals.actions.abilities.HealPortionWhenNoPlayersGoal;
import lycanitestweaks.entity.goals.actions.abilities.SummonLeveledMinionsGoal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
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

@Mixin(EntityRahovart.class)
public abstract class EntityRahovartTweaksMixin extends BaseCreatureEntity {

    // Changes with no configs
    /*
        Summon Wraith -> Not flight restricted, level match
        Summon Archvile -> Option Summon Royal Variant or three normal Minimum Phase 2
        Added -> Summon Ebon Cacodemon Minimum Phase 3

        IDEAS
        Barrier/Wall config spawn on phase start
        Barrier count config
        Cap Minion Count
     */

    @Unique
    public boolean lycanitesTweaks$cancelHellfireScaling = false;

    @Shadow(remap = false)
    public int hellfireEnergy;
    @Shadow(remap = false)
    public int hellfireWallTime;

    public EntityRahovartTweaksMixin(World world) {
        super(world);
    }


    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(stringValue = "hellfireball", ordinal = 0)
    )
    public String lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIReplaceProjectileAll(String constant){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.mainProjectileAll;
    }
    @ModifyConstant(
            method = "initEntityAI",
            constant = @Constant(stringValue = "hellfireball", ordinal = 1)
    )
    public String lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIReplaceProjectileTargeted(String constant){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.mainProjectileTarget;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 2),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIHeal(EntityAIBase task){
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.healPortionNoPlayers) return new HealPortionWhenNoPlayersGoal(this).setCheckRange(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.healPortionNoPlayersRange);
        else return task;
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 3),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIWraith(EntityAIBase task){
        return (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTeleportRange, 0).setMinionInfo("wraith").setSummonRate(200).setPerPlayer(true);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 6),
            index = 1
    )
    public EntityAIBase lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIArchville(EntityAIBase task){
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.royalArchvile)
            return (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTeleportRange, 0).setMinionInfo("archvile").setCustomName("Azazel").setSummonRate(600).setSummonCap(1).setVariantIndex(3).setSizeScale(2).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(1));
        return (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTeleportRange, 0).setMinionInfo("archvile").setSummonRate(200).setSummonCap(3).setPerPlayer(true).setSizeScale(2.0D).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(1));
    }

    @Inject(
            method = "initEntityAI",
            at = @At(value = "HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsEntityRahovart_initEntityAIAdditionalGoals(CallbackInfo ci){
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.cacodemonSummon)
            this.tasks.addTask(this.nextIdleGoalIndex, (new SummonLeveledMinionsGoal(this)).setBossMechanic(true, ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTeleportRange, 0).setMinionInfo("cacodemon").setCustomName("Pain Elemental").setSummonRate(600).setSummonCap(1).setVariantIndex(3).setSizeScale(2).setConditions((new ExtendedGoalConditions()).setMinimumBattlePhase(2)));
    }

    @ModifyArg(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;summonMinion(Lnet/minecraft/entity/EntityLivingBase;DD)V"),
            index = 0,
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesSetTemporaryMinions(EntityLivingBase creature){
        // Skip BaseCreatureCheck, we know they are
        ((BaseCreatureEntity)creature).enablePersistence();
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTemporaryDuration > 0)
            ((BaseCreatureEntity)creature).setTemporary(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionTemporaryDuration);
        return creature;
    }

    @ModifyArg(
            method = "updatePhases",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;summonMinion(Lnet/minecraft/entity/EntityLivingBase;DD)V"),
            index = 2,
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesMinionSpawnRange(double range){
        int min = ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionSpawnRangeMin;
        int max = ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.minionSpawnRangeMax;
        if(min >= max) return range;
        return (this.getRNG().nextFloat() * (max - min)) + min;
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 20, ordinal = 0),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesBelphEnergy(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyBelph;
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 20, ordinal = 1),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesBehemothEnergy(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyBehemoth;
    }

    @Inject(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;hellfireEnergy:I", ordinal = 2),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesSelfEnergyP1(CallbackInfo ci){
        if (this.updateTick % 20L == 0L && this.hellfireEnergy < 100)
            this.hellfireEnergy = Math.min(99, this.hellfireEnergy + ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergySelfP1);
    }

    @Inject(
            method = "updatePhases",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;hellfireEnergy:I", ordinal = 6),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesSelfEnergyP2(CallbackInfo ci){
        if (this.updateTick % 20L == 0L && this.hellfireEnergy < 100 && this.hellfireWallTime <= 0)
            this.hellfireEnergy = Math.min(99, this.hellfireEnergy + ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergySelfP2);
    }

    @ModifyConstant(
            method = "updatePhases",
            constant = @Constant(intValue = 5, ordinal = 2),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_updatePhasesSelfEnergyP3(int constant){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergySelfP3;
    }

    @ModifyExpressionValue(
            method = "hellfireWallAttack",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;hellfireWallTimeMax:I"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_hellfireWallAttackTimeMax(int original){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireWallTimeMax;
    }

    @Inject(
            method = "hellfireWallCleanup",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/projectile/EntityHellfireBarrier;setDead()V")
    )
    public void lycanitesTweaks_lycanitesMobsEntityRahovart_hellfireWallCleanupRefund(CallbackInfo ci){
        if(this.getBattlePhase() != 1)
            this.hellfireEnergy += ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireWallCleanupRefund;
    }

    @ModifyConstant(
            method = "onMinionDeath",
            constant = @Constant(intValue = 100),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_onMinionDeathBehemothBarrier(int constant, @Local(argsOnly = true) DamageSource damageSource){
        if(this.getBattlePhase() == 2 && damageSource.getTrueSource() == this) return 0;
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireBarrierBehemothDegrade;
    }

    @ModifyConstant(
            method = "onMinionDeath",
            constant = @Constant(intValue = 50),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityRahovart_onMinionDeathBelphBarrier(int constant, @Local(argsOnly = true) DamageSource damageSource){
        if(this.getBattlePhase() == 2 && damageSource.getTrueSource() == this) return 0;
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireBarrierBelphDegrade;
    }

    @Inject(
            method = "hellfireBarrierCleanup",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/projectile/EntityHellfireBarrier;setDead()V")
    )
    public void lycanitesTweaks_lycanitesMobsEntityRahovart_hellfireBarrierCleanupRefund(CallbackInfo ci){
        if(this.getBattlePhase() != 2)
            this.hellfireEnergy += ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireBarrierCleanupRefund;
    }

    // Stop the stupid float above fire and swim in water
    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityRahovart_onLivingUpdate(boolean original, @Local BlockPos arenaPos){
        return original || this.world.getBlockState(arenaPos).getBlock().isPassable(this.world, arenaPos);
    }

    @Unique
    @Override
    public boolean canEntityBeSeen(Entity target) {
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.playerXrayTarget && target instanceof EntityPlayer) return true;
        return super.canEntityBeSeen(target);
    }

    @Unique
    @Override
    public boolean doRangedDamage(Entity target, EntityThrowable projectile, float damage, boolean noPierce) {
        if(projectile instanceof EntityHellfireWall && ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttackFixedDamage) {
            lycanitesTweaks$cancelHellfireScaling = true;
        }
        boolean success = super.doRangedDamage(target, projectile, damage, noPierce);
        lycanitesTweaks$cancelHellfireScaling = false;
        return success;
    }

    @Unique
    @Override
    public int getLevel() {
        if(lycanitesTweaks$cancelHellfireScaling) return 1;
        return super.getLevel();
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

    @Unique
    @Override
    public void onTryToDamageMinion(EntityLivingBase minion, float damageAmount) {
        super.onTryToDamageMinion(minion, damageAmount);
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergySacrifice && minion instanceof BaseCreatureEntity) {
            int energy = lycanitesTweaks$getMinionSacrificeEnergy((BaseCreatureEntity) minion);
            if(energy >= 0) {
                this.hellfireEnergy += energy;
                EntityHellfireOrb hellfireOrb = new EntityHellfireOrb(this.getEntityWorld(), this);
                hellfireOrb.clientOnly = true;
                hellfireOrb.movement = true;
                hellfireOrb.projectileLife = 20 * 10;
                hellfireOrb.setLocationAndAngles(minion.posX + 0.5D, minion.posY + minion.getEyeHeight(), minion.posZ + 0.5D, 0, 0);
                hellfireOrb.shoot(0, 1, 0, 0.5F, 4);
                this.getEntityWorld().spawnEntity(hellfireOrb);
                minion.playSound(AssetManager.getSound("hellfirewave"), 0.5F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));

                // Amalgalich Suck
                DamageSource damageSource = new EntityDamageSource("mob", this);
                damageSource.setDamageIsAbsolute();
                damageSource.setDamageBypassesArmor();
                minion.attackEntityFrom(damageSource, 1000);
                minion.setDead();
            }
        }
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

    @Unique
    private int lycanitesTweaks$getMinionSacrificeEnergy(BaseCreatureEntity creature){
        if(this.hellfireWallTime > 0) return -1;
        if(creature.isRareVariant() || creature.isBoss()) return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyRare;
        else if(creature.creatureInfo.getName().equals("behemoth")) return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyMinionMain;
        else if(creature.creatureInfo.getName().equals("belph")) return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyMinionMain;
        else return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireEnergyMinionOther;
    }
}
