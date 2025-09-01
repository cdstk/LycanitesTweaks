package lycanitestweaks.handlers.features.effect;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CuringEffectsHandler {

    @SubscribeEvent
    public static void curingEffectApplicable(PotionEvent.PotionApplicableEvent event){
        if(event.isCanceled()) return;
        EntityLivingBase entity = event.getEntityLiving();
        if(entity == null) return;
        if(entity.getEntityWorld().isRemote) return;
        Potion appliedPotion = event.getPotionEffect().getPotion();

        if(ForgeConfigHandler.mixinPatchesConfig.fixNVCuringBlindness){
            if(entity.isPotionActive(MobEffects.NIGHT_VISION)
                    && appliedPotion.equals(MobEffects.BLINDNESS)) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        if(ForgeConfigHandler.minorFeaturesConfig.repulsionWeight){
            if(appliedPotion.equals(ObjectManager.getEffect("repulsion"))){
                ExtendedEntity extendedEntity = ExtendedEntity.getForEntity(event.getEntityLiving());
                if(extendedEntity != null && extendedEntity.isPickedUp()){
                    if(extendedEntity.pickedUpByEntity instanceof BaseCreatureEntity) ((BaseCreatureEntity) extendedEntity.pickedUpByEntity).dropPickupEntity();
                    else extendedEntity.setPickedUpByEntity(null);
                }
            }
        }

        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.customItemCureEffectList) {
            if (entity.isPotionActive(ObjectManager.getEffect("cleansed")) &&
                    ForgeConfigProvider.getCleansedCureEffects().contains(appliedPotion.getRegistryName())) {
                event.setResult(Event.Result.DENY);
                return;
            } else if (entity.isPotionActive(ObjectManager.getEffect("immunization")) &&
                    ForgeConfigProvider.getImmunizationCureEffects().contains(appliedPotion.getRegistryName())) {
                event.setResult(Event.Result.DENY);
                return;
            }


            if (appliedPotion == ObjectManager.getEffect("cleansed"))
                Helpers.cureActiveEffectsFromResourceSet(entity, ForgeConfigProvider.getCleansedCureEffects());
            else if (appliedPotion == ObjectManager.getEffect("immunization"))
                Helpers.cureActiveEffectsFromResourceSet(entity, ForgeConfigProvider.getImmunizationCureEffects());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void creatureEffectBlacklist(PotionEvent.PotionApplicableEvent event) {
        if (event.isCanceled()) return;
        if(!(event.getEntityLiving() instanceof BaseCreatureEntity)) return;
        BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntityLiving();
        if (creature.getEntityWorld().isRemote) return;
        Potion appliedPotion = event.getPotionEffect().getPotion();

        if(creature.isBoss() || creature.isRareVariant()){
            if(ForgeConfigProvider.getBossBlacklistedEffects().contains(appliedPotion.getRegistryName())) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        if(creature.isMinion() && ForgeConfigProvider.getMinionBlacklistedEffects().contains(appliedPotion.getRegistryName())){
            event.setResult(Event.Result.DENY);
            return;
        }
    }
}
