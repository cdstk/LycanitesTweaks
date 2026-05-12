package lycanitestweaks.compat.entitymodification.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.info.beastiary.entitymodification.vanilla.CycleIndexedVariant;
import net.minecraft.entity.Entity;

public class INFDragonStage extends CycleIndexedVariant {

    public static final String TYPE_VALUE = ModLoadedUtil.ICEANDFIRE_MODID + ":dragonStage";

    private static final int[] VARIANTS = {
            1, 2, 3, 4, 5
    };

    @Override
    public boolean refreshEntityData() {
        return true;
    }
    
    @Override
    public String getOptionLangKey() {
        return "dragon.stage";
    }

    @Override
    public String getActionLangKey() {
        return String.valueOf(this.variantIndex);
    }

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(this.variantIndex >= VARIANTS.length) this.variantIndex = 0;
        if(entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            dragon.setAgeInDays(0);
            dragon.growDragon(25 * VARIANTS[this.variantIndex++]);
        }
    }
}
