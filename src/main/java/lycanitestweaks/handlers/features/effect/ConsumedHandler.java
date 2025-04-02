package lycanitestweaks.handlers.features.effect;

import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.potion.PotionConsumed;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ConsumedHandler {

    private static DamageSource source;
    private static float damage = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void consumedBuffPurge(PotionEvent.PotionApplicableEvent event) {
        if (event.isCanceled()) return;
        EntityLivingBase entity = event.getEntityLiving();
        if (entity == null) return;
        if (entity.getEntityWorld().isRemote) return;

        if (ForgeConfigHandler.server.effectsConfig.consumedDeniesBuffs && entity.isPotionActive(PotionConsumed.INSTANCE))
            if (event.getPotionEffect().getPotion().isBeneficial()) event.setResult(Event.Result.DENY);

        if (ForgeConfigHandler.server.effectsConfig.consumedRemovesBuffs && event.getPotionEffect().getPotion() == PotionConsumed.INSTANCE)
            Helpers.removeAllPositiveEffects(entity);
    }

    @SubscribeEvent
    public static void consumedCooldown(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote) return;

        if (ForgeConfigHandler.server.effectsConfig.consumedItemCooldown > 0 && player.isPotionActive(PotionConsumed.INSTANCE)) {
            player.getCooldownTracker().setCooldown(player.getHeldItemMainhand().getItem(), ForgeConfigHandler.server.effectsConfig.consumedItemCooldown);
            player.getCooldownTracker().setCooldown(player.getHeldItemOffhand().getItem(), ForgeConfigHandler.server.effectsConfig.consumedItemCooldown);
        }
    }

    @SubscribeEvent
    public static void consumedCooldownDeny(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer() == null) return;
        if (ForgeConfigHandler.server.effectsConfig.consumedItemCooldown > 0 && event.getEntityPlayer().isPotionActive(PotionConsumed.INSTANCE))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void consumedPiercingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() == null || event.getEntityLiving().getEntityWorld().isRemote) return;
        if (!event.getEntityLiving().isPotionActive(PotionConsumed.INSTANCE)) return;

        if ((ForgeConfigHandler.server.effectsConfig.consumedPiecingEnvironment && event.getSource().getTrueSource() == null) ||
                ForgeConfigHandler.server.effectsConfig.consumedPiecingAll) {
            source = event.getSource();
            damage = event.getAmount();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void consumedPiercingDamage(LivingDamageEvent event) {
        if (event.getEntityLiving() == null || event.getEntityLiving().getEntityWorld().isRemote) return;
        if (!event.getEntityLiving().isPotionActive(PotionConsumed.INSTANCE)) return;

        if(source == event.getSource()) {
            event.setAmount(Math.max(damage, event.getAmount()));
            damage = 0;
        }
    }
}
