package lycanitestweaks.handlers.features.effect;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.potion.PotionCripplingBase;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Predicate;

public class CripplingEffectsHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void cripplingBuffPurge(PotionEvent.PotionApplicableEvent event) {
        if (event.getResult() == Event.Result.ALLOW) return;
        EntityLivingBase entity = event.getEntityLiving();
        if (entity == null || entity.getEntityWorld().isRemote) return;
        Potion appliedPotion = event.getPotionEffect().getPotion();

        // bruh why does Rahovart alone keep failing these checks
        // it is denied here but he still has it
        if (appliedPotion instanceof PotionCripplingBase) {
            if (entity instanceof EntityPlayer
                    && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator())) {
                event.setResult(Event.Result.DENY);
                return;
            } else if ((entity instanceof BaseCreatureEntity && ((BaseCreatureEntity) entity).isBoss())) {
                event.setResult(Event.Result.DENY);
                return;
            }

            if (((PotionCripplingBase) appliedPotion).shouldRemoveBuffs()) {
                Helpers.removeAllPositiveEffects(entity);
                return;
            }
        }

        if (!appliedPotion.isBadEffect() && entityHasCripplingPotion(event.getEntityLiving(), PotionCripplingBase::shouldDenyBuffs))
            event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public static void cripplingCooldownDeny(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer() == null) return;
        if (entityHasCripplingPotion(event.getEntityLiving(), p ->
                p.getItemCooldown() > 0))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cripplingPiercingAttack(LivingAttackEvent event) {
        if (event.getEntityLiving() == null || event.getEntityLiving().world.isRemote) return;
        if (event.getSource().isDamageAbsolute() && event.getSource().isUnblockable()) return; //don't run endless loops
        if (!entityHasCripplingPotion(event.getEntityLiving(), p ->
                p.shouldPierceAll() || p.shouldPierceEnvironment() && event.getSource().getTrueSource() == null))
            return;

        event.setCanceled(true); //don't run the current attack

        //instead run a new attack
        DamageSource newSource = copyDamageSource(event.getSource()).setDamageBypassesArmor().setDamageIsAbsolute();
        event.getEntityLiving().attackEntityFrom(newSource, event.getAmount());
    }

    // Copies most properties. Death message should remain the same and pass any damageType string matches
    private static DamageSource copyDamageSource(DamageSource source){
        String damageType = source.damageType;
        Entity immSource = source.getImmediateSource();
        Entity trueSource = source.getTrueSource();

        DamageSource newSource;
        if(immSource != null)
            newSource =  new EntityDamageSourceIndirect(damageType, immSource, trueSource);
        else if(trueSource != null)
            newSource =  new EntityDamageSource(damageType, trueSource);
        else
            newSource = new DamageSource(damageType);

        if(source.canHarmInCreative())
            newSource.setDamageAllowedInCreativeMode();
        if(source.isProjectile())
            newSource.setProjectile();
        if(source.isFireDamage())
            newSource.setFireDamage();
        if(source.isExplosion())
            newSource.setExplosion();
        if(source.isDifficultyScaled())
            newSource.setDifficultyScaled();
        if(newSource instanceof EntityDamageSource && source instanceof EntityDamageSource && ((EntityDamageSource) source).getIsThornsDamage())
            ((EntityDamageSource) newSource).setIsThornsDamage();

        //not copying damage location bc it's not really doable and only exists for checking if player can block it with shield, which we gladly disallow
        //not copying hunger dmg bc it would require a mixin and i don't care enough about it

        return newSource;
    }

    /**
     * runs a check over all PotionCripplingBase that match the given predicate.
     * If the given entity has any of those, return true, otherwise return false
     */
    private static boolean entityHasCripplingPotion(EntityLivingBase entity, Predicate<PotionCripplingBase> predicate){
        return PotionCripplingBase.instanceSet.stream().filter(predicate).anyMatch(entity::isPotionActive);
    }
}
