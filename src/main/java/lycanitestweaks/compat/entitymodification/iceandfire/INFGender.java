package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class INFGender extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":gender";

    @Override
    public String getOptionLangKey() {
        return "dragon.gender";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityCockatrice) {
            ((EntityCockatrice) entity).setHen(!((EntityCockatrice) entity).isHen());
        }
        else if(entity instanceof EntityDragonBase) {
            ((EntityDragonBase) entity).setGender(!((EntityDragonBase) entity).isMale());
        }
    }
}
