package lycanitestweaks.entity.goals.targeting;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import com.lycanitesmobs.core.entity.goals.targeting.TargetingGoal;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SupportDrowningGoal extends TargetingGoal {

    // Targets:
    private Class<?> targetClass = EntityLiving.class;
    private BlockPos destination = null;

    // Properties:
    private double speed = 1.0D;
    private boolean tameTargeting = false;
    private boolean clearTargetPath = false;
    private int targetChance = 0;
    protected double verticalTargetingRange = 4;

    // Pathing:
//    private int failedPathFindingPenalty = 0;
//    private int failedPathFindingPenaltyMax = 0;
//    private int failedPathFindingPenaltyPlayerMax = 0;
    private int repathTime = 0;

    // ==================================================
    //                    Constructor
    // ==================================================
    public SupportDrowningGoal(BaseCreatureEntity setHost) {
        super(setHost);
    }


    // ==================================================
    //                  Set Properties
    // ==================================================
    public SupportDrowningGoal setTameTargetting(boolean setTargetting) {
        this.tameTargeting = setTargetting;
        return this;
    }


    // ==================================================
    //                  Set Properties
    // ==================================================
    public SupportDrowningGoal setSpeed(double setSpeed) {
        this.speed = setSpeed;
        return this;
    }
    public SupportDrowningGoal setChance(int setChance) {
        this.targetChance = setChance;
        return this;
    }
    public SupportDrowningGoal setTargetClass(Class setTargetClass) {
        this.targetClass = setTargetClass;
        return this;
    }
    public SupportDrowningGoal setSightCheck(boolean setSightCheck) {
        this.checkSight = setSightCheck;
        return this;
    }
    public SupportDrowningGoal setOnlyNearby(boolean setNearby) {
        this.nearbyOnly = setNearby;
        return this;
    }
    public SupportDrowningGoal setCantSeeTimeMax(int setCantSeeTimeMax) {
        this.cantSeeTimeMax = setCantSeeTimeMax;
        return this;
    }
    public SupportDrowningGoal setRange(double setDist) {
        this.targetingRange = setDist;
        return this;
    }
    public SupportDrowningGoal setClearTargetPath(boolean setClearTargetPath) {
        this.clearTargetPath = setClearTargetPath;
        return this;
    }
    public SupportDrowningGoal setHorizontalTargetRange(double setTargetingRange) {
        this.targetingRange = setTargetingRange;
        return this;
    }
    public SupportDrowningGoal setVerticalTargetingRange(double setTargetingRange) {
        this.verticalTargetingRange = setTargetingRange;
        return this;
    }

    // ==================================================
    //                 Get Target Distance
    // ==================================================
    protected double getVerticalTargetDistance() {
        if(this.verticalTargetingRange > 0)
            return this.verticalTargetingRange;
        IAttributeInstance attributeInstance = this.host.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return attributeInstance.getAttributeValue();
    }


    @Override
    public void startExecuting() {
        super.startExecuting();
        this.destination = null;
        PathNavigate pathNavigate = this.host.getNavigator();
        pathNavigate.clearPath();
        pathNavigate.setPath(pathNavigate.getPathToEntityLiving(this.getTarget()), this.speed);
    }

    @Override
    public boolean shouldContinueExecuting() {
        ExtendedEntity extendedEntity = ExtendedEntity.getForEntity(this.getTarget());
        if(extendedEntity != null && extendedEntity.isPickedUp() && extendedEntity.pickedUpByEntity != this.host){
            return false;
        }
        if(this.host.getPickupEntity() == this.getTarget() && this.host.getNavigator().noPath()){
            return false;
        }
        return super.shouldContinueExecuting();
    }


    // ==================================================
    //                    Host Target
    // ==================================================
    @Override
    protected EntityLivingBase getTarget() {
        return this.host.getMasterTarget();
    }
    @Override
    protected void setTarget(EntityLivingBase newTarget) {
        this.host.setMasterTarget(newTarget);
    }


    // ==================================================
    //                 Valid Target Check
    // ==================================================
    @Override
    protected boolean isValidTarget(EntityLivingBase target) {
        // Target Class Check:
        if(this.targetClass != null && !this.targetClass.isAssignableFrom(target.getClass()))
            return false;

        // Tamed Checks:
        if(!this.tameTargeting && this.host.isTamed())
            return false;

        return (target.isInWater() || target.isInLava());
    }

    // ==================================================
    //                   Should Execute
    // ==================================================
    @Override
    public boolean shouldExecute() {
        if (this.host.updateTick % 20 != 0) {
            return false;
        }
        if(this.targetChance > 0 && this.host.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        if(this.host.hasMaster()) return false;

        this.target = null;

        double distance = this.getTargetDistance();
        double heightDistance = this.getVerticalTargetDistance();
        if(this.host.useDirectNavigator()) heightDistance = distance;
        this.target = this.getNewTarget(distance, heightDistance, distance);
        return this.target != null;
    }

    // ==================================================
    //                      Reset
    // ==================================================
    @Override
    public void resetTask() {
        super.resetTask();
        // Reset actual pickup
        if(this.host.getPickupEntity() != null && this.targetClass.isAssignableFrom(this.host.getPickupEntity().getClass())) {
            if(this.clearTargetPath && this.getTarget() instanceof EntityLiving) ((EntityLiving) this.getTarget()).getNavigator().clearPath();
            this.host.dropPickupEntity();
        }
        // Reset actual master
        if(this.host.getMasterTarget() != null && this.targetClass.isAssignableFrom(this.host.getMasterTarget().getClass())) {
            this.host.setMasterTarget(null);
        }
        this.destination = null;
    }

    @Override
    public void updateTask(){
        if(this.host.canPickupEntity(this.getTarget())){
            if(--this.repathTime <= 0) {
                this.repathTime = 4 + this.host.getRNG().nextInt(7);
                this.findDestination();
            }
            if(this.destination != null && !this.host.hasPickupEntity() && this.host.posY > this.getTarget().posY){
                if (this.getTarget() instanceof EntityLiving && this.getTarget().getRNG().nextFloat() < 0.8F) {
                    ((EntityLiving) this.getTarget()).getJumpHelper().setJumping();
                }
                if(this.host.getDistanceSq(this.getTarget()) <= this.host.getMeleeAttackRange(this.getTarget(), 0)){
                    Path destinationPath = this.host.getNavigator().getPathToPos(this.destination);
                    if(destinationPath != null){
                        this.getTarget().setPosition(this.host.posX + 0.5F, this.host.posY - this.getTarget().height, this.host.posZ + 0.5F);
                        this.host.pickupEntity(this.getTarget()); // Clears Path
                        this.host.getNavigator().setPath(destinationPath, this.speed);
                    }
                }
            }
        }
    }

    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        IBlockState iblockstate = this.host.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.host.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.getTarget()) && this.host.world.isAirBlock(blockpos.up()) && this.host.world.isAirBlock(blockpos.up(2));
    }

    private void findDestination() {
        PathNavigate destinationNavigator = ((EntityLiving) this.getTarget()).getNavigator();

        // Check of current target's path is good
        if(destinationNavigator.getPath() != null && destinationNavigator.getPath().getFinalPathPoint() != null){
            PathPoint destinationPoint = destinationNavigator.getPath().getFinalPathPoint();
            this.destination = new BlockPos(destinationPoint.x, destinationPoint.y, destinationPoint.z);
            if(this.host.world.getBlockState(this.destination).getMaterial().isLiquid()){
                this.destination = null;
            }
        }

        // Check of entity has a home
        if(this.destination == null){
            if(this.getTarget() instanceof EntityCreature && ((EntityCreature) this.getTarget()).hasHome()){
                this.destination = ((EntityCreature) this.getTarget()).getHomePosition();
            }
            else if(this.getTarget() instanceof BaseCreatureEntity && ((BaseCreatureEntity) this.getTarget()).hasHome()){
                this.destination = ((BaseCreatureEntity) this.getTarget()).getHomePosition();
            }
        }

        // Try to improve destination
        if(this.destination != null){
            BlockPos.MutableBlockPos testGroundPos = new BlockPos.MutableBlockPos(this.destination);
            int x = MathHelper.floor(testGroundPos.getX()) - 2;
            int z = MathHelper.floor(testGroundPos.getZ()) - 2;
            int y = MathHelper.floor(testGroundPos.getY());

            checkHorizontal:
            for (int dx = 0; dx <= 4; ++dx) {
                for (int dz = 0; dz <= 4; ++dz) {
                    if ((dx < 1 || dz < 1 || dx > 3 || dz > 3) && this.isTeleportFriendlyBlock(x, z, y, dx, dz)) {
                        testGroundPos.setPos(testGroundPos.getX() + dx, testGroundPos.getY(), testGroundPos.getZ() + dz);
                        break checkHorizontal;
                    }
                }
            }

            while(this.host.world.isAirBlock(testGroundPos) && testGroundPos.getX() > 0){
                testGroundPos.setY(testGroundPos.getY() - 1);
            }
            testGroundPos.setY(testGroundPos.getY() + 1);
            this.destination = testGroundPos;
        }
    }
}
