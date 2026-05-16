package lycanitestweaks.compat.entitymodification.ebwizardy;

import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.GenericEntityInfo;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class EBWWizardSkin extends CycleIndexedVariant {

    // Doesn't affect Element/Color, not used, use nbt mod if you're crazy

    public static final String TYPE_VALUE = ModLoadedUtil.EBW_MODID + ":wizardSkin";

    @Override
    public void generateDefaultValues(GenericEntityInfo entityInfo) {
        this.variantIndex = 6;
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= this.variantCount) this.variantIndex = 0;
        if(entity instanceof EntityWizard) {
            ((EntityWizard) entity).textureIndex = this.variantIndex++;
        }
        else if(entity instanceof EntityEvilWizard) {
            ((EntityEvilWizard) entity).textureIndex = this.variantIndex++;
        }
    }
}
