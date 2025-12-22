package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.Variant;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.loot.AddCountFromMobLevels;
import lycanitestweaks.loot.ApplyVariantItemDropsScale;
import lycanitestweaks.loot.EnchantWithMobLevels;
import lycanitestweaks.loot.HasMobLevels;
import lycanitestweaks.loot.IsVariant;
import lycanitestweaks.loot.RandomChanceWithVariantDropScale;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.EnchantRandomly;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityLootHandler {

    private static final LootCondition[] nullCond = new LootCondition[0];

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingExperienceDropEvent(LivingExperienceDropEvent event) {
        float bonus = ForgeConfigHandler.server.lootConfig.scaleXPWithLevelsBonus;
        if(bonus <= 0F) return;

        EntityPlayer player = event.getAttackingPlayer();
        if(player == null) return;
        if(!(event.getEntityLiving() instanceof BaseCreatureEntity)) return;
        if(event.getDroppedExperience() <= 0) return;

        BaseCreatureEntity creature = (BaseCreatureEntity) event.getEntityLiving();
        if(creature.getLevel() <= 1) return;

        if(creature.isRareVariant()) bonus *= (float) Variant.RARE_EXPERIENCE_SCALE;
        else if(creature.getVariantIndex() != 0) bonus *= (float) Variant.UNCOMMON_DROP_SCALE;

        if((creature.isBossAlways() && ForgeConfigHandler.server.lootConfig.scaleXPWithLevelsMainBoss)
                || (creature.spawnedAsBoss && ForgeConfigHandler.server.lootConfig.scaleXPWithLevelsSpawnedAsBoss))
            bonus *= (float) Variant.RARE_EXPERIENCE_SCALE;

        event.setDroppedExperience((int)((float)event.getDroppedExperience() * (1.0F + bonus * creature.getLevel())));
    }

    // JSON examples
    @SubscribeEvent
    public static void removeDefaultBossLoot(LootTableLoadEvent event){
        if(!ForgeConfigHandler.server.lootConfig.registerBossWithLevelsLootTables) {
            if (LycanitesMobs.modid.equals(event.getName().getNamespace())) {
                switch (event.getName().getPath()) {
                    case "amalgalich":
                        event.getTable().removePool("amalgalich_emeralds_with_mob_levels");
                        event.getTable().removePool("amalgalich_book_with_mob_levels");
                        break;
                    case "asmodeus":
                        event.getTable().removePool("asmodeus_emerald_with_mob_levels");
                        event.getTable().removePool("asmodeus_book_with_mob_levels");
                        break;
                    case "rahovart":
                        event.getTable().removePool("rahovart_emerald_with_mob_levels");
                        event.getTable().removePool("rahovart_book_with_mob_levels");
                        break;
                }
            }
        }
        if(!ForgeConfigHandler.server.lootConfig.registerBossSoulkeyLootTables){
            if (LycanitesMobs.modid.equals(event.getName().getNamespace())) {
                switch (event.getName().getPath()) {
                    case "amalgalich":
                        event.getTable().removePool("amalgalich_thousand_soulkey");
                        break;
                    case "asmodeus":
                        event.getTable().removePool("asmodeus_thousand_soulkey");
                        break;
                    case "rahovart":
                        event.getTable().removePool("rahovart_thousand_soulkey");
                        break;
                }
            }
        }
    }

    // LootTableLoadEvent examples
    // Using new/split lycanitestweaks:add_count_from_mob_levels
    @SubscribeEvent
    public static void addDefaultBossLoot(LootTableLoadEvent event){
        if (!LycanitesMobs.modid.equals(event.getName().getNamespace())) return;

        if(ForgeConfigHandler.server.lootConfig.registerSpawnedAsBossRandomBookLootTables){
            LootPool bookTable = new LootPool(
                    new LootEntry[]{
                            new LootEntryItem(Items.BOOK, 1, 0,
                                    new LootFunction[]{new EnchantRandomly(nullCond, null)},
                                    nullCond,
                                    LycanitesTweaks.MODID + ":enchant_with_mob_levels_book")},
                    new LootCondition[]{new IsVariant(-1, false, false, true)},
                new RandomValueRange(ForgeConfigHandler.server.lootConfig.spawnedAsBossBookRolls), new RandomValueRange(0),
                    LycanitesTweaks.MODID + "_random_boss_book");
            event.getTable().addPool(bookTable);
        }

        if(ForgeConfigHandler.server.lootConfig.registerSpawnedAsBossWithLevelsLootTables) {
            LootPool bookTable = new LootPool(
                    new LootEntry[]{
                            new LootEntryItem(Items.BOOK, 1, 0,
                                    new LootFunction[]{
                                            new EnchantWithMobLevels(
                                                    nullCond,
                                                    new RandomValueRange(ForgeConfigHandler.server.lootConfig.spawnedAsBossScaledBaseLevel),
                                                    true,
                                                    ForgeConfigHandler.server.lootConfig.spawnedAsBossScaledBossScale)
                                    },
                                    nullCond,
                                    LycanitesTweaks.MODID + ":enchant_with_mob_levels_book")},
                    new LootCondition[]{new IsVariant(-1, false, false, true)},
                    new RandomValueRange(1), new RandomValueRange(0), LycanitesTweaks.MODID + "_scaled_boss_book");
            event.getTable().addPool(bookTable);
        }

        if(ForgeConfigHandler.server.lootConfig.registerRandomChargesLootTable) {
            CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(event.getName().getPath());
            if(creatureInfo == null) return;
            if(creatureInfo.getGroups().contains(CreatureManager.getInstance().creatureGroups.get("animal"))) return;

            LootPool chargeTable = new LootPool(
                    new LootEntry[0],
                    new LootCondition[]{
                            new HasMobLevels(ForgeConfigHandler.server.lootConfig.randomChargeMinimumMobLevel),
                            new RandomChanceWithVariantDropScale(ForgeConfigHandler.server.lootConfig.randomChargeChance, ForgeConfigHandler.server.lootConfig.randomChargeChanceLooting, true)},
                    new RandomValueRange(1), new RandomValueRange(0), LycanitesTweaks.MODID + "_random_charges");

            for (ElementInfo elementInfo : creatureInfo.elements) {
                if (!Helpers.getChargeElementsMap().containsKey(elementInfo.name)) continue;

                for (String charge : Helpers.getChargeElementsMap().get(elementInfo.name)) {
                    String entryName = LycanitesTweaks.MODID + ":_random_charge_" + charge;
                    if (chargeTable.getEntry(entryName) != null) continue; //intellij complains here but it's fine
                    chargeTable.addEntry(getRandomChargesEntry(charge, entryName));
                }
            }
            event.getTable().addPool(chargeTable);
        }
    }

    // Using only nisch rewrite lycanitestweaks:scale_with_mob_levels
    private static LootEntry getRandomChargesEntry(String charge, String entryName) {
        return new LootEntryItem(ObjectManager.getItem(charge), 1, 0,
                new LootFunction[]{
                        new SetCount(nullCond,new RandomValueRange(
                                ForgeConfigHandler.server.lootConfig.randomChargeScaledCountMinimum,
                                ForgeConfigHandler.server.lootConfig.randomChargeScaledCountMaximum)),
                        new AddCountFromMobLevels(nullCond,
                                ForgeConfigHandler.server.lootConfig.randomChargeLevelScale,
                                ForgeConfigHandler.server.lootConfig.randomChargeLevelScale,
                                ForgeConfigHandler.server.lootConfig.randomChargeDropLimit),
                        // Looting is not level scaled
                        new LootingEnchantBonus(nullCond, new RandomValueRange(0,
                                ForgeConfigHandler.server.lootConfig.randomChargeLootingBonus), 0),
                        new ApplyVariantItemDropsScale(nullCond, true, false)
                },
                nullCond,
                entryName);
    }
}
