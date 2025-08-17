package lycanitestweaks.info.projectile.behaviours;

import com.google.gson.JsonObject;
import com.lycanitesmobs.GuiHandler;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.projectile.behaviours.ProjectileBehaviour;
import com.lycanitesmobs.core.network.MessageGUIRequest;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.playermoblevel.IPlayerMobLevelCapability;
import lycanitestweaks.capability.playermoblevel.PlayerMobLevelCapability;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class ProjectileBehaviourAdvancedSummon extends ProjectileBehaviour {
	/** The id of the mob to summon. **/
	public String summonMobId;

	/** If true, the player selected minion is summoned instead of a direct entity from mobId. **/
	public boolean summonMinion = false;

	/** The chance on summoning mobs. **/
	public double summonChance = 0.05;

	/** How long in ticks the summoned creature lasts for. **/
	public int summonDuration = 60;

	/** The minimum amount of mobs to summon. **/
	public int summonCountMin = 1;

	/** The maximum amount of mobs to summon. **/
	public int summonCountMax = 1;

	/** The subspecies of mobs to summon. **/
	public int subSpeciesIndex = -1;

	/** The variant of mobs to summon. **/
	public int variantIndex = -1;

	/** The size scale of summoned mobs. **/
	public double sizeScale = 1;

	/** The custom name of summoned mobs. **/
	public String customName = "";


	@Override
	public void loadFromJSON(JsonObject json) {
		if(json.has("summonMobId")) this.summonMobId = json.get("summonMobId").getAsString();
		if(json.has("summonMinion")) this.summonMinion = json.get("summonMinion").getAsBoolean();

		if(json.has("summonChance")) this.summonChance = json.get("summonChance").getAsDouble();
		if(json.has("summonDuration")) this.summonDuration = json.get("summonDuration").getAsInt();
		if(json.has("summonCountMin")) this.summonCountMin = json.get("summonCountMin").getAsInt();
		if(json.has("summonCountMax")) this.summonCountMax = json.get("summonCountMax").getAsInt();

		if(json.has("subSpeciesIndex")) this.subSpeciesIndex = json.get("subSpeciesIndex").getAsInt();
		if(json.has("variantIndex")) this.variantIndex = json.get("variantIndex").getAsInt();
		if(json.has("sizeScale")) this.sizeScale = json.get("sizeScale").getAsDouble();
		if(json.has("customName")) this.customName = json.get("customName").getAsString();

	}

	@Override
	public void onProjectileImpact(BaseProjectileEntity projectile, World world, BlockPos pos) {
		if(projectile == null || projectile.getEntityWorld().isRemote) {
			return;
		}
		if (projectile.getThrower() instanceof EntityPlayer && !CreatureManager.getInstance().config.isSummoningAllowed(world)) {
			return;
		}

		ResourceLocation mobId = null;

		// Summon Minion:
		SummonSet summonSet = null;
		if(this.summonMinion) {
			if(!(projectile.getThrower() instanceof EntityPlayer)) return;

			EntityPlayer player = (EntityPlayer)projectile.getThrower();
			ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
			if(extendedPlayer == null) return;

			summonSet = extendedPlayer.getSelectedSummonSet();
			if(summonSet == null || summonSet.getCreatureClass() == null) {
				if(player instanceof EntityPlayerMP) {
					MessageGUIRequest messageGUIRequest = new MessageGUIRequest(GuiHandler.GuiType.BEASTIARY.id);
					LycanitesMobs.packetHandler.sendToPlayer(messageGUIRequest, (EntityPlayerMP) player);
				}
				return;
			}
			mobId = summonSet.getCreatureInfo().getResourceLocation();
		}

		// Summon From ID:
		if(mobId == null && this.summonMobId != null) {
			mobId = new ResourceLocation(this.summonMobId);
		}
		if (mobId == null) return;

		int summonCount = this.summonCountMin;
		if(this.summonCountMax > this.summonCountMin) {
			summonCount = this.summonCountMin + projectile.getEntityWorld().rand.nextInt(this.summonCountMax - this.summonCountMin);
		}

		for(int i = 0; i < summonCount; i++) {
			if (projectile.getEntityWorld().rand.nextDouble() <= this.summonChance) {
				try {
					Entity entity = EntityList.createEntityByIDFromName(mobId, world);
					if (entity instanceof BaseCreatureEntity) {
						BaseCreatureEntity minionCreature = (BaseCreatureEntity) entity;
						minionCreature.setMinion(true);
						minionCreature.setTemporary(this.summonDuration);

						if(projectile.getThrower() instanceof BaseCreatureEntity){
							BaseCreatureEntity master = (BaseCreatureEntity) projectile.getThrower();
							if((this.sizeScale == -1)) minionCreature.setSizeScale(minionCreature.sizeScale * master.sizeScale);
							else minionCreature.setSizeScale(minionCreature.sizeScale * this.sizeScale);

							if((this.subSpeciesIndex == -1)) minionCreature.setSubspecies(master.getSubspeciesIndex());
							else minionCreature.setSubspecies(this.subSpeciesIndex);

							if((this.variantIndex == -1)) minionCreature.setVariant(master.getVariantIndex());
							else minionCreature.setVariant(this.variantIndex);

							minionCreature.setLevel(master.getLevel());

							minionCreature.setMasterTarget(master);
							minionCreature.spawnEventType = master.spawnEventType;
							if (master.getAttackTarget() != null) minionCreature.setRevengeTarget(master.getAttackTarget());
							master.addMinion(minionCreature);
						}
						else if(summonSet != null && this.summonMinion){
							minionCreature.setSizeScale(minionCreature.sizeScale * this.sizeScale);
							minionCreature.setSubspecies(summonSet.subspecies);
							minionCreature.setVariant(summonSet.variant);
						}
						else {
							minionCreature.setSizeScale(minionCreature.sizeScale * this.sizeScale);
							minionCreature.setSubspecies(this.subSpeciesIndex);
							minionCreature.setVariant(this.variantIndex);
						}

						if (projectile.getThrower() instanceof EntityPlayer && minionCreature instanceof TameableCreatureEntity) {
							EntityPlayer player = (EntityPlayer) projectile.getThrower();
							TameableCreatureEntity entityTameable = (TameableCreatureEntity) minionCreature;
							entityTameable.setPlayerOwner(player);
							entityTameable.setSitting(false);
							entityTameable.setFollowing(true);
							entityTameable.setPassive(false);
							entityTameable.setAssist(true);
							entityTameable.setAggressive(true);

							if(summonSet != null && this.summonMinion) summonSet.applyBehaviour(entityTameable);

							IPlayerMobLevelCapability pml = PlayerMobLevelCapability.getForPlayer(player);
							if(pml != null){
								if(!PlayerMobLevelsConfig.getPmlBonusCategorySoulgazer().contains(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant) || Helpers.hasSoulgazerEquiped(player)) {
									entityTameable.addLevel(pml.getTotalLevelsForCategory(PlayerMobLevelsConfig.BonusCategory.SummonMinionInstant, entityTameable));
								}
							}
						}
						minionCreature.refreshAttributes();

						float randomAngle = 45F + (45F * projectile.getEntityWorld().rand.nextFloat());
						if (projectile.getEntityWorld().rand.nextBoolean()) {
							randomAngle = -randomAngle;
						}
						BlockPos spawnPos = minionCreature.getFacingPosition(projectile, -1, randomAngle);
						entity.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), projectile.rotationYaw, 0.0F);

						if(!this.customName.isEmpty()) entity.setCustomNameTag(this.customName);
						projectile.getEntityWorld().spawnEntity(entity);
					}
				}
				catch (Exception e) {
					LycanitesTweaks.LOGGER.log(Level.WARN,"[Advanced Summon Projectile] Unable to spawn entity: {}", mobId);
				}
			}
		}
	}
}
