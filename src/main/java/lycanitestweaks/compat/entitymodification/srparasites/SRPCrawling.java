package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class SRPCrawling extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":crawling";

    @Override
    public String getOptionLangKey() {
        return "creature.status.crawling";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityInfEnderman) {
            ((EntityInfEnderman) entity).setCrawling(!((EntityInfEnderman) entity).isCrawling());
        }
    }
}
