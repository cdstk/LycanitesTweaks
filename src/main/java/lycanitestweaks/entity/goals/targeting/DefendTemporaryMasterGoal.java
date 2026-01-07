package lycanitestweaks.entity.goals.targeting;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.targeting.DefendEntitiesGoal;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class DefendTemporaryMasterGoal extends DefendEntitiesGoal {

    // ==================================================
  	//                    Constructor
  	// ==================================================
    public DefendTemporaryMasterGoal(BaseCreatureEntity setHost, Class<? extends EntityLiving> defendClass) {
        super(setHost, defendClass);
    }

    // ==================================================
    //                      Reset
    // ==================================================
    @Override
    public void resetTask() {
        super.resetTask();
        if(!this.host.isTamed() && this.host.hasMaster() && this.defendClass.isAssignableFrom(this.host.getMasterTarget().getClass())) {
            this.host.setMasterTarget(null);
        }
    }

    // ==================================================
 	//                 Valid Target Check
 	// ==================================================
    @Override
    protected boolean isValidTarget(EntityLivingBase target) {
        if(super.isValidTarget(target)){
            if(!this.host.isTamed() && !this.host.hasMaster()) {
                EntityLivingBase newMaster = (target instanceof EntityLiving) ? ((EntityLiving)target).getAttackTarget() : target.getRevengeTarget();
                this.host.setMasterTarget(newMaster);
            }
            return true;
        }
        return false;
    }
}
