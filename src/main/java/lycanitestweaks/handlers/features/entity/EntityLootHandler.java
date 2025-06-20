package lycanitestweaks.handlers.features.entity;

import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.loot.EnchantWithMobLevels;
import lycanitestweaks.loot.HasMobLevels;
import lycanitestweaks.loot.IsVariant;
import lycanitestweaks.loot.ScaleWithMobLevels;
import lycanitestweaks.util.Helpers;
import net.minecraft.init.Items;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityLootHandler {
    private static final LootCondition[] nullCond = new LootCondition[0];

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
            //TODO: nischcomment how will this double check below ever work?
            // resourceloc.toString will always be modid:whatever, so the first check will always fail
            if(LycanitesMobs.modid.equals(event.getName().toString())){
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
    }

    // LootTableLoadEvent examples
    @SubscribeEvent
    public static void addDefaultBossLoot(LootTableLoadEvent event){
        if (!LycanitesMobs.modid.equals(event.getName().getNamespace())) return;

        if(ForgeConfigHandler.server.lootConfig.registerSpawnedAsBossWithLevelsLootTables) {
            LootPool bookTable = new LootPool(
                    new LootEntry[]{
                            new LootEntryItem(Items.BOOK, 1, 0,
                                    new LootFunction[]{new EnchantWithMobLevels(nullCond, false, 1.0F)},
                                    nullCond,
                                    LycanitesTweaks.MODID + ":enchant_with_mob_levels_book")},
                    new LootCondition[]{new IsVariant(-1, false, false, true)},
                    new RandomValueRange(1), new RandomValueRange(0), LycanitesTweaks.MODID + "_boss_book");

            LootPool treasureBookTable = new LootPool(
                    new LootEntry[]{
                            new LootEntryItem(Items.BOOK, 1, 0,
                                    new LootFunction[]{new EnchantWithMobLevels(nullCond, true, 0.75F)},
                                    nullCond,
                                    LycanitesTweaks.MODID + ":enchant_with_mob_levels_book_treasure")},
                    new LootCondition[]{
                            new IsVariant(-1, false, false, true),
                            new HasMobLevels(30)},
                    new RandomValueRange(1), new RandomValueRange(0), LycanitesTweaks.MODID + "_boss_book_treasure");

            LootPool xpTable = new LootPool(
                    new LootEntry[]{
                            new LootEntryItem(Items.EXPERIENCE_BOTTLE, 1, 0,
                                    new LootFunction[]{
                                            new SetCount(nullCond, new RandomValueRange(0, 1)),
                                            new ScaleWithMobLevels(nullCond, 0.5F, 128)
                                    },
                                    nullCond,
                                    LycanitesTweaks.MODID + ":scale_with_mob_levels_xp")},
                    new LootCondition[]{new IsVariant(-1, false, false, true)},
                    new RandomValueRange(1), new RandomValueRange(0), LycanitesTweaks.MODID + "_boss_xp");

            event.getTable().addPool(bookTable);
            event.getTable().addPool(treasureBookTable);
            event.getTable().addPool(xpTable);
        }

        if(ForgeConfigHandler.server.lootConfig.registerRandomChargesLootTable) {
            CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(event.getName().getPath());
            if(creatureInfo == null) return;

            LootPool chargeTable = new LootPool(
                    new LootEntry[0],
                    new LootCondition[]{new HasMobLevels(ForgeConfigHandler.server.lootConfig.randomChargeMinimumMobLevel)},
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

    private static LootEntry getRandomChargesEntry(String charge, String entryName) {
        return new LootEntryItem(ObjectManager.getItem(charge), 1, 0,
                new LootFunction[]{
                        //TODO: rn its [min to max] x moblvl x scale + [0 to loot] x lootLvl
                        // idk if the looting part should be scaled as well
                        new SetCount(nullCond,new RandomValueRange(
                                ForgeConfigHandler.server.lootConfig.randomChargeScaledCountMinimum,
                                ForgeConfigHandler.server.lootConfig.randomChargeScaledCountMaximum)),
                        new ScaleWithMobLevels(nullCond,
                                ForgeConfigHandler.server.lootConfig.randomChargeLevelScale,
                                ForgeConfigHandler.server.lootConfig.randomChargeDropLimit),
                        new LootingEnchantBonus(nullCond, new RandomValueRange(0,
                                ForgeConfigHandler.server.lootConfig.randomChargeLootingBonus), 0)},
                nullCond,
                entryName);
    }
}
