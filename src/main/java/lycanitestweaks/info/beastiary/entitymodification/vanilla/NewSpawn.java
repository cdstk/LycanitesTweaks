package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class NewSpawn extends AbstractToggleState {

    public static final String TYPE_VALUE = "newSpawn";

//    @Override
//    public boolean refreshEntityData() {
//        return true;
//    }

    @Override
    public String getOptionLangKey() {
        return "gui.bestiary.button.newspawn";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityLiving) {
            entity.setDead();
        }
    }
}
