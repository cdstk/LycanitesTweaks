package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityParrot;

public class ToggleParrotParty extends AbstractToggleState {

    public static final String TYPE_VALUE = "partyParrot";

    @Override
    public String getOptionLangKey() {
        return "creature.status.dancing";
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
        if(entity instanceof EntityParrot) {
            EntityParrot parrot = (EntityParrot) entity;

            if(parrot.isPartying()) {
                parrot.setNoAI(false);
                parrot.setPartying(entity.getPosition(), false);
            }
            else {
                parrot.setNoAI(true);
                parrot.setPartying(entity.getPosition(), true);
            }
        }
    }
}
