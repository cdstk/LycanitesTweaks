package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityHydra;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class INFHydraHeads extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":hydraHeads";

    @Override
    public String getOptionLangKey() {
        return "entity.if_hydra.name";
    }

    @Override
    public String getActionLangKey() {
        return String.valueOf(this.variantIndex - 1);
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.knowledgeRank = 2;
        this.variantCount = EntityHydra.HEADS;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityHydra) {
            if(this.variantIndex > EntityHydra.HEADS) this.variantIndex = 1;
            ((EntityHydra) entity).setHeadCount(this.variantIndex++);
        }
    }
}
