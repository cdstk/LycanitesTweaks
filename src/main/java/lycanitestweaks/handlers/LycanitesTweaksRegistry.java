package lycanitestweaks.handlers;

import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.entity.item.EntityBossSummonCrystal;
import lycanitestweaks.entity.item.EntityEncounterSummonCrystal;
import lycanitestweaks.entity.projectile.EntityChargeArrow;
import lycanitestweaks.item.ItemChallengeSoulStaff;
import lycanitestweaks.item.ItemChargeStaff;
import lycanitestweaks.item.ItemEnchantedSoulkey;
import lycanitestweaks.item.ItemRapidChargeStaff;
import lycanitestweaks.loot.AddCountFromMobLevels;
import lycanitestweaks.loot.ApplyVariantItemDropsScale;
import lycanitestweaks.loot.EnchantWithMobLevels;
import lycanitestweaks.loot.HasMobLevels;
import lycanitestweaks.loot.IsVariant;
import lycanitestweaks.loot.ScaleWithMobLevels;
import lycanitestweaks.potion.PotionConsumed;
import lycanitestweaks.potion.PotionCripplingBase;
import lycanitestweaks.potion.PotionVoided;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = LycanitesTweaks.MODID)
public class LycanitesTweaksRegistry {

        // TODO time to move out soon
        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":challengesoulstaff")
        public static Item challengeSoulStaff = new ItemChallengeSoulStaff("challengesoulstaff", "");

        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":chargestaff")
        public static Item chargestaff = new ItemChargeStaff("chargestaff");
        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":devilgatlinggun")
        public static Item devilgatlinggun = new ItemRapidChargeStaff("devilgatlinggun");

        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":enchantedsoulkey")
        public static Item enchantedSoulkey = new ItemEnchantedSoulkey("enchantedsoulkey", 0);
        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":enchantedsoulkeydiamond")
        public static Item enchantedSoulkeyDiamond = new ItemEnchantedSoulkey("enchantedsoulkeydiamond", 1);
        @GameRegistry.ObjectHolder(LycanitesTweaks.MODID + ":enchantedsoulkeyemerald")
        public static Item enchantedSoulkeyEmerald = new ItemEnchantedSoulkey("enchantedsoulkeyemerald", 2);

        public static SoundEvent SOULGAZER_CRAFTINGTABLE;
        public static SoundEvent SOULGAZER_PLAYER;

        // wasted an hour wondering why it couldn't be like RLMixins
        public static void init() {
                LootConditionManager.registerCondition(new HasMobLevels.Serializer());
                LootConditionManager.registerCondition(new IsVariant.Serializer());
                LootFunctionManager.registerFunction(new AddCountFromMobLevels.Serializer());
                LootFunctionManager.registerFunction(new ApplyVariantItemDropsScale.Serializer());
                LootFunctionManager.registerFunction(new EnchantWithMobLevels.Serializer());
                LootFunctionManager.registerFunction(new ScaleWithMobLevels.Serializer());

                SOULGAZER_CRAFTINGTABLE = new SoundEvent(new ResourceLocation(LycanitesTweaks.MODID, "soulgazer_craftingtable")).setRegistryName("soulgazer_craftingtable");
                SOULGAZER_PLAYER = new SoundEvent(new ResourceLocation(LycanitesTweaks.MODID, "soulgazer_player")).setRegistryName("soulgazer_player");
        }

        @SubscribeEvent
        public static void registerItemEvent(RegistryEvent.Register<Item> event){
                if(ForgeConfigHandler.server.customStaffConfig.registerChallengeSoulStaffs) event.getRegistry().registerAll(challengeSoulStaff);
                if(ForgeConfigHandler.server.customStaffConfig.registerChargeStaffs) event.getRegistry().registerAll(chargestaff);
                if(ForgeConfigHandler.server.enchSoulkeyConfig.registerEnchantedSoulkeys) event.getRegistry().registerAll(enchantedSoulkey, enchantedSoulkeyDiamond, enchantedSoulkeyEmerald);
        }

        @SubscribeEvent
        public static void registerEntityEvent(RegistryEvent.Register<EntityEntry> event){
                int id = 1;

                event.getRegistry().register(
                        EntityEntryBuilder.create()
                        .entity(EntityBossSummonCrystal.class)
                        .id(new ResourceLocation(LycanitesTweaks.MODID, "bosscrystal"), id++)
                        .name("bosscrystal")
                        .tracker(64, 1, false)
                        .build()
                );
                event.getRegistry().register(
                        EntityEntryBuilder.create()
                        .entity(EntityEncounterSummonCrystal.class)
                        .id(new ResourceLocation(LycanitesTweaks.MODID, "encountercrystal"), id++)
                        .name("encountercrystal")
                        .tracker(64, 1, false)
                        .build()
                );
                if(ForgeConfigHandler.server.customStaffConfig.registerChargeStaffs){
                        event.getRegistry().register(
                                EntityEntryBuilder.create()
                                        .entity(EntityChargeArrow.class)
                                        .id(new ResourceLocation(LycanitesTweaks.MODID, "chargearrow"), id++)
                                        .name("chargearrow")
                                        .tracker(48, 1, false)
                                        .build()
                        );
                }
        }

        @SubscribeEvent
        public static void registerSoundEvent(RegistryEvent.Register<SoundEvent> event) {
                event.getRegistry().register(SOULGAZER_CRAFTINGTABLE);
                event.getRegistry().register(SOULGAZER_PLAYER);
        }

        @SubscribeEvent
        public static void registerPotionEvent(RegistryEvent.Register<Potion> event) {
                if(ForgeConfigHandler.server.effectsConfig.registerConsumed) {
                        event.getRegistry().register(PotionConsumed.INSTANCE);
                        PotionCripplingBase.addInstance(PotionConsumed.INSTANCE);
                }
                if(ForgeConfigHandler.server.effectsConfig.registerVoided) {
                        event.getRegistry().register(PotionVoided.INSTANCE);
                        PotionCripplingBase.addInstance(PotionVoided.INSTANCE);
                }
        }
}