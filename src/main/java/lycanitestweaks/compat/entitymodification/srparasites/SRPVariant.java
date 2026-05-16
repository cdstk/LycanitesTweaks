package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPrimitive;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPure;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityShycoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityMudo;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityNuuh;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityRathol;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityDorpa;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfHuman;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfVillager;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityNogla;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SRPVariant extends CycleIndexedVariant {

    // No way to add automatically and no way to refresh stats for display smh
    // Can manually add more or just use the "Respawn" DoInitialSpawn to see update's new ones

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":parasitetype";
    public static final String SKINS_JSON = "skins";

    private final List<Integer> skins = new ArrayList<>();

    public SRPVariant() {
        super();
    }

    public SRPVariant(JsonObject json) {
        List<Integer> possibleValues = new ArrayList<>();
        if(json.has(SKINS_JSON)) {
            json.get(SKINS_JSON).getAsJsonArray().forEach(value -> possibleValues.add(value.getAsInt()));
            this.skins.addAll(possibleValues);
        }
    }

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getOptionLangKey() {
        return "creature.status.skin";
    }

    @Override
    public String getActionLangKey() {
        if(!this.skins.isEmpty() && this.variantIndex > 0) {
            return "#" + this.skins.get(this.variantIndex - 1);
        }
        return "";
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.knowledgeRank = 1;
        this.skins.add(0);

        // Tyrant Longarms
        if(EntityShycoAdapted.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Flying Carrier
//        else if(EntityButhol.class.equals(entityInfo.getEntityClass())) {
//            this.skins.add(1);
//        }
        // Light Carrier
//        else if(EntityGothol.class.equals(entityInfo.getEntityClass())) {
//            this.skins.add(1);
//        }
        // Rupter
        else if(EntityMudo.class.equals(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(5, 6));
        }
        // Mangler
        else if(EntityNuuh.class.equals(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(5, 6));
        }
        // Heavy Carrier
        else if(EntityRathol.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Assimilated Big Spider
        else if(EntityDorpa.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        else if(EntityInfHuman.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(1, 2, 3));
        }
        if(EntityInfVillager.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Primitive Reeker
        else if(EntityNogla.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Monarch
        else if(EntityOrch.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Haunter
        else if(EntityPheon.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }
        // Colony Carrier
        else if(EntityVesta.class.equals(entityInfo.getEntityClass())) {
            this.skins.add(1);
        }

        if(EntityPAdapted.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(5, 6, 7));
        }
        else if(EntityPPrimitive.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(5, 6, 7));
        }
        else if(EntityPPure.class.isAssignableFrom(entityInfo.getEntityClass())) {
            this.skins.addAll(Arrays.asList(5, 6, 7));
        }
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
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

        JsonArray skins = new JsonArray();
        this.skins.forEach(skins::add);
        tagMod.add(SKINS_JSON, skins);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= this.skins.size()) this.variantIndex = 0;
        if(this.skins.isEmpty()) return;

        if(entity instanceof EntityParasiteBase) {
            EntityParasiteBase parasite = (EntityParasiteBase) entity;
            parasite.setSkin(this.skins.get(this.variantIndex++));
        }
    }
}
