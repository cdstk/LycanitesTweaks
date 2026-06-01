package lycanitestweaks.client.model.item;

import com.lycanitesmobs.LycanitesMobs;
import lycanitestweaks.client.model.ModelObjOldGeneric;
import net.minecraft.entity.Entity;

public class ModelHellfireCannon extends ModelObjOldGeneric {

    // ==================================================
    //                    Constructors
    // ==================================================
    public ModelHellfireCannon() {
        this(1.0F);
    }

    public ModelHellfireCannon(float shadowSize) {
        // Load Model:
        this.initModel("rahovart", LycanitesMobs.modInfo, "entity/rahovart");

        // Set Rotation Centers:
        setPartCenter("head", 0F, 23.275F, 0F);
        setPartCenter("mouth", 0F, 23.075F, -2.5F);
        setPartCenter("body", 0F, 11.025F, 0F);
        setPartCenter("armleft", 5.6F, 19.775F, 0F);
        setPartCenter("armright", -5.6F, 19.775F, 0F);
        setPartCenter("legleft", 2.45F, 11.2F, 0F);
        setPartCenter("legright", -2.45F, 11.2F, 0F);
        setPartCenter("tail", 0F, 18.9F, 4.725F);

        lockHeadX = false;
        lockHeadY = false;

        // Trophy:
        this.trophyScale = 0.1F;
        this.trophyOffset = new float[] {0.0F, 0.0F, -0.4F};
    }


    // ==================================================
    //                Can Render Part
    // ==================================================
    /** Returns true if the part can be rendered on the base layer. **/
    public boolean canBaseRenderPart(String partName, Entity entity, boolean trophy) {
        return partName.equals("armleft");
    }

    @Override
    public float getRenderScale(Entity entity) {
        return (1.8F / 25F) * 3F;
    }
}
