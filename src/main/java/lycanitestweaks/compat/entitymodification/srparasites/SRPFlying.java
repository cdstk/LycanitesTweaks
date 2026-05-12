package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfDragonE;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class SRPFlying extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":flying";

    @Override
    public String getOptionLangKey() {
        return "creature.status.flying";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityInfDragonE) {
            ((EntityInfDragonE) entity).changeStateTo(!((EntityInfDragonE) entity).getFlyingState());
        }
    }
}
