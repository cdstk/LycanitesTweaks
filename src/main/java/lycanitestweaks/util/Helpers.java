package lycanitestweaks.util;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.item.ChargeItem;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.BaublesHandler;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class Helpers {

    private static HashMap<String, ArrayList<String>> chargeElementsMap = null;
    private static HashMap<String, ArrayList<String>> creatureElementsMap = null;

    public static boolean hasSoulgazerEquiped(EntityLivingBase target){
        return Helpers.hasSoulgazerEquiped(target, false);
    }

    public static boolean hasSoulgazerEquiped(EntityLivingBase target, boolean ignoreHand){
        if(!ignoreHand){
            if(target.getHeldItemMainhand().getItem() instanceof ItemSoulgazer) return true;
            if(target.getHeldItemOffhand().getItem() instanceof ItemSoulgazer) return true;
        }

        if(ModLoadedUtil.isBaublesLoaded()) {
            if (target instanceof EntityPlayer) return BaublesHandler.hasSoulgazerBauble((EntityPlayer) target);
        }
        return false;
    }

    public static void cureActiveEffectsFromResourceSet(EntityLivingBase entity, Set<ResourceLocation> curingSet){
        Set<Potion> potionsToRemove = entity.getActivePotionEffects().stream()
                .map(PotionEffect::getPotion)
                .filter(potion -> curingSet.contains(potion.getRegistryName()))
                .collect(Collectors.toSet());
        potionsToRemove.forEach(entity::removePotionEffect);
    }

    public static void removeAllPositiveEffects(EntityLivingBase entity){
        ArrayList<Potion> toRemove = new ArrayList<>();

        for(PotionEffect effect : entity.getActivePotionEffects())
            if(!effect.getPotion().isBadEffect()) toRemove.add(effect.getPotion());

        for(Potion potion : toRemove) entity.removePotionEffect(potion);
    }

    public static HashMap<String, ArrayList<String>> getChargeElementsMap(){
        if(Helpers.chargeElementsMap == null){
            Helpers.chargeElementsMap = new HashMap<>();
            ObjectManager.items.forEach((name, item) -> {
                if(item instanceof ChargeItem){
                    ((ChargeItem) item).getElements().forEach(elementInfo -> {
                        String elementString = elementInfo.name;
                        String chargeString = ((ChargeItem) item).itemName.trim();
                        ArrayList<String> projectiles;

                        if(!Helpers.chargeElementsMap.containsKey(elementString)) projectiles = new ArrayList<>();
                        else projectiles = Helpers.chargeElementsMap.get(elementString);
                        projectiles.add(chargeString);
                        Helpers.chargeElementsMap.put(elementString, projectiles);
                    });
                }
            });
            if(ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "chargeElementsMap: {}", Helpers.chargeElementsMap);
        }
        return Helpers.chargeElementsMap;
    }

    public static HashMap<String, ArrayList<String>> getCreatureElementsMap(){
        if(Helpers.creatureElementsMap == null){
            Helpers.creatureElementsMap = new HashMap<>();
            CreatureManager.getInstance().creatures.forEach((creatureName, creatureInfo) -> {
                for(ElementInfo elementInfo : creatureInfo.elements){
                    ArrayList<String> creatures;

                    if(!Helpers.creatureElementsMap.containsKey(elementInfo.name)) creatures = new ArrayList<>();
                    else creatures = Helpers.creatureElementsMap.get(elementInfo.name);
                    creatures.add(creatureName);
                    Helpers.creatureElementsMap.put(elementInfo.name, creatures);
                }
            });
            if(ForgeConfigHandler.debug.debugLoggerAutomatic) LycanitesTweaks.LOGGER.log(Level.INFO, "creatureElementsMap: {}", Helpers.creatureElementsMap);
        }
        return Helpers.creatureElementsMap;
    }

    public static double getImperfectHostileChance(ExtendedPlayer extendedPlayer, CreatureInfo creatureInfo){
        double hostileChance = ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectHostileBaseChance;
        if (ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectHostileBaseChance != 0.0D && extendedPlayer.getBeastiary().hasKnowledgeRank(creatureInfo.getName(), 1)) {
            hostileChance -= extendedPlayer.getBeastiary().getCreatureKnowledge(creatureInfo.getName()).experience * ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectHostileChanceModifier;
        }
        return hostileChance;
    }

    public static double getImperfectStatsChance(ExtendedPlayer extendedPlayer, CreatureInfo creatureInfo){
        double lowerStatsChance = ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectStatsBaseChance;
        if(ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectStatsChanceModifier != 0.0D && extendedPlayer.getBeastiary().hasKnowledgeRank(creatureInfo.getName(), 1)){
            lowerStatsChance -= extendedPlayer.getBeastiary().getCreatureKnowledge(creatureInfo.getName()).experience * ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectStatsChanceModifier;
        }
        return lowerStatsChance;
    }
}