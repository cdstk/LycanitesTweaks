package lycanitestweaks.compat.entitymodification.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCutomAttack;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.ToggleAttacking;
import net.minecraft.entity.Entity;

public class SRPAttacking extends ToggleAttacking {

    public static final String TYPE_VALUE = ModLoadedUtil.SRP_MODID + ":attacking";

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        this.toggle = !this.toggle;
        if (entity instanceof EntityParasiteBase) {
            ((EntityParasiteBase) entity).setParasiteStatus(this.toggle ? 1 : 0);
        }

        // Many Things
        if(entity instanceof EntityCutomAttack) {
            ((EntityCutomAttack) entity).attackEntityAsMobAOE(entity);
        }
    }
}
