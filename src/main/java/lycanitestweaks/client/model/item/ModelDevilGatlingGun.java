package lycanitestweaks.client.model.item;

import com.lycanitesmobs.LycanitesMobs;
import lycanitestweaks.client.model.ModelObjOldGeneric;
import lycanitestweaks.client.renderer.RenderContext;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ModelDevilGatlingGun extends ModelObjOldGeneric {

    // ==================================================
    //                    Constructors
    // ==================================================
    public ModelDevilGatlingGun() {
        this(1.0F);
    }

    public ModelDevilGatlingGun(float shadowSize) {
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


    // ==================================================
    //                Can Render Part
    // ==================================================
    /** Returns true if the part can be rendered on the base layer. **/
    public boolean canBaseRenderPart(String partName, Entity entity, boolean trophy) {
        switch (partName) {
            case "turret":
            case "weapon":
                return true;
        }
        return false;
    }


    // ==================================================
    //                 Animate Part
    // ==================================================
    @Override
    public void animatePart(String partName, Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale) {
        super.animatePart(partName, entity, time, distance, loop, lookY, lookX, scale);
        float rotZ = 0F;

        // Turret:
        if(partName.contains("turret") || partName.contains("weapon")) {
            if(partName.contains("weapon"))
                this.centerPartToPart("weapon", "turret");
            float xRotation = 0F;
            if(entity instanceof EntityLivingBase) {
                xRotation = (float) Math.toDegrees(lookX / (180F / Math.PI)) - 25F;
            }
            this.rotate(xRotation, (float) Math.toDegrees(lookY / (180F / Math.PI)), 0);
            if(partName.contains("weapon"))
                this.uncenterPartToPart("weapon", "turret");
        }

        // Spinning Weapon:
        if(entity instanceof EntityLivingBase && partName.contains("weapon")) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            ItemStack itemStack = entityLivingBase.getActiveItemStack();
            if(itemStack.getItem() == LycanitesTweaksRegistry.devilGatlingGun && itemStack == RenderContext.currentRenderStack) {
//                rotZ -= loop * 7.5F;
                rotZ -= loop * Math.min(entityLivingBase.getItemInUseCount(), 20F);
            }
        }

        // Apply Animations:
        this.rotate(0F, 0F, rotZ);
    }

    @Override
    public float getRenderScale(Entity entity) {
        return (1.8F / 21F) * 3.5F;
    }
}
