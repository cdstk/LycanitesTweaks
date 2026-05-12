package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractEntityModification;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class INFGiantSizeScale extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":sizeScale";
    public static final String SIZES_JSON = "sizes";
    public static final String GIANT_SIZES_JSON = "giantSizes";

    private final List<Float> sizes = new ArrayList<>();
    private final List<Float> giantSizes = new ArrayList<>();

    private static NBTTagCompound sizeNBT = null;

    public INFGiantSizeScale() {
        super();
    }

    public INFGiantSizeScale(JsonObject json) {
        List<Float> possibleValues = new ArrayList<>();
        if(json.has(SIZES_JSON)) {
            json.get(SIZES_JSON).getAsJsonArray().forEach(value -> possibleValues.add(value.getAsFloat()));
            this.sizes.addAll(possibleValues);
        }

        if(json.has(GIANT_SIZES_JSON)) {
            possibleValues.clear();
            json.get(GIANT_SIZES_JSON).getAsJsonArray().forEach(value -> possibleValues.add(value.getAsFloat()));
            this.giantSizes.addAll(possibleValues);
        }
    }

    @Override
    public boolean refreshEntityData() {
        return true;
    }
    
    @Override
    public String getOptionLangKey() {
        return "createWorld.customize.custom.size";
    }

    @Override
    public String getActionLangKey() {
        return "#" + this.variantIndex;
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        if(EntityDeathWorm.class == entityInfo.getEntityClass()) {
            this.sizes.addAll(Arrays.asList(0.25F, 0.3375F, 0.425F, 0.5125F, 0.6F, 1.0F, 1.35F));
            this.giantSizes.addAll(Arrays.asList(1.7F, 2.05F, 2.4F));
        }
        else if(EntitySeaSerpent.class == entityInfo.getEntityClass()) {
            this.sizes.addAll(Arrays.asList(1.5F , 2.5F , 3.5F , 4.5F , 5.5F));
            this.giantSizes.addAll(Arrays.asList(6.0F , 6.75F , 7.5F , 8.25F , 9.0F));
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

        JsonArray sizes = new JsonArray();
        this.sizes.forEach(sizes::add);
        tagMod.add(SIZES_JSON, sizes);

        JsonArray giantSizes = new JsonArray();
        this.giantSizes.forEach(giantSizes::add);
        tagMod.add(GIANT_SIZES_JSON, giantSizes);

        tagModGroup.add(tagMod);
        json.add(AbstractEntityModification.ENTITY_MODS_JSON, tagModGroup);
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityDeathWorm) {
            EntityDeathWorm deathWorm = (EntityDeathWorm) entity;
            List<Float> sizeList = deathWorm.getScaleForAge() > 3
                    ? this.giantSizes
                    : this.sizes;
            if(this.variantIndex >= sizeList.size()) this.variantIndex = 0;
            if(sizeList.isEmpty()) return;

            deathWorm.setDeathWormScale(sizeList.get(this.variantIndex++));
        }
        else if(entity instanceof EntitySeaSerpent) {
            EntitySeaSerpent seaSerpent = (EntitySeaSerpent) entity;
            List<Float> sizeList = seaSerpent.isAncient()
                    ? this.giantSizes
                    : this.sizes;
            if(this.variantIndex >= sizeList.size()) this.variantIndex = 0;
            if(sizeList.isEmpty()) return;

            if(IceAndFireHandler.isRLCraft()) {
                seaSerpent.setSeaSerpentScale(sizeList.get(this.variantIndex++));
            }
            else {
                if(sizeNBT == null) {
                    sizeNBT = new NBTTagCompound();
                }
                sizeNBT.setFloat("Scale", sizeList.get(this.variantIndex++));
                seaSerpent.readFromNBT(sizeNBT);
            }
        }
    }
}
