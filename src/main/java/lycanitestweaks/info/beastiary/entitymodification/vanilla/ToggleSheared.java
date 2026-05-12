package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntitySheep;

public class ToggleSheared extends AbstractToggleState {

    public static final String TYPE_VALUE = "shearedSkin";

    @Override
    public String getOptionLangKey() {
        return "creature.status.sheared";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntitySheep) {
            ((EntitySheep) entity).setSheared(!((EntitySheep) entity).getSheared());
        }
        else if(entity instanceof EntitySnowman) {
            ((EntitySnowman) entity).setPumpkinEquipped(!((EntitySnowman) entity).isPumpkinEquipped());
        }
    }
}
