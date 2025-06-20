package lycanitestweaks.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class PotionCripplingBase extends PotionBase {

    public static HashSet<PotionCripplingBase> instanceSet = new HashSet<>();

    protected double healthModifier;
    protected int itemCooldown;
    protected boolean deniesBuffs;
    protected boolean removesBuffs;
    protected boolean piercingEnvironment;
    protected boolean piercingAll;

    public PotionCripplingBase(String name, boolean isBadEffect, int liquidColor) {
        super(name, isBadEffect, liquidColor);
    }

    public static void addInstance(PotionCripplingBase potion){
        PotionCripplingBase.instanceSet.add(potion);
    }

    public int getItemCooldown(){
        return this.itemCooldown;
    }

    public boolean shouldDenyBuffs(){
        return this.deniesBuffs;
    }

    public boolean shouldRemoveBuffs(){
        return this.removesBuffs;
    }

    public boolean shouldPierceEnvironment(){
        return this.piercingEnvironment;
    }

    public boolean shouldPierceAll(){
        return this.piercingAll;
    }

    @Override
    @Nonnull
    public List<ItemStack> getCurativeItems(){
        return new ArrayList<>();
    }

    @Override
    public void applyAttributesModifiersToEntity(@Nonnull EntityLivingBase entityLivingBaseIn, @Nonnull AbstractAttributeMap attributeMapIn, int amplifier){
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth()) {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
        }
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier){
        return modifier.getAmount();
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBase, int amplifier) {
        if(entityLivingBase.world.isRemote) return;

        if(entityLivingBase instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLivingBase;

            if (this.itemCooldown > 0) {
                player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), this.itemCooldown);
                player.getCooldownTracker().setCooldown(player.getHeldItemOffhand().getItem(), this.itemCooldown);
            }
        }
    }
}
