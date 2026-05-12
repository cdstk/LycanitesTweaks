package lycanitestweaks.info.beastiary;

import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;

public class GenericEntityKnowledge {

	public GenericBestiary bestiary;
	public String entityID;
	public int rank;
	public int experience = 0;

	/**
	 * Constructor
	 * @param bestiary The Bestiary this knowledge is part of.
	 * @param entityID The ID of the entity that this is knowledge of.
	 * @param rank The rank of this knowledge.
	 * @param experience The amount of experience of this knowledge.
	 */
	public GenericEntityKnowledge(GenericBestiary bestiary, String entityID, int rank, int experience) {
		this.bestiary = bestiary;
		this.entityID = entityID;
		this.rank = rank;
		this.experience = experience;
	}

	/**
	 * Returns the Entity Info of the Knowledge.
	 * @return The entity info.
	 */
	public GenericEntityInfo getEntityInfo() {
		return GenericEntityInfoManager.getInstance().getEntityInfo(this.entityID);
	}

	/**
	 * Adds experience to this entity knowledge.
	 * @param experience The amount of experience to add.
	 * @return The amount of experience required for the next rank up. 0 is returned when at max rank.
	 */
	public int addExperience(int experience) {
		int maxExperience = this.getMaxExperience();
		if (maxExperience <= 0) {
			return 0;
		}
		this.experience += experience;
		int remainingExperience = this.experience - maxExperience;

		// Rank Up:
		if (remainingExperience >= 0) {
			this.rank++;
			this.experience = 0;
			this.addExperience(remainingExperience);
			this.bestiary.sendAddedMessage(this);
		}

		return remainingExperience;
	}

	public int getMaxExperience() {
		if (this.rank == 1) {
			return 1000;
		}
		if (this.rank >= 2) {
			return 0;
		}
		return 1;
	}

	public float getExperienceRatio() {
		return this.getMaxExperience() <= 0 ? 1.0F : (float) this.experience / this.getMaxExperience();
	}

	public float getRankWithKnowledge() {
		return this.rank + this.getExperienceRatio();
	}
}
