package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class AttributesHandler {

    public static final IAttribute PIERCE = (new RangedAttribute(null, "generic.pierce", 0.0D, 0.0D, 1024.0D)).setShouldWatch(true);

    @SubscribeEvent
    public static void onEntityConstruction(EntityEvent.EntityConstructing event) {
        if(event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) event.getEntity();
            if(entityLivingBase instanceof BaseCreatureEntity) return;

            entityLivingBase.getAttributeMap().registerAttribute(BaseCreatureEntity.DEFENSE);
            entityLivingBase.getEntityAttribute(BaseCreatureEntity.DEFENSE).setBaseValue(0);
            entityLivingBase.getAttributeMap().registerAttribute(PIERCE);
//            entityLivingBase.getAttributeMap().registerAttribute(BaseCreatureEntity.RANGED_SPEED);
        }
    }

    // Different calc from Lycanites Pierce where it reduces original portion and modifies iframes
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if(event.isCanceled()) return;
        if(event.getSource().getTrueSource() instanceof BaseCreatureEntity) return;
        if(!(event.getSource().getTrueSource() instanceof EntityLivingBase)) return;
        if(event.getSource().isUnblockable() && event.getSource().isDamageAbsolute()) return; // Assume it's piercing

        DamageSource pierceSource = null;
        DamageSource incomingSource = event.getSource();
        EntityLivingBase attacker = (EntityLivingBase) incomingSource.getTrueSource();
        EntityLivingBase victim = event.getEntityLiving();
        if(incomingSource.isProjectile()) {
            if(incomingSource.getImmediateSource() instanceof BaseProjectileEntity) return;
            pierceSource = causeProjectilePierceDamage(attacker, incomingSource.getImmediateSource(), incomingSource.getDamageType());
        }
        else if(incomingSource.damageType.equals("mob") || incomingSource.damageType.equals("player")) {
            pierceSource = causeMeleePierceDamage(attacker, incomingSource.getDamageType());
        }

        if(pierceSource == null || victim.isActiveItemStackBlocking()) return;

        IAttributeInstance pierce = attacker.getEntityAttribute(PIERCE);
        if(pierce != null) {
            float pierceDamage = (float) pierce.getAttributeValue();
            pierceDamage = Math.min(pierceDamage, event.getAmount());
            if(pierceDamage > 0)
                victim.attackEntityFrom(pierceSource, pierceDamage);
        }
    }

    // Should be after armor calc but before absorption, but that's not possible so after absorption
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        EntityLivingBase victim = event.getEntityLiving();
        if(victim instanceof BaseCreatureEntity) return;

        IAttributeInstance defense = victim.getEntityAttribute(BaseCreatureEntity.DEFENSE);
        if(defense != null && event.getAmount() > 1F) {
            float damageReduction = (float) defense.getAttributeValue();
            if(victim.isActiveItemStackBlocking()) {
                damageReduction = Math.max(1, damageReduction);
                damageReduction *= 4F;
            }
            event.setAmount(Math.max(1F, event.getAmount() - damageReduction));
        }
    }

    public static DamageSource causeMeleePierceDamage(EntityLivingBase attacker, String damageType) {
        return new EntityDamageSource(damageType, attacker).setDamageBypassesArmor().setDamageIsAbsolute();
    }

    public static DamageSource causeProjectilePierceDamage(Entity attacker, Entity projectile, String damageType) {
        return new EntityDamageSourceIndirect(damageType, projectile, attacker).setProjectile().setDamageBypassesArmor().setDamageIsAbsolute();
    }

    /**
     * Helper method for use within an Entity class's attack methods
     * <p>
     * Do a piercing attack before the vanilla attack.
     * If the piercing amount can deal the maximum possible damage,
     * the vanilla attack should use the piercing damage source.
     * Else deal piercing damage and don't modify the vanilla attack.
     * <p>
     * Should only be used after checking if a piercing attack should be dealt
     *
     * @param victim Entity to damage
     * @param pierceSource Damage Source for piercing damage
     * @param vanillaSource Damage Source for vanilla melee/projectile damage
     * @param pierceAmount Maximum piercing damage dealt, Lycanites entity double should be cleaned for float comparison
     * @param vanillaAmount Maximum possible damage dealt
     * @return DamageSource to use in primary attackEntityFrom
     */
    public static DamageSource doPreemptivePierceAttack(Entity victim, DamageSource pierceSource, DamageSource vanillaSource, float pierceAmount, float vanillaAmount) {
        // 1 attack with 100% Pierce
        if(pierceAmount == vanillaAmount) {
            return pierceSource;
        }
        // Preemptive pierce attack, lower base damage goes first for iframes
        else {
            victim.attackEntityFrom(pierceSource, pierceAmount);
            return vanillaSource;
        }
    }
}
