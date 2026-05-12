package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;

public class ToggleBatHanging extends AbstractToggleState {

    public static final String TYPE_VALUE = "batHanging";

    @Override
    public String getOptionLangKey() {
        return "creature.status.hanging";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityBat) {
            EntityBat bat = (EntityBat) entity;
            bat.setIsBatHanging(!bat.getIsBatHanging());
        }
    }
}
