package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityZombie;

public class ToggleBaby extends AbstractToggleState {

    public static final String TYPE_VALUE = "baby";

    @Override
    public String getOptionLangKey() {
        return "creature.status.baby";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityAgeable) {
            EntityAgeable entityAgeable = (EntityAgeable) entity;
            if(entityAgeable.isChild()) entityAgeable.setGrowingAge(0);
            else entityAgeable.setGrowingAge(-24000);
        }
        if(entity instanceof EntityZombie) {
            EntityZombie zombie = (EntityZombie) entity;
            zombie.setChild(!zombie.isChild());
        }
    }
}
