package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.inventory.InventoryCreature;
import lycanitestweaks.util.IBaseCreatureEntity_VanillaEquipmentMixin;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_VanillaEquipmentMixin extends EntityLiving {

    /** Used to sync the Mainhand Equipment slot of this creature. **/
    @Unique
    private static final DataParameter<ItemStack> EQUIPMENT_WEAPON = EntityDataManager.createKey(BaseCreatureEntity.class, DataSerializers.ITEM_STACK);
    /** Used to sync the Offhand Equipment slot of this creature. **/
    @Unique
    private static final DataParameter<ItemStack> EQUIPMENT_OFFHAND = EntityDataManager.createKey(BaseCreatureEntity.class, DataSerializers.ITEM_STACK);

    @Shadow(remap = false)
    public InventoryCreature inventory;

    public BaseCreatureEntity_VanillaEquipmentMixin(World world) {
        super(world);
    }

    @Inject(
            method = "entityInit",
            at = @At("TAIL")
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_entityInitHandData(CallbackInfo ci){
        dataManager.register(EQUIPMENT_WEAPON, ItemStack.EMPTY);
        dataManager.register(EQUIPMENT_OFFHAND, ItemStack.EMPTY);
        // Store public references to mixin statics lololol (The DataParameters need to be init here)
        IBaseCreatureEntity_VanillaEquipmentMixin.handDataParameters.putIfAbsent("weapon", EQUIPMENT_WEAPON);
        IBaseCreatureEntity_VanillaEquipmentMixin.handDataParameters.putIfAbsent("offhand", EQUIPMENT_OFFHAND);
    }

    @Inject(
            method = "setCurrentItemOrArmor",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/inventory/InventoryCreature;setEquipmentStack(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_setCurrentItemOrArmorOffhand(int slot, ItemStack itemStack, CallbackInfo ci, @Local LocalRef<String> type){
        if(slot == 5) type.set("offhand");
    }

    @ModifyExpressionValue(
            method = "getMeleeCooldown",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;getAttributeValue()D", remap = true),
            remap = false
    )
    public double lycanitesTweaks_lycanitesMobsBaseCreatureEntity_getMeleeCooldownMinimum(double attackSpeed){
        return Math.max(1, attackSpeed);
    }

    @ModifyExpressionValue(
            method = "attackEntityAsMob",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getAttackDamage(D)F"),
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobCalcByCreature(float damage, Entity target){
        if(target instanceof EntityLivingBase) {
            damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) target).getCreatureAttribute());
        }
        return damage;
    }

    @ModifyConstant(
            method = "attackEntityAsMob",
            constant = @Constant(intValue = 0, ordinal = 3), // Was targeting return statements for some reason
            remap = false
    )
    private int lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobKnockbackEnchantment(int enchantmentKnockback, Entity target){
        if (target instanceof EntityLivingBase) {
            enchantmentKnockback += EnchantmentHelper.getKnockbackModifier(this);
        }
        return enchantmentKnockback;
    }

    @WrapOperation(
            method = "attackEntityAsMob",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_attackEntityAsMobApplyKnockback(Entity target, double strength, double xRatio, double zRatio, Operation<Void> original, @Local(name = "enchantmentKnockback") int enchantmentKnockback){
        if(target instanceof EntityLivingBase)
            ((EntityLivingBase) target).knockBack(
                    this,
                    enchantmentKnockback * 0.5F,
                    MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.F),
                    -MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.F)
            );
    }


    // Search both Lycanites inventory and vanilla slots
    @Unique
    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        ItemStack itemStack = ItemStack.EMPTY;
        if(this.inventory != null){
            switch (slotIn){
                case MAINHAND: itemStack = this.inventory.getEquipmentStack("weapon"); break;
                case OFFHAND: itemStack = this.inventory.getEquipmentStack("offhand"); break;
                case HEAD: itemStack = this.inventory.getEquipmentStack("head"); break;
                case CHEST: itemStack = this.inventory.getEquipmentStack("chest"); break;
                case LEGS: itemStack = this.inventory.getEquipmentStack("legs"); break;
                case FEET: itemStack = this.inventory.getEquipmentStack("feet"); break;
            }
        }
        return (itemStack == ItemStack.EMPTY) ? super.getItemStackFromSlot(slotIn) : itemStack;
    }
}
