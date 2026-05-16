package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.chunk.storage.AnvilChunkLoader.readWorldEntityPos;

public class NBTModification extends AbstractEntityModification {

    public static final String TYPE_VALUE = "nbtMod";
    public static final String NBT_TAG_JSON = "nbtTag";
    public static final String NBT_TYPE_JSON = "nbtType";
    public static final String VALUES_JSON = "values";
    public static final String DEFAULT_JSON = "isDefault";
    public static final String USER_INPUT_JSON = "userInput";
    public static final String REFRESH_DATA_JSON = "refreshData";

    public final String nbtTag;
    public final NBTType nbtType;
    public final List<String> values;
    public final boolean isDefault;
    public final boolean userInput;
    public final boolean refreshData;
    private int valueIndex = 0;

    public NBTModification() {
        this.knowledgeRank = 3;
        this.nbtTag = "";
        this.isDefault = false;
        this.userInput = true;
        this.refreshData = false;
        this.nbtType = NBTType.ALL;
        this.values = new ArrayList<>();
    }

    public NBTModification(JsonObject json) {
        // Knowledge Rank Requirement set by static method in AbstractEntityModification
        this.nbtTag = json.has(NBT_TAG_JSON) ? json.get(NBT_TAG_JSON).getAsString() : "";
        this.isDefault = json.has(DEFAULT_JSON) && json.get(DEFAULT_JSON).getAsBoolean();
        this.userInput = json.has(USER_INPUT_JSON) && json.get(USER_INPUT_JSON).getAsBoolean();
        this.refreshData = json.has(REFRESH_DATA_JSON) && json.get(REFRESH_DATA_JSON).getAsBoolean();

        NBTType possibleType = json.has(NBT_TYPE_JSON) ? NBTType.valueOf(json.get(NBT_TYPE_JSON).getAsString().toUpperCase()) : null;
        this.nbtType = possibleType == null ? NBTType.ALL : possibleType;

        List<String> possibleValues = new ArrayList<>();
        json.get(VALUES_JSON).getAsJsonArray().forEach(value -> possibleValues.add(value.getAsString()));
        this.values = possibleValues;
    }

    @Override
    public boolean isDefaultModification() {
        return this.isDefault;
    }

    @Override
    public boolean takesUserInput() {
        return this.userInput;
    }

    @Override
    public boolean refreshEntityData() {
        return this.refreshData;
    }

    @Override
    public String getOptionLangKey() {
        return "NBT" + (this.nbtType == NBTType.ALL ? "" : ":" + this.nbtTag);
    }

    @Override
    public String getActionLangKey() {
        if(this.userInput) {
            return "gui.bestiary.button.set";
        }
        else if(!this.values.isEmpty() && this.valueIndex > 0) {
            return this.values.get(this.valueIndex - 1);
        }
        return "";
    }

    @Override
    public void generateDefaultJSON(JsonObject json) {
        JsonArray tagModGroup = json.has(AbstractEntityModification.ENTITY_MODS_JSON)
                ? json.get(AbstractEntityModification.ENTITY_MODS_JSON).getAsJsonArray()
                : new JsonArray();
        JsonObject tagMod = new JsonObject();

        tagMod.addProperty(AbstractEntityModification.TYPE_JSON, TYPE_VALUE);
        tagMod.addProperty(AbstractEntityModification.KNOWLEDGE_JSON, this.knowledgeRank);
        tagMod.addProperty(AbstractEntityModification.EXPERIENCE_JSON, this.experienceRatio);
        tagMod.addProperty(NBT_TAG_JSON, "");
        tagMod.addProperty(DEFAULT_JSON, this.isDefault);
        tagMod.addProperty(USER_INPUT_JSON, this.userInput);
        tagMod.addProperty(REFRESH_DATA_JSON, this.refreshData);
        tagMod.addProperty(NBT_TYPE_JSON, "ALL");

        JsonArray values = new JsonArray();
        tagMod.add(VALUES_JSON, values);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.values.isEmpty()) return;
        if(this.valueIndex >= this.values.size()) this.valueIndex = 0;
        this.modifyEntity(entity, this.values.get(this.valueIndex++));
    }
    
    @Override
    public void modifyEntity(Entity entity, String value) {
        if(value.isEmpty()) return;

        NBTTagCompound entityNBT = CommandBase.entityToNBT(entity);
        NBTTagCompound copyNBT = entityNBT.copy();
        NBTTagCompound jsonNBT = new NBTTagCompound();
        String nbtString = getNbtString(value);

        try {
            jsonNBT = JsonToNBT.getTagFromJson(nbtString);
        }
        catch (NBTException nbtexception) {
            LycanitesTweaks.LOGGER.log(Level.WARN,"[Tag Modification] Failed to read NBT from JSON string: {}", nbtexception.getMessage());
        }

        UUID uuid = entity.getUniqueID();
        entityNBT.merge(jsonNBT);
        entity.setUniqueId(uuid);

        if (entityNBT.equals(copyNBT)) {
            LycanitesTweaks.LOGGER.log(Level.WARN,"Entity NBT already equals JSON NBT: {}", entityNBT.toString());
        }
        else {
            entity.readFromNBT(entityNBT);
            if (entityNBT.hasKey("Passengers", 9)) {
                NBTTagList nbttaglist = entityNBT.getTagList("Passengers", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    BlockPos pos = entity.getPosition();
                    Entity passenger = readWorldEntityPos(nbttaglist.getCompoundTagAt(i), entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), true);

                    if (passenger != null) {
                        passenger.startRiding(entity, true);
                    }
                }
            }
        }
    }

    private String getNbtString(String nbtValue) {
        String nbtString = "";
        switch (nbtType) {
            case ALL:
                nbtString = nbtValue;
                break;
            case BOOLEAN:
            case BYTE:
                nbtString = nbtTag + ":" + nbtValue + "b";
                break;
            case INTEGER:
            case DOUBLE:
                nbtString = nbtTag + ":" + nbtValue;
                break;
            case SHORT:
                nbtString = nbtTag + ":" + nbtValue + "s";
                break;
            case LONG:
                nbtString = nbtTag + ":" + nbtValue + "l";
                break;
            case FLOAT:
                nbtString = nbtTag + ":" + nbtValue + "f";
                break;
            case STRING:
                nbtString = nbtTag + ":\"" + nbtValue + "\"";
                break;
        }
        return "{" + nbtString + "}";
    }

    public enum NBTType {
        ALL,
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        BOOLEAN
    }
}
