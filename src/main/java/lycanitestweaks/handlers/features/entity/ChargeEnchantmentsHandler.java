package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
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
        if(shooter == null) return;

        ItemStack itemStack = shooter.getHeldItemMainhand();
        if(!(itemStack.getItem() instanceof ItemBow) && itemStack.getItemUseAction() != EnumAction.BOW) {
            itemStack = shooter.getActiveItemStack();
        }
        if(!(itemStack.getItem() instanceof ItemBow) && itemStack.getItemUseAction() != EnumAction.BOW) {
            return;
        }

        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemStack);
        if(powerLevel > 0) {
            baseProjectileEntity.setDamage((int) (baseProjectileEntity.damage + powerLevel * 0.5D + 0.5D));
        }

        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemStack);
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

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, baseProjectileEntity.getThrower().getHeldItemMainhand()) > 0) {
            victim.setFire(5);
        }
    }
}
