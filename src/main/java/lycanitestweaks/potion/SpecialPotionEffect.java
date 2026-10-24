package lycanitestweaks.potion;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class SpecialPotionEffect extends PotionEffect {

    public static final String CLEANSE_NBT = "Cleansable";
    public static final String PURGE_NBT = "Purgeable";
    public static final String NEUTRAL_NBT = "Neutral";

    private boolean isCleansable = true;
    private boolean isPurgeable = true;
    private boolean isNeutral = false;

    public SpecialPotionEffect(Potion potionIn) {
        this(potionIn, 0, 0);
    }

    public SpecialPotionEffect(Potion potionIn, int durationIn) {
        this(potionIn, durationIn, 0);
    }

    public SpecialPotionEffect(Potion potionIn, int durationIn, int amplifierIn) {
        this(potionIn, durationIn, amplifierIn, false, true);
    }

    public SpecialPotionEffect(Potion potionIn, int durationIn, int amplifierIn, boolean ambientIn, boolean showParticlesIn) {
        super(potionIn, durationIn, amplifierIn, ambientIn, showParticlesIn);
    }

    public SpecialPotionEffect(PotionEffect other) {
        super(other);
        if(other instanceof SpecialPotionEffect) {
            SpecialPotionEffect potionEffect = (SpecialPotionEffect) other;
            this.setCleansable(potionEffect.isCleansable);
            this.setPurgeable(potionEffect.isPurgeable);
            this.setNeutral(potionEffect.isNeutral);
        }
    }

    public boolean getCleansable() {
        return this.isCleansable;
    }

    public boolean getPurgeable() {
        return this.isPurgeable;
    }

    public boolean getNeutral() {
        return this.isNeutral;
    }

    public SpecialPotionEffect setCleansable(boolean cleansable) {
        this.isCleansable = cleansable;
        return this;
    }

    public SpecialPotionEffect setPurgeable(boolean purgeable) {
        this.isPurgeable = purgeable;
        return this;
    }

    public SpecialPotionEffect setNeutral(boolean neutral) {
        this.isNeutral = neutral;
        return this;
    }

    @Override
    public void combine(PotionEffect other) {
        super.combine(other);
        if(other instanceof SpecialPotionEffect) {
            SpecialPotionEffect potionEffect = (SpecialPotionEffect) other;
            this.setCleansable(potionEffect.isCleansable);
            this.setPurgeable(potionEffect.isPurgeable);
            this.setNeutral(potionEffect.isNeutral);
        }
    }

    @Override
    public String toString() {
        String s = super.toString();

        if (!this.isCleansable) {
            s = s + ", Cleansable: false";
        }

        if (!this.isPurgeable) {
            s = s + ", Purgeable: false";
        }

        if (this.isNeutral) {
            s = s + ", Neutral: true";
        }

        return s;
    }

    @Override
    public boolean equals(Object other) {
        boolean specialEquals = false;
        if(other instanceof SpecialPotionEffect) {
            SpecialPotionEffect potionEffect = (SpecialPotionEffect) other;
            specialEquals = this.isCleansable == potionEffect.isCleansable
                    && this.isPurgeable == potionEffect.isPurgeable
                    && this.isNeutral == potionEffect.isNeutral;
        }

        return specialEquals && super.equals(other);
    }

    @Override
    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + (this.isCleansable ? 1 : 0);
        i = 31 * i + (this.isPurgeable ? 1 : 0);
        i = 31 * i + (this.isNeutral ? 1 : 0);
        return i;
    }

    @Override
    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
        nbt = super.writeCustomPotionEffectToNBT(nbt);

        nbt.setBoolean(CLEANSE_NBT, this.getCleansable());
        nbt.setBoolean(PURGE_NBT, this.getPurgeable());
        nbt.setBoolean(NEUTRAL_NBT, this.getNeutral());

        return nbt;
    }

    // I didn't like the final, especially the no potion sickness property, Vanilla HUD is a 2x2 grid while Dynamic Surroundings is 3x3x grid
//    @SideOnly(Side.CLIENT)
//    public static void renderSpecialProperties(SpecialPotionEffect potionEffect, int x, int y) {
//        Minecraft mc = Minecraft.getMinecraft();
//
//        final float propertyScale = 1.5F;
//        if(!potionEffect.getCleansable()) {
//            GlStateManager.pushMatrix();
//            GlStateManager.scale(1F / propertyScale, 1F / propertyScale, 1F / propertyScale);
//            mc.getRenderItem().renderItemAndEffectIntoGUI(
//                    new ItemStack(Items.MILK_BUCKET),
//                    (int) ((x + 1) * propertyScale),
//                    (int) ((y + 1) * propertyScale)
//            );
//            mc.getRenderItem().renderItemAndEffectIntoGUI(
//                    new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)),
//                    (int) ((x + 1) * propertyScale),
//                    (int) ((y + 1) * propertyScale)
//            );
//            GlStateManager.popMatrix();
//        }
//
//        if(!potionEffect.getPurgeable()) {
//            GlStateManager.pushMatrix();
//            GlStateManager.scale(1F / propertyScale, 1F / propertyScale, 1F / propertyScale);
//            mc.getRenderItem().renderItemAndEffectIntoGUI(
//                    new ItemStack(Items.TOTEM_OF_UNDYING),
//                    (int) ((x + 1) * propertyScale),
//                    (int) ((y + 11) * propertyScale)
//            );
//            mc.getRenderItem().renderItemAndEffectIntoGUI(
//                    new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)),
//                    (int) ((x + 1) * propertyScale),
//                    (int) ((y + 11) * propertyScale)
//            );
//            GlStateManager.popMatrix();
//        }
//
//        if(potionEffect.getNeutral()) {
//            if(ModLoadedUtil.potionCore.isLoaded()) {
//                final float potionScale = 0.75F;
//                GlStateManager.pushMatrix();
//                GlStateManager.scale(1F / potionScale, 1F / potionScale, 1F / potionScale);
//                PotionCoreHandler.getPotionSickness().renderInventoryEffect(
//                        (int) ((x + 1 - 12) * potionScale),
//                        (int) ((y + 21 - 12) * potionScale),
//                        potionEffect,
//                        mc
//                );
//                GlStateManager.popMatrix();
//                GlStateManager.pushMatrix();
//                GlStateManager.scale(1F / propertyScale, 1F / propertyScale, 1F / propertyScale);
//                mc.getRenderItem().renderItemAndEffectIntoGUI(
//                        new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)),
//                        (int) ((x + 1) * propertyScale),
//                        (int) ((y + 21) * propertyScale)
//                );
//                GlStateManager.popMatrix();
//            }
//            else {
//                GlStateManager.pushMatrix();
//                GlStateManager.scale(1F / propertyScale, 1F / propertyScale, 1F / propertyScale);
//                mc.getRenderItem().renderItemAndEffectIntoGUI(
//                        new ItemStack(Items.NETHER_STAR),
//                        (int) ((x + 1) * propertyScale),
//                        (int) ((y + 21) * propertyScale)
//                );
//                GlStateManager.popMatrix();
//            }
//        }
//    }
}
