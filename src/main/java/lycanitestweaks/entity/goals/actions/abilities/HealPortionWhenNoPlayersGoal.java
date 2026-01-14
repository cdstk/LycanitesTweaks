package lycanitestweaks.entity.goals.actions.abilities;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealPortionWhenNoPlayersGoal extends EntityAIBase {
	BaseCreatureEntity host;

	// Targets:
	public List<EntityPlayer> playerTargets = new ArrayList<>();
	public boolean firstPlayerTargetCheck = false;

    // Properties:
	private int tickDelay = 200;
	private int tickRate = 20;
	private int checkRange = 64;
    private float healAmount = 0.02F;

	/**
	 * Constrcutor
	 * @param setHost The creature using this goal.
	 */
	public HealPortionWhenNoPlayersGoal(BaseCreatureEntity setHost) {
        this.host = setHost;
    }

	/**
	 * Sets how long this creature checks for no players.
	 * @param tickDelay The tick delay.
	 * @return This goal for chaining.
	 */
	public HealPortionWhenNoPlayersGoal setTickDelay(int tickDelay) {
		this.tickDelay = tickDelay;
		return this;
	}

	/**
	 * Sets how fast this creature heals by.
	 * @param tickRate The tick rate to heal by.
	 * @return This goal for chaining.
	 */
	public HealPortionWhenNoPlayersGoal setTickRate(int tickRate) {
		this.tickRate = tickRate;
		return this;
	}

	/**
	 * Sets how far to check for nearby players.
	 * @param checkRange The distance to check.
	 * @return This goal for chaining.
	 */
	public HealPortionWhenNoPlayersGoal setCheckRange(int checkRange) {
		this.checkRange = checkRange;
		return this;
	}

	/**
	 * Sets how much this creature heals by.
	 * @param healAmount The amount to heal by.
	 * @return This goal for chaining.
	 */
	public HealPortionWhenNoPlayersGoal setHealAmount(float healAmount) {
		this.healAmount = healAmount;
		return this;
	}

	@Override
    public boolean shouldExecute() {
		return this.host.isEntityAlive();
    }

	@Override
    public boolean shouldContinueExecuting() {
        return this.host.isEntityAlive();
    }

	@Override
    public void startExecuting() {}

	@Override
    public void resetTask() {
		this.firstPlayerTargetCheck = false;
	}

	@Override
    public void updateTask() {
		if(this.host.updateTick % this.tickDelay != 0 && !this.firstPlayerTargetCheck) {
			return;
		}
		this.firstPlayerTargetCheck = true;

		// Check for players in range
		if(checkRange == -1){
			this.playerTargets = this.host.playerTargets.stream().filter(player -> !player.isSpectator()).collect(Collectors.toList());
		}
		else {
			this.playerTargets = this.host.getNearbyEntities(EntityPlayer.class, entity -> !((EntityPlayer)entity).isSpectator(), checkRange);
		}

		if (this.host.updateTick % this.tickRate == 0 && this.playerTargets.isEmpty()) {
			this.host.heal(this.healAmount * this.host.getMaxHealth());
		}
    }
}
