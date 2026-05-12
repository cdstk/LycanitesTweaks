package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusByte extends AbstractEntityModification {

    // Must be model based animations

    public static final String TYPE_VALUE = "statusByte";
    public static final String VALUES_JSON = "values";

    protected int valueIndex = 0;
    protected List<Byte> values = new ArrayList<>();

    public StatusByte() {

    }

    public StatusByte(JsonObject json) {
        // Knowledge Rank Requirement set by static method in AbstractEntityModification
        List<Byte> possibleValues = new ArrayList<>();
        json.get(VALUES_JSON).getAsJsonArray().forEach(value -> possibleValues.add(value.getAsByte()));
        this.values = possibleValues;
    }

    @Override
    public boolean isDefaultModification() {
        return false;
    }

    @Override
    public boolean takesUserInput() {
        return false;
    }

    @Override
    public String getOptionLangKey() {
        return "gui.bestiary.button.status";
    }

    @Override
    public String getActionLangKey() {
        return this.values.isEmpty() || this.valueIndex <= 0 ? "" : "#" + this.values.get(this.valueIndex - 1);
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        if(EntityIronGolem.class.isAssignableFrom(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 4, (byte) 11, (byte) 34));
            this.values.addAll(Arrays.asList((byte) 11, (byte) 34));
        }
//        else if(EntityWitch.class.isAssignableFrom(entityInfo.getEntityClass())) {
//            this.values.add((byte) 15);
//        }
        else if(EntityRabbit.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.values.add((byte) 1);
        }
        else if(EntitySquid.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.values.add((byte) 19);
        }
//        else if(EntityVillager.class.isAssignableFrom(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 12, (byte) 13, (byte) 14));
//        }
        else if(EntityWolf.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.values.add((byte) 8);
        }

//        if(EntityAnimal.class.isAssignableFrom(entityInfo.getEntityClass())) {
//            this.values.add((byte) 18);
//        }
//        if(EntityTameable.class.isAssignableFrom(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 6, (byte) 7));
//        }
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

        JsonArray values = new JsonArray();
        this.values.forEach(values::add);
        tagMod.add(VALUES_JSON, values);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @Override
    public void modifyEntity(Entity entity) {
        if (this.valueIndex >= this.values.size()) this.valueIndex = 0;
        if (!this.values.isEmpty()) {
            entity.handleStatusUpdate(this.values.get(this.valueIndex++));
        }
    }
}
