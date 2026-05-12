package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHost;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHostII;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityWymo;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.compat.SRPHandler;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class SRPBurrow extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":burrow";

    @Override
    public String getOptionLangKey() {
        return "creature.status.burrow";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityHost) {
            ((EntityHost) entity).setBurrowed(!((EntityHost) entity).getBurrowed());
        }
        else if(entity instanceof EntityHostII) {
            ((EntityHostII) entity).setBurrowed(!((EntityHostII) entity).getBurrowed());
        }

        if(SRPHandler.dotNineIncompleteMobs()) {
            if (entity instanceof EntityWymo) {
                ((EntityWymo) entity).setBurrowed(!((EntityWymo) entity).getBurrowed());
            }
        }
    }
}
