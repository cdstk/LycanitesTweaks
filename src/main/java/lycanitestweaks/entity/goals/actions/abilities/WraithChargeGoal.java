package lycanitestweaks.entity.goals.actions.abilities;

import com.lycanitesmobs.core.entity.creature.EntityWraith;
import com.lycanitesmobs.core.entity.goals.BaseGoal;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class WraithChargeGoal extends BaseGoal {

    private boolean targetMatchesMaster = false;
    private boolean copyPotionsFromMaster = false;
    private float minRange = -1F;
    private float maxRange = -1F;
    private final NonNullList<ElementInfo> additionalElements = NonNullList.create();

    public WraithChargeGoal(EntityWraith setHost) {
        super(setHost);
    }

    public WraithChargeGoal setTargetMatchesMaster(boolean targetMatches) {
        this.targetMatchesMaster = targetMatches;
        return this;
    }

    public WraithChargeGoal setCopyPotions(boolean copyPotions) {
        this.copyPotionsFromMaster = copyPotions;
        return this;
    }

    public WraithChargeGoal setMinRange(float minRange) {
        this.minRange = minRange;
        return this;
    }

    public WraithChargeGoal setMaxRange(float maxRange) {
        this.maxRange = maxRange;
        return this;
    }

    public WraithChargeGoal addElement(String elementName) {
        ElementInfo elementInfo = ElementManager.getInstance().getElement(elementName);
        if(elementInfo != null) additionalElements.add(elementInfo);

        return this;
    }

    public WraithChargeGoal addAllElements(List<String> elementNames) {
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
        EntityLivingBase attackTarget;
        if(this.targetMatchesMaster && this.host.hasMaster()) {
            attackTarget = this.host.getMasterAttackTarget();
            if(attackTarget == null || !attackTarget.isEntityAlive()) {
                return false;
            }
        }

        attackTarget = this.getTarget();
        if(attackTarget == null || !attackTarget.isEntityAlive()) {
            return false;
        }

        return super.shouldExecute();
    }

    @Override
    public void updateTask() {
        EntityLivingBase wraithTarget = this.getTarget();
        if(this.targetMatchesMaster) {
            wraithTarget = this.host.getMasterAttackTarget();
        }

        if(wraithTarget == null || !wraithTarget.isEntityAlive()) {
            return;
        }

        if(this.host instanceof EntityWraith) {
            EntityWraith wraith = (EntityWraith) this.host;
            wraith.getLookHelper().setLookPosition(
                    wraithTarget.posX,
                    wraithTarget.posY + wraithTarget.getEyeHeight(),
                    wraithTarget.posZ,
                    wraith.getHorizontalFaceSpeed(),
                    wraith.getVerticalFaceSpeed()
            );
            if(!wraith.hasFixateTarget() && wraith.canEntityBeSeen(wraithTarget)) {
                double min = this.minRange > -1F ? this.minRange : Math.sqrt(wraith.getMeleeAttackRange(wraithTarget, 0.5D));
                double max = this.maxRange > -1F ? this.maxRange : wraith.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
                if (wraith.getDistance(wraithTarget) >= min
                        && wraith.getDistance(wraithTarget) <= max
                        && wraithTarget.posY - wraith.posY <= 1F
                        && (wraith.posY + wraith.height) - (wraithTarget.posY + wraithTarget.height) <= 1F) {
                    wraith.playAttackSound();
                    wraith.chargeAttack();

                    if(this.copyPotionsFromMaster && wraith.hasMaster()) {
                        wraith.getMasterTarget().getActivePotionEffects().forEach(wraith::addPotionEffect);
                    }
                    this.additionalElements.forEach(elementInfo -> wraith.getElements().add(elementInfo));

                    wraith.setFixateTarget(wraithTarget);
                }
            }
        }
    }
}
