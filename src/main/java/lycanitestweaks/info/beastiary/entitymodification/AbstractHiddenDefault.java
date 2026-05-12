package lycanitestweaks.info.beastiary.entitymodification;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class AbstractHiddenDefault extends AbstractEntityModification {

    protected abstract String getTypeValue();

    @Override
    public boolean isDefaultModification() {
        return true;
    }

    @Override
    public boolean takesUserInput() {
        return false;
    }

    @Override
    public String getOptionLangKey() {
        return "";
    }

    @Override
    public String getActionLangKey() {
        return "";
    }

    @Override
    public void generateDefaultJSON(JsonObject json) {
        JsonArray tagModGroup = json.has(AbstractEntityModification.ENTITY_MODS_JSON)
                ? json.get(AbstractEntityModification.ENTITY_MODS_JSON).getAsJsonArray()
                : new JsonArray();
        JsonObject tagMod = new JsonObject();

        tagMod.addProperty(AbstractEntityModification.TYPE_JSON, this.getTypeValue());

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }
}
