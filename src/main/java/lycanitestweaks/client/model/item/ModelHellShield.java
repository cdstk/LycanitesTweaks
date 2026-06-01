package lycanitestweaks.client.model.item;

import com.lycanitesmobs.LycanitesMobs;
import lycanitestweaks.client.model.ModelObjOldGeneric;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelHellShield extends ModelObjOldGeneric {

    private boolean isItem = true;
    private boolean isActive = false;
    private int shieldVariant = 0;

    // ==================================================
    //                    Constructors
    // ==================================================
    public ModelHellShield() {
        this(1.0F);
    }

    public ModelHellShield(float shadowSize) {
        // Load Model:
        this.initModel("asmodeus", LycanitesMobs.modInfo, "entity/asmodeus");

        // Set Rotation Centers:
        // Blender: Y = Z
        setPartCenter("head", 0F, 10F, 0F);
        setPartCenter("shield", 0F, 10F, 0F);
        setPartCenter("body", 0F, 10F, 0F);
        setPartCenter("turret", 0F, 7.72F, -0.995F);
        setPartCenter("weapon", 0F, 4.6F, -7.025F);

        setPartCenter("armleft", 6.8F, 15.6F, -2F);
        setPartCenter("armright", -6.8F, 15.6F, -2F);

        setPartCenter("legleftfront", 5.14256F, 9.2F, -3.2F);
        setPartCenter("legleftmiddle", 6.4F, 9.2F, 0F);
        setPartCenter("legleftback", 5.14256F, 9.2F, 3.2F);

        setPartCenter("legrightfront", -5.14256F, 9.2F, -3.2F);
        setPartCenter("legrightmiddle", -6.4F, 9.2F, 0F);
        setPartCenter("legrightback", -5.14256F, 9.2F, 3.2F);

        lockHeadX = true;
        lockHeadY = true;

        // Trophy:
        this.trophyScale = 0.1F;
        this.trophyOffset = new float[] {0.0F, 0.0F, -0.4F};
    }

    public int getShieldVariant() {
        return this.shieldVariant;
    }

    public void setItem(boolean item) {
        this.isItem = item;
    }

    public void setShieldActive(boolean active) {
        this.isActive = active;
    }

    public void setShieldVariant(int variant) {
        this.shieldVariant = variant;
    }


    // ==================================================
    //                Can Render Part
    // ==================================================
    /** Returns true if the part can be rendered on the base layer. **/
    public boolean canBaseRenderPart(String partName, Entity entity, boolean trophy) {
        switch (partName) {
            case "body":
                return this.isItem;
            case "shield":
                return this.isActive;
        }
        return false;
    }


    // ==================================================
    //                 Animate Part
    // ==================================================
    @Override
    public void animatePart(String partName, Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.animatePart(partName, entity, time, distance, loop, lookY, lookX, scale);
        float rotY = 0F;

        // Spinning Shield:
        if(partName.contains("shield")) {
            rotY += loop * 30;
            float shieldScale = 1.05F + ((0.5F + (MathHelper.sin(loop / 4) / 2)) / 8);
            this.scale(shieldScale, shieldScale, shieldScale);
        }

        // Apply Animations:
        this.rotate(0F, rotY, 0F);
    }

    @Override
    public float getRenderScale(Entity entity) {
        return (1.8F / 21F) * 2.625F;
    }
}
