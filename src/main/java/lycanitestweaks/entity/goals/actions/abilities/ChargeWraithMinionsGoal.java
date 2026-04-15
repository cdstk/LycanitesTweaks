package lycanitestweaks.entity.goals.actions.abilities;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import com.lycanitesmobs.core.entity.goals.BaseGoal;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChargeWraithMinionsGoal extends BaseGoal {

    private boolean targetMatchesMaster = false;
    private boolean copyPotions = false;
    private float minRange = -1F;
    private float maxRange = -1F;
    private final NonNullList<ElementInfo> additionalElements = NonNullList.create();

    // Runtime list
    private final Set<EntityWraith> affectedMinions = new HashSet<>();

    public ChargeWraithMinionsGoal(BaseCreatureEntity setHost) {
        super(setHost);
    }

    public ChargeWraithMinionsGoal setTargetMatchesMaster(boolean targetMatches) {
        this.targetMatchesMaster = targetMatches;
        return this;
    }

    public ChargeWraithMinionsGoal setCopyPotions(boolean copyPotions) {
        this.copyPotions = copyPotions;
        return this;
    }

    public ChargeWraithMinionsGoal setMinRange(float minRange) {
        this.minRange = minRange;
        return this;
    }

    public ChargeWraithMinionsGoal setMaxRange(float maxRange) {
        this.maxRange = maxRange;
        return this;
    }

    public ChargeWraithMinionsGoal addElement(String elementName) {
        ElementInfo elementInfo = ElementManager.getInstance().getElement(elementName);
        if(elementInfo != null) additionalElements.add(elementInfo);

        return this;
    }

    public ChargeWraithMinionsGoal addAllElements(List<String> elementNames) {
        List<ElementInfo> elementsToAdd = new ArrayList<>();

        for(String elementName : elementNames) {
            ElementInfo elementInfo = ElementManager.getInstance().getElement(elementName);
            if(elementInfo != null) elementsToAdd.add(elementInfo);
        }

        this.additionalElements.addAll(elementsToAdd);
        return this;
    }

    @Override
    public boolean shouldExecute() {
        if(this.host.getMinions(EntityWraith.class).isEmpty()) {
            return false;
        }

        EntityLivingBase attackTarget = this.getTarget();
        if(attackTarget == null || !attackTarget.isEntityAlive()) {
            return false;
        }

        return super.shouldExecute();
    }

    @Override
    public void updateTask() {
        List<EntityLivingBase> wraithMinions = this.host.getMinions(EntityWraith.class);
        wraithMinions.forEach(minion -> {
            if (minion instanceof EntityWraith) {
                EntityWraith wraith = (EntityWraith) minion;
                if(!this.affectedMinions.contains(wraith)) {
                    this.affectedMinions.add(wraith);
                    wraith.tasks.addTask(wraith.nextCombatGoalIndex++, new WraithChargeGoal(wraith)
                            .setTargetMatchesMaster(this.targetMatchesMaster)
                            .setCopyPotions(this.copyPotions)
                            .setMinRange(this.minRange)
                            .setMaxRange(this.maxRange)
                            .addAllElements(this.additionalElements.stream().map(elementInfo -> elementInfo.name).collect(Collectors.toList()))
                    );
                }
            }
        });
        this.affectedMinions.removeIf(wraith -> !wraithMinions.contains(wraith));
    }
}
