package lycanitestweaks.info.beastiary.entitymodification.vanilla;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;

public class CycleHorseVariant extends CycleIndexedVariant {

    public static final String TYPE_VALUE = "horseVariants";

    private static final int[] VARIANTS = {
            0, 1, 2, 3, 4, 5, 6,
            256, 257, 258, 259, 260, 261, 262,
            512, 513, 514, 515, 516, 517, 518,
            768, 769, 770, 771, 772, 773, 774,
            1024, 1025, 1026, 1027, 1028, 1029, 1030
    };

    @Override
    protected String getTypeValue() {
        return TYPE_VALUE;
    }

    @Override
    public void modifyEntity(Entity entity) {
        if(entity instanceof EntityHorse) {
            if(variantIndex >= VARIANTS.length) variantIndex = 0;
            ((EntityHorse) entity).setHorseVariant(VARIANTS[variantIndex++]);
        }
    }
}
