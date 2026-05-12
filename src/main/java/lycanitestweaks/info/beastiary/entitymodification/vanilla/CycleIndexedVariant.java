package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractCycleState;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;

public class CycleIndexedVariant extends AbstractCycleState {

    public static final String TYPE_VALUE = "basicVariants";
    public static final String VARIANT_COUNT_JSON = "variantCount";

    protected int variantIndex = 0;
    protected int variantCount = 4;
    private static NBTTagCompound shulkerColor = null;

    public CycleIndexedVariant() {
        super();
    }

    public CycleIndexedVariant(JsonObject json) {
        if(json.has(VARIANT_COUNT_JSON)) this.variantCount = json.get(VARIANT_COUNT_JSON).getAsInt();
    }

    @Override
    public String getOptionLangKey() {
        return "gui.bestiary.button.variant";
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        if(entityInfo.getEntityClass() == EntityParrot.class) {
            this.variantCount = 5;
        }
        else if(entityInfo.getEntityClass() == EntityRabbit.class) {
            this.variantCount = 7;
        }
        else if(entityInfo.getEntityClass() == EntitySheep.class) {
            this.variantCount = 16;
        }
        else if(entityInfo.getEntityClass() == EntityShulker.class) {
            this.variantCount = 16;
        }
        else {
            this.variantCount = 4;
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

        tagMod.addProperty(VARIANT_COUNT_JSON, this.variantCount);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= this.variantCount) this.variantIndex = 0;
        if(entity instanceof EntityLlama) {
            ((EntityLlama) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityOcelot) {
            ((EntityOcelot) entity).setTameSkin(this.variantIndex++);
        }
        else if(entity instanceof EntityParrot) {
            ((EntityParrot) entity).setVariant(this.variantIndex++);
        }
        else if(entity instanceof EntityRabbit) {
            if(this.variantIndex == this.variantCount - 1) this.variantIndex = 99;
            ((EntityRabbit) entity).setRabbitType(this.variantIndex++);
        }
        else if(entity instanceof EntitySheep) {
            ((EntitySheep) entity).setFleeceColor(EnumDyeColor.byMetadata(this.variantIndex++));
        }
        else if(entity instanceof EntityShulker) {
            if(shulkerColor == null) shulkerColor = new NBTTagCompound();
            shulkerColor.setInteger("Color", this.variantIndex++);
            entity.readFromNBT(shulkerColor);
        }
    }
}
