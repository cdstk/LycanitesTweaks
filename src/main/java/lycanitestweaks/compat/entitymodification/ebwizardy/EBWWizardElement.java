package lycanitestweaks.compat.entitymodification.ebwizardy;

import electroblob.wizardry.constants.Element;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.entity.living.EntityWizard;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class EBWWizardElement extends CycleIndexedVariant {

    // Doesn't work enough, not used, use nbt mod if you're crazy

    public static final String TYPE_VALUE = ModLoadedUtil.EBW_MODID + ":wizardElement";

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= Element.values().length) this.variantIndex = 0;
        if(entity instanceof EntityWizard) {
            EntityWizard wizard = (EntityWizard) entity;
            wizard.setElement(Element.values()[this.variantIndex++]);
        }
        else if(entity instanceof EntityEvilWizard) {
            ((EntityEvilWizard) entity).setElement(Element.values()[this.variantIndex++]);
        }
    }
}
