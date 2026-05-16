package lycanitestweaks.entity.goals.actions;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// Based on com/lycanitesmobs/core/entity/goals/actions/FollowOwnerGoal.java
public class TeleportToHostGoal extends EntityAIBase {

    BaseCreatureEntity host;

    // Properties:
    double lostDistance = 32;
    double teleportDistance = 0;

    // ==================================================
    //                    Constructor
    // ==================================================
    public TeleportToHostGoal(BaseCreatureEntity setHost) {
        this.host = setHost;
        this.setMutexBits(1);
    }

    // ==================================================
    //                    Target
    // ==================================================
    public Entity getTarget() {
        return this.host.getMasterTarget();
    }

    public void setTarget(Entity entity) {
        if(entity instanceof EntityLivingBase) this.host.setMasterTarget((EntityLivingBase) entity);
    }

    // ==================================================
    //                  Target Distance
    // ==================================================
    public void onTargetDistance(double distance, Entity followTarget) {

    }

    // ==================================================
    //                  Set Properties
    // ==================================================
    public TeleportToHostGoal setLostDistance(double setDist) {
        this.lostDistance = setDist;
        return this;
    }
    public TeleportToHostGoal setFollowBehind(double setDist) {
        this.teleportDistance = setDist;
        return this;
    }

    // ==================================================
    //                  Should Execute
    // ==================================================
    @Override
    public boolean shouldExecute() {
        Entity target = this.getTarget();
        if(target == null) return false;
        if(!target.isEntityAlive()) return false;

        // TODO Replace adding within SummonLeveledMinionsGoal due to possible position desync
        if(this.host.ticksExisted <= 20) return false;

        // Start straying when within the stray radius and the target.
        double distance = this.host.getDistance(target);
        if(distance <= this.lostDistance && this.lostDistance != 0) return false;

        return true;
    }

    // ==================================================
    //                Continue Executing
    // ==================================================
    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting();
    }

    // ==================================================
    //                      Update
    // ==================================================
    @Override
    public void updateTask() {
        if(this.host.getDistance(this.getTarget()) >= this.lostDistance) this.teleportToOwner();
        super.updateTask();
    }

    // ========== Teleport to Owner ==========
    public void teleportToOwner() {
        if(this.getTarget() == null) return;

        if(!this.host.canBreatheAir() && ((!this.host.isLavaCreature && !this.getTarget().isInWater())
                || (this.host.isLavaCreature && !this.getTarget().isInLava()))) {
            return;
        }
        if(!this.host.canBreatheUnderwater() && this.getTarget().isInWater()) {
            return;
        }

        World world = this.getTarget().getEntityWorld();
        int xPos = MathHelper.floor(this.getTarget().posX) - 2;
        int yPos = MathHelper.floor(this.getTarget().getEntityBoundingBox().minY);
        int zPos = MathHelper.floor(this.getTarget().posZ) - 2;

        if(this.host.isFlying() || this.getTarget().isInWater()) {
            this.host.setLocationAndAngles(xPos, yPos + 1, zPos, this.host.rotationYaw, this.host.rotationPitch);
            this.host.clearMovement();
            return;
        }

        for(int xOffset = 0; xOffset <= 4; ++xOffset) {
            for(int zOffset = 0; zOffset <= 4; ++zOffset) {
                if((xOffset < 1 || zOffset < 1 || xOffset > 3 || zOffset > 3)
                        && world.isSideSolid(new BlockPos(xPos + xOffset, yPos - 1, zPos + zOffset), EnumFacing.UP)
                        && !world.isBlockNormalCube(new BlockPos(xPos + xOffset, yPos, zPos + zOffset), true)
                        && !world.isBlockNormalCube(new BlockPos(xPos + xOffset, yPos + 1, zPos + zOffset), true)) {
                    this.host.setLocationAndAngles(((float)(xPos + xOffset) + 0.5F), yPos, ((float)(zPos + zOffset) + 0.5F), this.host.rotationYaw, this.host.rotationPitch);
                    this.host.clearMovement();
                    return;
                }
            }
        }
    }
}