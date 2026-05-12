package lycanitestweaks.info.beastiary;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.NBTModification;
import lycanitestweaks.mixin.vanilla.EntityLivingBase_InvokerMixin;
import lycanitestweaks.mixin.vanilla.EntityLiving_InvokerMixin;
import lycanitestweaks.network.PacketGenericEntityInfo;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.jsonloader.AbstractInfo;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import lycanitestweaks.util.jsonloader.ModableJSONLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityShoulderRiding;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Contains various information about a creature from default spawn information to stats, etc. **/
public class GenericEntityInfo extends AbstractInfo {

	public static final String ENTITY_ID_JSON = "entityID";
	public static final String LANG_NAME_JSON = "langKeyName";
	public static final String LANG_PREFIX_JSON = "langKeyPrefix";
	public static final String LANG_SUFFIX_JSON = "langKeySuffix";
	public static final String INITIAL_SPAWN_JSON = "renderInitialSpawn";
	public static final String DISABLE_BESTIARY_ENTRY_JSON = "disableBestiaryEntry";

	// Core Info:
	protected final String entityID;
	protected final String entryName;
	protected String langPrefix = "";
	protected String langSuffix = "";
	public boolean doInitialSpawn = true;

	public boolean disableBestiaryEntry = false;

	public List<AbstractEntityModification> bestiaryModifiers = new ArrayList<>();
	public List<AbstractEntityModification> defaultModifiers = new ArrayList<>();
	private final List<Biome> biomes = new ArrayList<>();


	/** The mod entry the entity belongs to. **/
	protected BeastiaryModInfo modInfo;

	/** The entity class used by this entity. **/
	public Class<? extends EntityLiving> entityClass = null;

	// Runtime Attributes, from the Beastiary's render Entity
	public double health = 20.0D;
	public double armor = 0.0D;
//	public double armorToughness = 0.0D;
	public double speed = 0.7D;
	public double knockbackResistance = 0.0D;

	public double damage = 2.0D;
	public double followRange = 32.0D;

	public int experience = 0;


	// Runtime Properties, from the Beastiary's render Entity
	private final List<String> combatTypes = new ArrayList<>();
	public boolean potentialSpawn = false;
	public boolean boss = false;
	public boolean animal = false;
	public boolean tameable = false;
	public boolean perchable = false;

	/**
	 * Constructor
	 * @param entityID The entity ID that this entity definition will belong to.
	 * @param entityEntryName The forge name that this entity definition will belong to.
	 */
	public GenericEntityInfo(String entityID, String entityEntryName) {
		this.entityID = entityID;
		this.entryName = entityEntryName;
	}

	@Override
	public void generateDefaultJSON(JsonObject json) {
		// Common
		if(!json.has(ModableJSONLoader.LOAD_DEFAULT)) json.addProperty(ModableJSONLoader.LOAD_DEFAULT, true);
		if(!json.has(ENTITY_ID_JSON)) json.addProperty(ENTITY_ID_JSON, this.entityID);
		if(!json.has(LANG_NAME_JSON)) json.addProperty(LANG_NAME_JSON, this.entryName);
		if(!json.has(LANG_PREFIX_JSON)) json.addProperty(LANG_PREFIX_JSON, this.langPrefix);
		if(!json.has(LANG_SUFFIX_JSON)) json.addProperty(LANG_SUFFIX_JSON, this.langSuffix);
		if(!json.has(INITIAL_SPAWN_JSON)) json.addProperty(INITIAL_SPAWN_JSON, this.doInitialSpawn);
		if(!json.has(DISABLE_BESTIARY_ENTRY_JSON)) json.addProperty(DISABLE_BESTIARY_ENTRY_JSON, this.disableBestiaryEntry);

		// Client
		if(!GenericEntityInfoManager.getInstance().isClientSide()) return;

		if(!json.has(AbstractEntityModification.ENTITY_MODS_JSON)) {
			this.defaultModifiers.forEach(tagMod -> tagMod.generateDefaultJSON(json));
			this.bestiaryModifiers.forEach(tagMod -> tagMod.generateDefaultJSON(json));
		}
	}

	@Override
	public void loadFromJSON(JsonObject json) {
		if(json.has(ENTITY_ID_JSON) && !json.get(ENTITY_ID_JSON).getAsString().equals(this.entityID))
			LycanitesTweaks.LOGGER.log(Level.WARN, "Entity ID does not match JSON. ID: {}, JSON: {}", this.entityID, json);
		if(json.has(LANG_NAME_JSON) && !json.get(LANG_NAME_JSON).getAsString().equals(this.entryName))
			LycanitesTweaks.LOGGER.log(Level.WARN, "Lang Key Name does not match JSON. Name: {}, JSON: {}", this.entryName, json);

		if(json.has(LANG_PREFIX_JSON)) {
			String candidate = json.get(LANG_PREFIX_JSON).getAsString();
			if(!candidate.isEmpty()) this.langPrefix = candidate;
		}
		if(json.has(LANG_SUFFIX_JSON)) {
			String candidate = json.get(LANG_SUFFIX_JSON).getAsString();
			if(!candidate.isEmpty()) this.langSuffix = candidate;
		}
		if(json.has(INITIAL_SPAWN_JSON)) {
			this.doInitialSpawn = json.get(INITIAL_SPAWN_JSON).getAsBoolean();
		}
		if(json.has(DISABLE_BESTIARY_ENTRY_JSON))
			this.disableBestiaryEntry = json.get(DISABLE_BESTIARY_ENTRY_JSON).getAsBoolean();

		if(json.has(AbstractEntityModification.ENTITY_MODS_JSON)) {
			for (JsonElement entityModJson : json.get(NBTModification.ENTITY_MODS_JSON).getAsJsonArray()) {
				AbstractEntityModification entityMod = AbstractEntityModification.loadFromJSON(entityModJson.getAsJsonObject());
				if (entityMod == null) continue;
				if (entityMod.isDefaultModification()) this.defaultModifiers.add(entityMod);
				else this.bestiaryModifiers.add(entityMod);
			}
		}
	}

	@Override
	public void preInit() {
		// Skip Dummies:
		if(this.disableBestiaryEntry) {
			return;
		}
	}

	@Override
	public void postInit() {
		// Skip Dummies:
		if(this.disableBestiaryEntry) {
			return;
		}

		this.initBiomes();
	}

	private void initBiomes() {
		this.biomes.clear();
		if(!GenericEntityInfoManager.getInstance().isClientSide()) return;
		ForgeRegistries.BIOMES.forEach(biome -> Arrays.stream(EnumCreatureType.values())
				.forEach(creatureType -> biome.getSpawnableList(creatureType)
						.forEach(spawnListEntry -> {
							if(spawnListEntry.entityClass == this.entityClass) {
								if(!this.biomes.contains(biome)) this.biomes.add(biome);
							}}))
		);
	}

	public void getDataFromEntity(Entity entity) {
		this.potentialSpawn = false;
		this.experience = 0;
		this.combatTypes.clear();

		if(entity instanceof EntityLiving_InvokerMixin) {
			this.experience = ((EntityLiving_InvokerMixin) entity).lycanitesTweaks$getExperienceValue();
		}
		if(this.experience == 0 && entity instanceof EntityLivingBase_InvokerMixin) {
			this.experience = ((EntityLivingBase_InvokerMixin) entity).lycanitesTweaks$invokeGetExperiencePoints(LycanitesTweaks.PROXY.getClientPlayer());
		}

		if(entity instanceof IRangedAttackMob) {
			this.combatTypes.add("range");
		}

		if(entity instanceof EntityLivingBase) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			// Safe LivingBase
			this.health = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
			this.armor = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue();
//			this.armorToughness = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getBaseValue();
			this.speed = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
			this.knockbackResistance = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getBaseValue();

			// Must check
			IAttributeInstance attribute = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			if(attribute != null) {
				this.damage = attribute.getBaseValue();
				if(this.damage != 0.0D && this.damage != 2.0D) { // Mostly Accurate, previously used a wrapper on setBaseValue
					this.combatTypes.add("melee");
				}
			}
			else {
				this.damage = -1D;
			}
			attribute = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			if(attribute != null) {
				this.followRange = attribute.getBaseValue();
			}
			else {
				this.followRange = -1D;
			}
		}

		this.animal = entity instanceof EntityAnimal;
		this.tameable = entity instanceof EntityTameable;
		this.perchable = entity instanceof EntityShoulderRiding;

		if(entity != null) {
			this.boss = !entity.isNonBoss();
			if(this.combatTypes.isEmpty()) {
				if(entity.getSoundCategory() == SoundCategory.HOSTILE) {
					this.combatTypes.add("hostile");
				}
				else {
					if(this.damage == -1D) this.followRange = -1D;
				}
			}
			if(entity.world.isRemote) {
				PacketGenericEntityInfo message = new PacketGenericEntityInfo(this);
				PacketHandler.instance.sendToServer(message);
			}
		}
	}

	/**
	 * Associates a Mod Entry to this Entity Info.
	 * @param modInfo The Mod Entry to add.
	 * @return
	 */
	public void addModInfo(BeastiaryModInfo modInfo) {
		this.modInfo = modInfo;
		if(this.langPrefix.isEmpty()) this.langPrefix = modInfo.entityLangPrefix;
		if(this.langSuffix.isEmpty()) this.langSuffix = modInfo.entityLangSuffix;
		this.disableBestiaryEntry = this.disableBestiaryEntry || modInfo.disableAllBestiaryEntries;
	}


	/**
	 * Returns the forge name of this entity
	 * @return Entity name.
	 */
	public String getEntityEntryName() {
		return this.entryName;
	}


	/**
	 * Returns the entity id as a string
	 * @return Entity ID
	 */
	public String getEntityId() {
		return this.entityID;
	}


	/**
	 * Returns the entity class
	 * @return entity class.
	 */
	public Class<? extends Entity> getEntityClass() {
		return this.entityClass;
	}


	/**
	 * Returns the resource location for this entity.
	 * @return Entity resource location.
	 */
	public ResourceLocation getResourceLocation() {
		return new ResourceLocation(this.entityID);
	}


	/**
	 * Returns the language key for this entity's name
	 * @return Entity language key.
	 */
	public String getLocalisationKey() {
		return this.langPrefix + this.entryName + this.langSuffix;
	}


	/**
	 * Returns a translated description of this entity.
	 * @return The entity description.
	 */
	public String getLocalizedName() {
		return LycanitesTweaks.PROXY.formatLangKey(this.getLocalisationKey());
	}


	/**
	 * Returns a translated description of this entity.
	 * @return The entity description.
	 */
	public String getDescription() {
		String descKey = this.modInfo.getEntityLoreKey(this.entryName);
		if(LycanitesTweaks.PROXY.hasLangKey(descKey)) {
			return LycanitesTweaks.PROXY.formatLangKey(descKey);
		}

		String[] split = this.entityID.split(":");
		descKey = this.modInfo.getEntityLoreKey(split.length > 1 ? split[1] : split[0]);

		if(LycanitesTweaks.PROXY.hasLangKey(descKey)) {
			return LycanitesTweaks.PROXY.formatLangKey(descKey);
		}

		return this.modInfo.getModdedLore(this);
	}


	/**
	 * Returns a translated description of this entity.
	 * @return The entity description.
	 */
	public List<String> getCombatDescription() {
		List<String> combatEntry = new ArrayList<>();

		this.combatTypes.forEach(langkey -> combatEntry.add(LycanitesTweaks.PROXY.formatLangKey("creature.combat." + langkey)));

		return combatEntry;
	}


	/**
	 * Returns a comma separated list of Biomes native for this Entity, NOT translated.
	 * @return The Biomes native for this Entity.
	 */
	public String getBiomeNames() {
		if(this.biomes.isEmpty()) {
			return "";
		}
		StringBuilder biomeNames = new StringBuilder();
		boolean firstBiome = true;
		for(Biome biome : biomes) {
			if(!firstBiome) {
				biomeNames.append(", ");
			}
			firstBiome = false;
			biomeNames.append(biome.getBiomeName());
		}
		return biomeNames.toString();
	}

	public BeastiaryModInfo getModInfo() {
		return this.modInfo;
	}
}
