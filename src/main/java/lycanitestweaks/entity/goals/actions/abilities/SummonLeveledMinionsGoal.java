package lycanitestweaks.entity.goals.actions.abilities;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.goals.BaseGoal;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.entity.goals.actions.TeleportToHostGoal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class SummonLeveledMinionsGoal extends BaseGoal {
    // Properties:
	protected int summonTime = 0;
	protected int summonRate = 60;
	protected int summonCap = 5;
	protected int subSpeciesIndex = -1;
	protected int variantIndex = -1;
	protected int lostDistance = 32;
	protected int behindDistance = 0;
	protected CreatureInfo minionInfo;
	protected boolean bossMechanic = false;
	protected boolean forceOnces = false;
	protected boolean perPlayer = false;
	protected boolean antiFlight = false;
	protected boolean setPersistence = false;
	protected double sizeScale = 1.0F;
	protected String customName = "";

	/**
	 * Constructor
	 * @param setHost The creature using this goal.
	 */
	public SummonLeveledMinionsGoal(BaseCreatureEntity setHost) {
		super(setHost);
    }

	/**
	 * Sets the rate of summoning (in ticks).
	 * @param summonRate The summoning tick rate.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setSummonRate(int summonRate) {
    	this.summonRate = summonRate;
    	return this;
    }

	/**
	 * Sets the minion count cap for summoning.
	 * @param summonCap The summoning cap.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setSummonCap(int summonCap) {
		this.summonCap = summonCap;
		return this;
	}

	/**
	 * Sets the sub species index for summoning.
	 * @param subSpeciesIndex The sub species index.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setSubSpeciesIndex(int subSpeciesIndex) {
		this.subSpeciesIndex = subSpeciesIndex;
		return this;
	}

	/**
	 * Sets the variant index for summoning.
	 * @param variantIndex The variant index.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setVariantIndex(int variantIndex) {
		this.variantIndex = variantIndex;
		return this;
	}

	/**
	 * If true, will enable persistence and set temporary for 5 minutes. Only applies to instances of BaseCreatureEntity
	 * @param bossMechanic True to enable.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setBossMechanic(boolean bossMechanic) {
		this.bossMechanic = bossMechanic;
		return this;
	}

	public SummonLeveledMinionsGoal setForceOnce(boolean forceOnces) {
		this.forceOnces = forceOnces;
		return this;
	}

	/**
	 * If true, will enable persistence and set temporary for 5 minutes. Only applies to instances of BaseCreatureEntity
	 * @param bossMechanic True to enable.
	 * @param lostDistance Distance away behind minion can teleport to host.
	 * @param behindDistance Distance behind minion tries to be after teleport.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setBossMechanic(boolean bossMechanic, int lostDistance, int behindDistance) {
		this.bossMechanic = bossMechanic;
		this.lostDistance = lostDistance;
		this.behindDistance = behindDistance;
		return this;
	}

	/**
	 * If true, the cap is scaled per players detected.
	 * @param perPlayer True to enable.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setPerPlayer(boolean perPlayer) {
		this.perPlayer = perPlayer;
		return this;
	}

	/**
	 * Sets anti flight summoning where minions are summoned at any player targets that are flying.
	 * @param antiFlight True to enable.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setAntiFlight(boolean antiFlight) {
    	this.antiFlight = antiFlight;
    	return this;
    }

	/**
	 * If true, the minion will be spawned with persistence
	 * @param persitence True to enable.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setPersitence(boolean persitence) {
		this.setPersistence = persitence;
		return this;
	}

	/**
	 * Sets the creature to summon.
	 * @param creatureName The creature name to summon.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setMinionInfo(String creatureName) {
    	this.minionInfo = CreatureManager.getInstance().getCreature(creatureName);
    	return this;
    }

	/**
	 * Sets the scale to multiple the minion's size by.
	 * @param sizeScale The scale to multiple the creature's size by.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setSizeScale(double sizeScale) {
		this.sizeScale = sizeScale;
		return this;
	}

	/**
	 * Sets the Custom Name of the minions.
	 * @param customName The Custom Name.
	 * @return This goal for chaining.
	 */
	public SummonLeveledMinionsGoal setCustomName(String customName) {
		this.customName = customName;
		return this;
	}

	@Override
    public boolean shouldExecute() {
		if(this.host.isPetType("familiar")) {
			return false;
		}
		return super.shouldExecute() && this.minionInfo != null;
    }

	@Override
    public void startExecuting() {
		this.summonTime = 1;
	}

	@Override
    public void updateTask() {
		if(this.forceOnces) {
			this.summonTime = this.summonRate - 1;
			this.forceOnces = false;
		}

		if(this.summonTime++ % this.summonRate != 0) {
			return;
		}

		if(this.host.getMinions(this.minionInfo.getEntityClass()).size() >= this.summonCap) {
			return;
		}

		// Anti Flight Mode:
		if(this.antiFlight) {
			for (EntityPlayer target : this.host.playerTargets) {
				if(target.isCreative() || target.isSpectator())
					continue;
				if (CreatureManager.getInstance().config.bossAntiFlight > 0 && target.posY > this.host.posY + CreatureManager.getInstance().config.bossAntiFlight + 1) {
					this.summonMinion(target);
				}
			}
			return;
		}

		this.summonMinion(this.host.getAttackTarget());
    }

    protected void summonMinion(EntityLivingBase target) {
		EntityLivingBase minion = this.minionInfo.createEntity(this.host.getEntityWorld());
		int variantIndex = (this.variantIndex == -1) ? this.host.getVariantIndex() : this.variantIndex;

		// TODO fix this jank way to ensure boss health bar
		if(minion instanceof BaseCreatureEntity){
			((BaseCreatureEntity) minion).setVariant(variantIndex);
		}

		this.host.summonMinion(minion, this.host.getRNG().nextDouble() * 360, this.host.width + 1);
		if(!this.customName.isEmpty()) minion.setCustomNameTag(this.customName);
		if(minion instanceof BaseCreatureEntity) {
			BaseCreatureEntity minionCreature = (BaseCreatureEntity)minion;
			minionCreature.setAttackTarget(target);

			if(this.setPersistence) minionCreature.enablePersistence();
			if(this.bossMechanic){
				minionCreature.enablePersistence();
				minionCreature.setTemporary(6000);
				((BaseCreatureEntity) minion).tasks.addTask(((BaseCreatureEntity) minion).nextTravelGoalIndex++,
						new TeleportToHostGoal((BaseCreatureEntity) minion)
								.setLostDistance(this.lostDistance)
								.setFollowBehind(this.behindDistance));
			}

			if((this.sizeScale == -1)) minionCreature.setSizeScale(minionCreature.sizeScale * this.host.sizeScale);
			else minionCreature.setSizeScale(minionCreature.sizeScale * this.sizeScale);

			if((this.subSpeciesIndex == -1)) minionCreature.setSubspecies(this.host.getSubspeciesIndex());
			else minionCreature.setSubspecies(this.subSpeciesIndex);

            minionCreature.setVariant(variantIndex);

			minionCreature.applyLevel(host.getLevel()); // refresh stats
			if(minionCreature.getBossInfo() != null) minionCreature.bossInfo.setName(minion.getDisplayName());
        }
	}
}
