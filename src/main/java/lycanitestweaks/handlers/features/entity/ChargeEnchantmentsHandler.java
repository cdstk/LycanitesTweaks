package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChargeEnchantmentsHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onChargeEntityJoinWorld(EntityJoinWorldEvent event) {
        if(event.getWorld().isRemote) return;
        if(!(event.getEntity() instanceof BaseProjectileEntity)) return;
        BaseProjectileEntity baseProjectileEntity = (BaseProjectileEntity) event.getEntity();
        EntityLivingBase shooter = baseProjectileEntity.getThrower();
        if(shooter == null || shooter instanceof EntityPlayer) return; // TODO Rework Charge Shooting Staff and Holding Offhand Bow

        if(shooter.getHeldItemMainhand().getItemUseAction() == EnumAction.BOW) {
            int materialDamage = 2; // Was going to integrate through materials, but would be insane
            baseProjectileEntity.setDamage(baseProjectileEntity.damage + materialDamage);
        }

        int powerLevel = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, shooter);
        if(powerLevel > 0) {
            baseProjectileEntity.setDamage((int) (baseProjectileEntity.damage + powerLevel * 0.5D + 0.5D));
        }

        int punchLevel = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, shooter);
        if(punchLevel > 0) {
            baseProjectileEntity.knockbackChance += punchLevel;
        }
    }

    @SubscribeEvent
    public static void onChargeHitDamage(LivingDamageEvent event) {
        if(!(event.getSource().getImmediateSource() instanceof BaseProjectileEntity)) return;
        if(!"thrown".equals(event.getSource().damageType)) return;

        BaseProjectileEntity baseProjectileEntity = (BaseProjectileEntity)event.getSource().getImmediateSource();
        if(baseProjectileEntity.getThrower() == null) return;
        EntityLivingBase victim = event.getEntityLiving();

        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, baseProjectileEntity.getThrower()) > 0) {
            victim.setFire(5);
        }


    }
}
