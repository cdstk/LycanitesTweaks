package lycanitestweaks.entity.goals;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.GoalConditions;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.List;

public class ExtendedGoalConditions extends GoalConditions {

    protected int minimumBattlePhase = -1;
    protected boolean isDependentOnEntity = false;
    protected final List<EntityLivingBase> dependentOnEntities = new ArrayList<>();

    public ExtendedGoalConditions setMinimumBattlePhase(int minimumBattlePhase) {
        this.minimumBattlePhase = minimumBattlePhase;
        return this;
    }

    public ExtendedGoalConditions setDependencyEntities(List<EntityLivingBase> entities) {
        this.isDependentOnEntity = true;
        this.dependentOnEntities.clear();
        this.dependentOnEntities.addAll(entities);
        return this;
    }

    public ExtendedGoalConditions addDependencyEntity(EntityLivingBase entity) {
        this.isDependentOnEntity = true;
        this.dependentOnEntities.add(entity);
        return this;
    }

    public ExtendedGoalConditions setDependentOnEntity(boolean dependentOnEntity) {
        this.isDependentOnEntity = dependentOnEntity;
        return this;
    }

    public void cleanDependencyEntities() {
        this.dependentOnEntities.removeIf(
                dependency -> dependency == null
                        || !dependency.isEntityAlive()
                        || !dependency.isAddedToWorld()
        );
    }

    public void removeDependencyEntity(EntityLivingBase entity) {
        this.dependentOnEntities.remove(entity);
    }

    @Override
    public boolean isMet(BaseCreatureEntity creatureEntity) {
        if(this.minimumBattlePhase != -1 && creatureEntity.getBattlePhase() < this.minimumBattlePhase) {
            return false;
        }

        if(this.isDependentOnEntity) {
            if(creatureEntity.updateTick % 200 == 0L) {
                this.cleanDependencyEntities();
            }
            if(this.dependentOnEntities.isEmpty()) return false;
        }

        return super.isMet(creatureEntity);
    }
}