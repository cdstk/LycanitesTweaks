package lycanitestweaks.compat.entitymodification.battletowers;

import atomicstryker.battletowers.common.AS_EntityGolem;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.AbstractToggleState;
import lycanitestweaks.util.IAS_EntityGolem_ModifyMixin;
import net.minecraft.entity.Entity;

public class ASBTAwake extends AbstractToggleState {

    public static final String TYPE_VALUE = ModLoadedUtil.BATTLETOWERS_MODID + ":awake";

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getOptionLangKey() {
        return "creature.status.attack";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof AS_EntityGolem && entity instanceof IAS_EntityGolem_ModifyMixin) {
            AS_EntityGolem golem = (AS_EntityGolem) entity;
            IAS_EntityGolem_ModifyMixin modifyGolem = (IAS_EntityGolem_ModifyMixin) entity;
            modifyGolem.lycanitesTweaks$setAwake(golem.getIsDormant());
        }
    }
}
