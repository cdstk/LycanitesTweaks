package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import lycanitestweaks.compat.IceAndFireHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import net.minecraft.entity.Entity;

public class INFGiantVariant extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":giantVariant";

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getOptionLangKey() {
        return "entity.Giant.name";
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
        if(entity instanceof EntityDeathWorm) {
            EntityDeathWorm deathWorm = (EntityDeathWorm) entity;
            if(deathWorm.getScaleForAge() > 3.0F) {
                deathWorm.setDeathWormScale(0.6F);
            }
            else {
                deathWorm.setDeathWormScale(2.4F);
            }
        }
        else if(entity instanceof EntitySeaSerpent) {
            EntitySeaSerpent seaSerpent = (EntitySeaSerpent) entity;
            seaSerpent.setAncient(!seaSerpent.isAncient());
            if(IceAndFireHandler.isRLCraft()) {
                seaSerpent.setSeaSerpentScale(seaSerpent.getSeaSerpentScale());
            }
        }
    }
}
