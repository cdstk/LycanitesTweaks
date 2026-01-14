package lycanitestweaks.compat;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.tmtravlr.potioncore.potion.PotionArchery;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class PotionCoreHandler {

    @SubscribeEvent
    public static void onTrueShotEffect(PotionEvent.PotionApplicableEvent event){
        if(!(event.getEntityLiving() instanceof BaseCreatureEntity)) return;
        BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntityLiving();
        if(creature.getEntityWorld().isRemote) return;
        Potion trueshotPotion = PotionArchery.INSTANCE;
        Potion strengthPotion = Potion.getPotionById(5); // Strength
        if(strengthPotion == null) return;

        PotionEffect appliedEffect = event.getPotionEffect();
        if(creature.isPotionActive(strengthPotion) && appliedEffect.getPotion() == trueshotPotion){
            creature.addPotionEffect(
                    new PotionEffect(
                            strengthPotion,
                            appliedEffect.getDuration(),
                            appliedEffect.getAmplifier(),
                            appliedEffect.getIsAmbient(),
                            appliedEffect.doesShowParticles()
                    )
            );
            event.setResult(Event.Result.DENY);
        }
        else if(creature.isPotionActive(trueshotPotion) && appliedEffect.getPotion() == strengthPotion){
            PotionEffect currentEffect = creature.getActivePotionEffect(trueshotPotion);
            creature.addPotionEffect(
                    new PotionEffect(
                            strengthPotion,
                            currentEffect.getDuration(),
                            currentEffect.getAmplifier(),
                            currentEffect.getIsAmbient(),
                            currentEffect.doesShowParticles()
                    )
            );
            creature.removePotionEffect(trueshotPotion);
        }
    }
}
