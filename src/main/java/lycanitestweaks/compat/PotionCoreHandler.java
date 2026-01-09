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
        PotionEffect appliedEffect = event.getPotionEffect();
        if(appliedEffect.getPotion() != PotionArchery.INSTANCE) return;

        Potion replacementPotion = Potion.getPotionById(5); // Strength
        if(replacementPotion != null){
            creature.addPotionEffect(
                    new PotionEffect(
                            replacementPotion,
                            appliedEffect.getDuration(),
                            appliedEffect.getAmplifier(),
                            appliedEffect.getIsAmbient(),
                            appliedEffect.doesShowParticles()
                    )
            );
            event.setResult(Event.Result.DENY);
        }
    }
}
