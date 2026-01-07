package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAegis;
import lycanitestweaks.entity.goals.targeting.DefendTemporaryMasterGoal;
import lycanitestweaks.entity.goals.targeting.SupportDrowningGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAegis.class)
public abstract class EntityAegis_PickupGolemMixin extends TameableCreatureEntity {

    public EntityAegis_PickupGolemMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 3),
            index = 1
    )
    private EntityAIBase lycanitesTweaks_lycanitesMobsEntityAegis_initEntityAITemporaryMaster(EntityAIBase task){
        return new DefendTemporaryMasterGoal(this, EntityIronGolem.class);
    }

    @Inject(
            method = "initEntityAI",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAegis_initEntityAISupportSwimming(CallbackInfo ci){
        this.targetTasks.addTask(this.nextSpecialTargetIndex++, new SupportDrowningGoal(this).setTargetClass(EntityVillager.class).setChance(100));
        this.targetTasks.addTask(this.nextSpecialTargetIndex++, new SupportDrowningGoal(this).setTargetClass(EntityIronGolem.class).setChance(2).setClearTargetPath(true).setVerticalTargetingRange(0));
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAegis_onLivingUpdatePickupIronGolem(CallbackInfo ci){
        // Entity Pickup Update:
        if(!this.getEntityWorld().isRemote && this.getControllingPassenger() == null) {
            EntityLivingBase attackTarget = this.getMasterAttackTarget();
            if(attackTarget == null) attackTarget = this.getAttackTarget();
            if(!this.isTamed() && this.hasMaster()){
                if(this.hasAttackTarget() && this.getMasterTarget() instanceof EntityIronGolem){
                    EntityIronGolem pickupGolem = (EntityIronGolem) this.getMasterTarget();
                    if(this.getPickupEntity() == pickupGolem){
                        // Drop Golem On Target:
                        if(this.lycanitesTweaks$isInRangeForPickup(attackTarget) && this.posY > attackTarget.posY) {
                            pickupGolem.getNavigator().clearPath();
                            pickupGolem.setAttackTarget(attackTarget);
                            this.dropPickupEntity();
                        }
                    }
                    else if(!this.hasPickupEntity() && this.canPickupEntity(pickupGolem)){
                        if(this.lycanitesTweaks$isDistanceWorthPickup(pickupGolem, this.getAttackTarget())){
                            if(this.lycanitesTweaks$isInRangeForPickup(pickupGolem)) {
                                this.setPosition(pickupGolem.posX + 0.5F, pickupGolem.posY + pickupGolem.height, pickupGolem.posZ + 0.5F);
                                this.pickupEntity(pickupGolem);
                            }
                        }
                    }
                }
            }
            // Drop if for some reason carrying something
            else if(this.hasPickupEntity()){
                this.dropPickupEntity();
            }
        }
    }

    @Unique
    @Override
    public double getFlightOffset() {
        if(this.hasPickupEntity()) {
            return this.getPickupEntity().height;
        }
        return super.getFlightOffset();
    }

    // ==================================================
    //                      Attacks
    // ==================================================
    // ========== Melee Attack ==========
    @Unique
    @Override
    public boolean attackMelee(Entity target, double damageScale) {
        return !this.hasPickupEntity() && super.attackMelee(target, damageScale);
    }

    // Prevent getting stuck
    @Unique
    @Override
    public boolean canAttackWithPickup() {
        return true;
    }

    // Doesn't look the best but doesn't cripple speed
    @Unique
    @Override
    public double[] getPickupOffset(Entity entity) {
        if(entity != null) {
            return new double[]{0, -entity.height, 0};
        }
        return super.getPickupOffset(entity);
    }

    @Unique
    private boolean lycanitesTweaks$isInRangeForPickup(EntityLivingBase pickupTarget){
        return this.getDistanceSq(pickupTarget) <= this.getMeleeAttackRange(pickupTarget, 0);
    }

    @Unique
    private boolean lycanitesTweaks$isDistanceWorthPickup(EntityLivingBase pickupTarget, EntityLivingBase attackTarget){
        return this.getDistanceSq(attackTarget) >= this.getMeleeAttackRange(attackTarget, (pickupTarget.width + 1.0F) * (pickupTarget.width + 1.0F));
    }
}
