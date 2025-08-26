package lycanitestweaks.util;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public interface IObjectImperfectSummoning_Mixin {

    UUID lycanitesTweaks$IP_DMG_UUID = UUID.fromString("e45e8101-a2df-4e82-b19c-b4c73fe3596c");
    UUID lycanitesTweaks$IP_HP_UUID = UUID.fromString("d6fcfe2e-40ef-47ab-9f55-798351be490b");
    UUID lycanitesTweaks$IP_RS_UUID = UUID.fromString("4a99583b-34a6-446c-ab37-040b7c5536c4");
    String lycanitesTweaks$IP_DMG_ID = LycanitesTweaks.MODID + ":imperfectDamage";
    String lycanitesTweaks$IP_HP_ID = LycanitesTweaks.MODID + ":imperfectHealth";
    String lycanitesTweaks$IP_RS_ID = LycanitesTweaks.MODID + ":imperfectRangedSpeed";

    default void lycanitesTweaks$applyImperfectSummoning(TameableCreatureEntity entityCreature, EntityPlayer player, boolean sendMessage){
        ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);

        if(extendedPlayer != null
            && ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.imperfectMinionNerfs
            && !extendedPlayer.getBeastiary().hasKnowledgeRank(entityCreature.creatureInfo.getName(), ForgeConfigHandler.majorFeaturesConfig.imperfectSummoningConfig.variantSummonRank))
        {
            if (player.getEntityWorld().rand.nextDouble() < Helpers.getImperfectHostileChance(extendedPlayer, entityCreature.creatureInfo)) {
                entityCreature.setOwnerId(null);
                entityCreature.setRevengeTarget(player);
                if(sendMessage) player.sendStatusMessage(new TextComponentTranslation("message.summon.imperfect.hostile"), true);
            }
            else {
                double lowerStatsChance = Helpers.getImperfectStatsChance(extendedPlayer, entityCreature.creatureInfo);
                if (player.getEntityWorld().rand.nextDouble() < lowerStatsChance) {
                    lowerStatsChance = Math.min(0.95D, lowerStatsChance);
                    if (player.getEntityWorld().rand.nextBoolean()) {
                        entityCreature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(lycanitesTweaks$IP_HP_UUID, lycanitesTweaks$IP_HP_ID, -lowerStatsChance, 2));
                        entityCreature.setHealth(entityCreature.getMaxHealth());
                        if(sendMessage) player.sendStatusMessage(new TextComponentTranslation("message.summon.imperfect.health"), true);
                    } else {
                        entityCreature.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier(lycanitesTweaks$IP_DMG_UUID, lycanitesTweaks$IP_DMG_ID, -lowerStatsChance, 2));
                        entityCreature.getEntityAttribute(BaseCreatureEntity.RANGED_SPEED).applyModifier(new AttributeModifier(lycanitesTweaks$IP_RS_UUID, lycanitesTweaks$IP_RS_ID, -lowerStatsChance, 2));
                        if(sendMessage) player.sendStatusMessage(new TextComponentTranslation("message.summon.imperfect.attack"), true);
                    }
                }
            }
        }
    }
}
