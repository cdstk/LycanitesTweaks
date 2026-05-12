package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class INFTrollWeapon extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":trollWeapon";

    @Override
    public String getOptionLangKey() {
        return "key.swapHands";
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.knowledgeRank = 2;
        this.variantCount = EnumTroll.Weapon.values().length;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityTroll) {
            if(this.variantIndex >= EnumTroll.Weapon.values().length) this.variantIndex = 0;
            ((EntityTroll) entity).setWeaponType(EnumTroll.Weapon.values()[this.variantIndex++]);
        }
    }
}
