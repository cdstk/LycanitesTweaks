package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.advancedarmor;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_ProjectilesMeleeMixin extends EntityLiving {

    @Unique
    private boolean lycanitesTweaks$doRangedMelee = false;

    public BaseCreatureEntity_ProjectilesMeleeMixin(World world) {
        super(world);
    }

    @WrapMethod(
            method = "doRangedDamage",
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageMelee(Entity target, EntityThrowable projectile, float damage, boolean noPierce, Operation<Boolean> original){
        this.lycanitesTweaks$doRangedMelee = this.getHeldItemMainhand().isItemEnchanted();
        boolean success = original.call(target, projectile, damage, noPierce);
        this.lycanitesTweaks$doRangedMelee = false;
        return success;
    }

    @ModifyVariable(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getPierce()D"),
            name = "damage",
            remap = false
    )
    private float lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageCalcByCreature(float damage, Entity target){
        if(target instanceof EntityLivingBase && this.lycanitesTweaks$doRangedMelee) {
            damage += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) target).getCreatureAttribute());
        }
        return damage;
    }

    @ModifyArg(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;getDamageSource(Lnet/minecraft/util/EntityDamageSource;Z)Lnet/minecraft/util/DamageSource;"),
            index = 0,
            remap = false
    )
    private EntityDamageSource lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageSourceMob(EntityDamageSource nestedDamageSource){
        if(this.lycanitesTweaks$doRangedMelee) {
            return null;
        }
        return nestedDamageSource;
    }

    @Inject(
            method = "doRangedDamage",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_doRangedDamageKnockbackEnchantment(Entity target, EntityThrowable projectile, float damage, boolean noPierce, CallbackInfoReturnable<Boolean> cir, @Local(name = "success") boolean success){
        if (success && this.lycanitesTweaks$doRangedMelee) {
            if(target instanceof EntityLivingBase) {
                int enchantmentKnockback = EnchantmentHelper.getKnockbackModifier(this);
                if(enchantmentKnockback > 0) {
                    ((EntityLivingBase) target).knockBack(
                            this,
                            enchantmentKnockback * 0.5F,
                            MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.F),
                            -MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.F)
                    );
                }
            }
            // Fire Enchanted Held Weapons:
            int fireEnchantDuration = EnchantmentHelper.getFireAspectModifier(this);
            if(fireEnchantDuration > 0)
                target.setFire(fireEnchantDuration * 4);
            EnchantmentHelper.applyArthropodEnchantments(this, target);
        }
    }
}
