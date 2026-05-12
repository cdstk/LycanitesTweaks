package lycanitestweaks.info.beastiary.entitymodification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class AbstractToggleState extends AbstractEntityModification {

    protected abstract String getTypeValue();

    @Override
    public boolean isDefaultModification() {
        return false;
    }

    @Override
    public boolean takesUserInput() {
        return false;
    }

    @Override
    public String getActionLangKey() {
        return "gui.bestiary.button.toggle";
    }

    @Override
    public void generateDefaultJSON(JsonObject json) {
        JsonArray tagModGroup = json.has(AbstractEntityModification.ENTITY_MODS_JSON)
                ? json.get(AbstractEntityModification.ENTITY_MODS_JSON).getAsJsonArray()
                : new JsonArray();
        JsonObject tagMod = new JsonObject();

        tagMod.addProperty(AbstractEntityModification.TYPE_JSON, this.getTypeValue());
        tagMod.addProperty(AbstractEntityModification.KNOWLEDGE_JSON, this.knowledgeRank);
        tagMod.addProperty(AbstractEntityModification.EXPERIENCE_JSON, this.experienceRatio);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }
}
