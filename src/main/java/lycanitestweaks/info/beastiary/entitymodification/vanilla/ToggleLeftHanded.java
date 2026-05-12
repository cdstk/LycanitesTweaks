package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class ToggleLeftHanded extends AbstractToggleState {

    public static final String TYPE_VALUE = "leftHanded";

    @Override
    public String getOptionLangKey() {
        return "options.mainHand";
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.knowledgeRank = 2;
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityLiving) {
            EntityLiving entityLiving = (EntityLiving) entity;
            entityLiving.setLeftHanded(!entityLiving.isLeftHanded());
        }
    }
}
