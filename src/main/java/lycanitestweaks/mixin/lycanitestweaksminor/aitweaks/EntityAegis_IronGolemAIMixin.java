package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAegis;
import com.lycanitesmobs.core.entity.goals.actions.MoveVillageGoal;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAegis.class)
public abstract class EntityAegis_IronGolemAIMixin extends TameableCreatureEntity {

    @Unique
    private int lycanitesTweaks$homeCheckTimer;
    @Unique
    private Village lycanitesTweaks$village;

    public EntityAegis_IronGolemAIMixin(World world) {
        super(world);
    }

    @Inject(
            method = "initEntityAI",
            at = @At("HEAD")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAegis_initEntityAIPatrolVillage(CallbackInfo ci){
        this.tasks.addTask(this.nextTravelGoalIndex++, new MoveVillageGoal(this).setSpeed(ForgeConfigHandler.minorFeaturesConfig.aegisVillagePatrolSpeed).setNocturnal(true));
    }

    @ModifyExpressionValue(
            method = "shouldCreatureGroupHunt",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;shouldCreatureGroupHunt(Lnet/minecraft/entity/EntityLivingBase;)Z"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityAegis_shouldCreatureGroupHuntIronGolemTargets(boolean shouldCreatureGroupHunt, EntityLivingBase target){
        if(target instanceof EntityTameable && !((EntityTameable) target).isTamed()) {
            return false;
        }
        return shouldCreatureGroupHunt || IMob.VISIBLE_MOB_SELECTOR.apply(target) && !(target instanceof EntityCreeper);
    }

    @Unique
    @Override
    public boolean canMove() {
        return true;
    }

    @Unique
    @Override
    protected void updateAITasks(){
        if (!this.isTamed() && --this.lycanitesTweaks$homeCheckTimer <= 0) {
            this.lycanitesTweaks$homeCheckTimer = 70 + this.rand.nextInt(50);
            this.lycanitesTweaks$village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);

            if (this.lycanitesTweaks$village == null) {
                this.detachHome();
            }
            else {
                BlockPos blockpos = this.lycanitesTweaks$village.getCenter();
                this.setHome(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.lycanitesTweaks$village.getVillageRadius() * 0.6F);
            }
        }

        super.updateAITasks();
    }

    @Unique
    @Override
    public void onDeath(DamageSource cause){
        if (!this.isTamed() && this.attackingPlayer != null && this.lycanitesTweaks$village != null) {
            this.lycanitesTweaks$village.modifyPlayerReputation(this.attackingPlayer.getUniqueID(), -5);
        }
        super.onDeath(cause);
    }
}
