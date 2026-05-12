package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityBanoAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityCanraAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.EntityNoglaAdapted;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityCanra;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityNogla;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityEsor;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityGanro;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.StatusByte;
import net.minecraft.entity.Entity;

import java.util.Arrays;

public class SRPStatus extends StatusByte {

    // No way to add automatically
    // Not all work
    // Can manually add more

    public SRPStatus() {
        super();
    }

    public SRPStatus(JsonObject json) {
        super(json);
    }

    @Override
    public String getOptionLangKey() {
        return "creature.status.skill";
    }

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":status";

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.values.add((byte) 0);

        // Adapted Bolster
        if(EntityBanoAdapted.class.equals(entityInfo.getEntityClass())) {
            this.values.addAll(Arrays.asList((byte) 3, (byte) 25));
        }
        // Adapted Summoner
        else if(EntityCanraAdapted.class.equals(entityInfo.getEntityClass())) {
            this.values.add((byte) 10);
        }
        // Adapted Reeker
        else if(EntityNoglaAdapted.class.equals(entityInfo.getEntityClass())) {
            this.values.addAll(Arrays.asList((byte) 2, (byte) 3));
        }
        // Adapted Arachnida
//        else if(EntityRanracAdapted.class.equals(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 2, (byte) 11));
//        }
        // Adapted Longarms
//        else if(EntityShycoAdapted.class.equals(entityInfo.getEntityClass())) {
//            this.values.add((byte) 10);
//        }
        // Primitive Bolster
//        else if(EntityBano.class.equals(entityInfo.getEntityClass())) {
//            this.values.add((byte) 15);
//        }
        // Primitive Devourer
//        else if(EntityLum.class.equals(entityInfo.getEntityClass())) {
//            this.values.add((byte) 1);
//        }
        // Primitive Reeker
        else if(EntityNogla.class.equals(entityInfo.getEntityClass())) {
            this.values.addAll(Arrays.asList((byte) 2, (byte) 3));
        }
        // Primitive Arachnida
//        else if(EntityRanrac.class.equals(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 2, (byte) 11));
//        }
        // Primitive Longarms
//        else if(EntityShyco.class.equals(entityInfo.getEntityClass())) {
//            this.values.add((byte) 10);
//        }
        // Primitive Summoner
        else if(EntityCanra.class.equals(entityInfo.getEntityClass())) {
            this.values.add((byte) 10);
        }
        // Overseer
//        else if(EntityAlafha.class.equals(entityInfo.getEntityClass())) {
//            this.values.addAll(Arrays.asList((byte) 2, (byte) 10));
//        }
        // Marauder
        else if(EntityEsor.class.equals(entityInfo.getEntityClass())) {
            this.values.addAll(Arrays.asList((byte) 3, (byte) 25));
        }
        // Warden
        else if(EntityGanro.class.equals(entityInfo.getEntityClass())) {
            this.values.addAll(Arrays.asList((byte) 2, (byte) 3, (byte) 100));
        }
        // Monarch
        else if(EntityOrch.class.equals(entityInfo.getEntityClass())) {
            this.values.add((byte) 10);
        }
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
        if (!this.values.isEmpty() && entity instanceof EntityParasiteBase) {
            ((EntityParasiteBase) entity).setParasiteStatus((this.values.get(this.valueIndex++)));
        }
    }
}
