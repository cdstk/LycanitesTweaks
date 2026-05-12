package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillagerProfessions extends AbstractEntityModification {

    public static final String TYPE_VALUE = "villagerProfessions";

    protected String professionID = "";

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
        return "creature.status.profession";
    }

    @Override
    public String getActionLangKey() {
        return this.professionID;
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

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) entity;
            VillagerRegistry.VillagerProfession profession = villager.getProfessionForge();
            profession = VillagerRegistry.getById(VillagerRegistry.getId(profession) + 1);
            if(profession == null) profession = VillagerRegistry.getById(0);

            villager.setProfession(profession);
            this.professionID = "entity.Villager." + profession.getRegistryName().getPath();
        }
        else if(entity instanceof EntityZombieVillager) {
            EntityZombieVillager zombieVillager = (EntityZombieVillager) entity;
            VillagerRegistry.VillagerProfession profession = zombieVillager.getForgeProfession();
            profession = VillagerRegistry.getById(VillagerRegistry.getId(profession) + 1);
            if(profession == null) profession = VillagerRegistry.getById(0);

            zombieVillager.setForgeProfession(profession);
            this.professionID = "entity.Villager." + profession.getRegistryName().getPath();
        }
    }
}
