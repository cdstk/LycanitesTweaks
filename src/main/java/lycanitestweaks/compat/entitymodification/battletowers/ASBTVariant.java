package lycanitestweaks.compat.entitymodification.battletowers;

import atomicstryker.battletowers.common.AS_WorldGenTower;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import lycanitestweaks.mixin.battletowers.BattleTowerTypes_InvokerMixin;
import lycanitestweaks.util.IAS_EntityGolem_ModifyMixin;
import net.minecraft.entity.Entity;

public class ASBTVariant extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.BATTLETOWERS_MODID + ":towerType";

    private String actionDisplay = "";

    @Override
    public boolean refreshEntityData() {
        return true;
    }

    @Override
    public String getActionLangKey() {
        return this.actionDisplay;
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= AS_WorldGenTower.TowerTypes.values().length) this.variantIndex = 0;
        if(entity instanceof IAS_EntityGolem_ModifyMixin) {
            IAS_EntityGolem_ModifyMixin golem = (IAS_EntityGolem_ModifyMixin) entity;
            AS_WorldGenTower.TowerTypes type = AS_WorldGenTower.TowerTypes.values()[this.variantIndex++];

            if((Object)type instanceof BattleTowerTypes_InvokerMixin) {
                this.actionDisplay = ((BattleTowerTypes_InvokerMixin)(Object)type).lycanitesTweaks$invokeGetWallBlockID().getLocalizedName();
            }
            else {
                this.actionDisplay = type.getName();
            }

            golem.lycanitesTweaks$modifyTowerType(type.ordinal());
        }
    }
}
