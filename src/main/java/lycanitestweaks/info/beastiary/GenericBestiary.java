package lycanitestweaks.info.beastiary;

import com.lycanitesmobs.core.info.CreatureManager;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketGenericBeastiary;
import lycanitestweaks.network.PacketGenericEntityKnowledge;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.util.jsonloader.GenericEntityInfoManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GenericBestiary {

	public static final String KNOWLEDGE_NBT ="GenericEntityKnowledge";
	public static final String ENTITY_NBT = "EntityID";
	public static final String RANK_NBT = "Rank";
	public static final String EXPERIENCE_NBT = "Experience";

	public ILycanitesTweaksPlayerCapability ltp;
	public Map<String, GenericEntityKnowledge> entityKnowledgeMap = new HashMap<>();

	public GenericBestiary(ILycanitesTweaksPlayerCapability ltp) {
		this.ltp = ltp;
	}
	

    // ==================================================
    //                     Knowledge
    // ==================================================
	/**
	 * Update an Entity Knowledge in the Bestiary after checking rank, etc.
	 * @param newKnowledge The new knowledge to add.
	 * @return If the new knowledge is added or an existing on is updated, false if unchanged or invalid.
	 */
	public boolean updateEntityKnowledge(GenericEntityKnowledge newKnowledge, boolean sendToClient) {
		GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(newKnowledge.entityID);
		if(entityInfo == null)
			return false;
		if(entityInfo.disableBestiaryEntry)
			return false;

		GenericEntityKnowledge currentKnowledge = this.getCreatureKnowledge(entityInfo.getEntityId());
		if(currentKnowledge != null) {
			currentKnowledge.rank = newKnowledge.rank;
			currentKnowledge.experience = newKnowledge.experience;
			if (sendToClient) {
				this.sendToClient(currentKnowledge);
			}
			return true;
		}

		this.entityKnowledgeMap.put(newKnowledge.entityID, newKnowledge);
		if (sendToClient) {
			this.sendAddedMessage(newKnowledge);
			this.sendToClient(newKnowledge);
		}
		return true;
	}

	/**
	 * Attempt to add Entity Knowledge to this Bestiary based on the provided entity and sends feedback to the player.
	 * @param entity The entity being discovered.
	 * @param experience The Knowledge experience being gained.
	 * @return The newly added or updated knowledge or null if unchanged or invalid.
	 */
	public GenericEntityKnowledge addEntityKnowledge(Entity entity, int experience) {
		GenericEntityInfo entityInfo = GenericEntityInfoManager.getInstance().getEntityInfo(entity.getClass());
		if(entityInfo == null) return null;

		GenericEntityKnowledge newKnowledge = this.getCreatureKnowledge(entityInfo.getEntityId());
		if (newKnowledge == null) {
			newKnowledge = new GenericEntityKnowledge(this.ltp.getBestiary(), entityInfo.getEntityId(), 1, experience);
		}
		else {
			if (newKnowledge.getMaxExperience() <= 0) {
				return null;
			}
			newKnowledge.addExperience(experience);
		}
		this.updateEntityKnowledge(newKnowledge, true);

		return newKnowledge;
	}

	/**
	 * Sends a message to the player on gaining Entity Knowledge.
	 * @param entityKnowledge The entity knowledge that was added.
	 */
	public void sendAddedMessage(GenericEntityKnowledge entityKnowledge) {
		if(this.ltp.getPlayer().world.isRemote || !CreatureManager.getInstance().config.beastiaryKnowledgeMessages) {
			return;
		}
		GenericEntityInfo entityInfo = entityKnowledge.getEntityInfo();
		if(entityInfo == null) return;
		if (entityKnowledge.rank == 1) {
			this.ltp.getPlayer().sendMessage(new TextComponentTranslation(
					"message.bestiary.generic.new",
					new TextComponentTranslation(entityInfo.getLocalisationKey()))
			);
		}
		else {
			this.ltp.getPlayer().sendMessage(new TextComponentTranslation(
					"message.bestiary.generic.rank",
					new TextComponentTranslation(entityInfo.getLocalisationKey()),
					entityKnowledge.rank
			));
		}
	}


	/**
	 * Sends a message to the player if they attempt to add an entity that they already know.
	 * @param entityKnowledge The entity knowledge that was trying to be added.
	 */
	public void sendKnownMessage(GenericEntityKnowledge entityKnowledge) {
		if(this.ltp.getPlayer().getEntityWorld().isRemote) {
			return;
		}
		GenericEntityInfo entityInfo = entityKnowledge.getEntityInfo();
		GenericEntityKnowledge currentKnowledge = this.ltp.getBestiary().getCreatureKnowledge(entityInfo.getEntityId());
		if(currentKnowledge == null) return;
		this.ltp.getPlayer().sendMessage(
				new TextComponentTranslation(
						"message.beastiary.generic.known",
						currentKnowledge.rank,
						new TextComponentTranslation(entityInfo.getLocalisationKey()))
		);
	}


	/**
	 * Returns the current knowledge of the provided entity. Use GenericEntityKnowledge.rank to get the current rank of knowledge the player has.
	 * @param entityID The ID of the entity to get the knowledge of.
	 * @return The entity knowledge or null if there is no knowledge.
	 */
	
	public GenericEntityKnowledge getCreatureKnowledge(String entityID) {
		return this.entityKnowledgeMap.get(entityID);
	}


	/**
	 * Returns if this Bestiary has the provided knowledge rank or higher.
	 * @param entityID The ID of the entity to check the knowledge rank of.
	 * @param rank The minimum knowledge rank required.
	 * @return True if the knowledge rank is met or exceeded.
	 */
	public boolean hasKnowledgeRank(String entityID, int rank) {
		GenericEntityKnowledge entityKnowledge = this.getCreatureKnowledge(entityID);
		if(entityKnowledge == null) {
			return false;
		}
		return entityKnowledge.rank >= rank;
	}

	
	/**
	 * Returns how many entities of the specified mod entry the player has discovered.
	 * @param beastiaryModInfo Mod Entry to check with.
	 * @return True if the player has at least one entity form the specific mod entry.
	 */
	public int getCreaturesDiscovered(BeastiaryModInfo beastiaryModInfo) {
		if(this.entityKnowledgeMap.isEmpty()) {
			return 0;
		}

		int entitiesDescovered = 0;
		for(Entry<String, GenericEntityKnowledge> knowledgeEntry : this.entityKnowledgeMap.entrySet()) {
			if(knowledgeEntry.getValue() != null) {
				if (knowledgeEntry.getValue().getEntityInfo().getModInfo() == beastiaryModInfo) {
					entitiesDescovered++;
				}
			}
		}
		return entitiesDescovered;
	}
	
	
	// ==================================================
    //                    Network Sync
    // ==================================================
	/** Sends GenericEntityKnowledge to the client. For when it's added or changed server side but needs updated client side. **/
	public void sendToClient(GenericEntityKnowledge newKnowledge) {
		if(this.ltp.getPlayer().world.isRemote && !(this.ltp.getPlayer() instanceof EntityPlayerMP)) {
			return;
		}
		PacketGenericEntityKnowledge message = new PacketGenericEntityKnowledge(newKnowledge);
		PacketHandler.instance.sendTo(message, (EntityPlayerMP) this.ltp.getPlayer());
	}
	
	/** Sends the whole Bestiary progress to the client, use sparingly! **/
	public void sendAllToClient() {
		if(this.ltp.getPlayer() instanceof EntityPlayerMP) {
			if(ForgeConfigHandler.debug.debugLoggerTrigger) LycanitesTweaks.LOGGER.log(Level.DEBUG, "Sending all bestiary to client. {}", this.ltp.getPlayer());
			PacketGenericBeastiary.sendAllEntriesToClient(this, (EntityPlayerMP) this.ltp.getPlayer());
		}
	}
	

	// ==================================================
    //                        NBT
    // ==================================================
    /** Reads a list of Entity Knowledge from a player's NBTTag. **/
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
    	if(!nbtTagCompound.hasKey(KNOWLEDGE_NBT))
    		return;
    	this.entityKnowledgeMap.clear();
    	NBTTagList knowledgeList = nbtTagCompound.getTagList(KNOWLEDGE_NBT, 10);
    	for(int i = 0; i < knowledgeList.tagCount(); ++i) {
	    	NBTTagCompound nbtKnowledge = knowledgeList.getCompoundTagAt(i);
    		if(nbtKnowledge.hasKey(ENTITY_NBT)) {
    			String entityName = nbtKnowledge.getString(ENTITY_NBT);
				int rank = 0;
				if(nbtKnowledge.hasKey(RANK_NBT)) {
					rank = nbtKnowledge.getInteger(RANK_NBT);
				}
				int experience = 0;
				if(nbtKnowledge.hasKey(EXPERIENCE_NBT)) {
					experience = nbtKnowledge.getInteger(EXPERIENCE_NBT);
				}
	    		GenericEntityKnowledge entityKnowledge = new GenericEntityKnowledge(
                        this,
	    				entityName,
	    				rank,
						experience
	    			);
	    		this.updateEntityKnowledge(entityKnowledge, false);
    		}
    	}
    }

    /** Writes a list of Entity Knowledge to a player's NBTTag. **/
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
    	NBTTagList knowledgeList = new NBTTagList();
		for(Entry<String, GenericEntityKnowledge> knowledgeEntry : entityKnowledgeMap.entrySet()) {
			GenericEntityKnowledge entityKnowledge = knowledgeEntry.getValue();
			NBTTagCompound nbtKnowledge = new NBTTagCompound();
			nbtKnowledge.setString(ENTITY_NBT, entityKnowledge.entityID);
			nbtKnowledge.setInteger(RANK_NBT, entityKnowledge.rank);
			nbtKnowledge.setInteger(EXPERIENCE_NBT, entityKnowledge.experience);
			knowledgeList.appendTag(nbtKnowledge);
		}
		nbtTagCompound.setTag(KNOWLEDGE_NBT, knowledgeList);
    }
}
