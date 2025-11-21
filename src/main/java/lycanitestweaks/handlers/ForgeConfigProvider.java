package lycanitestweaks.handlers;

import fermiumbooter.FermiumRegistryAPI;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.config.EarlyConfigReader;
import lycanitestweaks.handlers.config.IntegrationConfig;
import lycanitestweaks.handlers.config.MinorFeaturesConfig;
import lycanitestweaks.handlers.config.major.CreatureInteractConfig;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.handlers.config.server.AltarsConfig;
import lycanitestweaks.handlers.config.server.CustomStaffConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ForgeConfigProvider {

    // Any larger and fully disableable configs are not provided here
    // Instead provided by the respective config class itself

    // Core
    private static final Map<String, Set<String>> assetPaths = new HashMap<>();

    // Client
    private static final Set<String> altarBeastiaryBlacklist = new HashSet<>();
    private static final Set<String> creatureBeastiaryBlacklist = new HashSet<>();
    private static final Map<String, Set<Integer>> creatureSubspeciesBeastiaryBlacklist = new HashMap<>();
    private static final Set<String> elementBeastiaryBlacklist = new HashSet<>();
    private static final Set<String> equipmentPartRenderSkip = new HashSet<>();

    // Server
    private static final Set<Enchantment> chargeStaffEnchantsBlacklist = new HashSet<>();

    // Minor
    private static final Set<ResourceLocation> flowersaurBiomes = new HashSet<>();

    // Major
    private static final Set<Enchantment> craftedEquipmentEnchantsBlacklist = new HashSet<>();
    private static final Set<String> transformBossSpawnerNames = new HashSet<>();
    private static final Set<String> elementsApplyBuffBlacklist = new HashSet<>();
    private static final Map<String, Integer> effectsApplyScaleLevelLimited = new HashMap<>();
    private static final Map<String, Integer> elementsApplyScaleLevelLimitedBuffs = new HashMap<>();
    private static final Map<String, Integer> elementsApplyScaleLevelLimitedDebuffs = new HashMap<>();
    private static final Set<ResourceLocation> cleansedCureEffects = new HashSet<>();
    private static final Set<ResourceLocation> immunizationCureEffects = new HashSet<>();
    private static final Set<ResourceLocation> bossBlacklistedEffects = new HashSet<>();
    private static final Set<ResourceLocation> minionBlacklistedEffects = new HashSet<>();

    public static void pluginInit(){
        // initialise earlier for mixins that run on startup
        ForgeConfigProvider.assetPaths.put("creaturetypes", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("creaturegroups", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("creatures", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("elements", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("items", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("projectiles", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("equipment", new HashSet<>());

        ForgeConfigProvider.assetPaths.put("dungeons/themes", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("dungeons/structures", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("dungeons/sectors", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("dungeons/schematics", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("mobevents_events", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("mobevents_spawners", new HashSet<>());
        ForgeConfigProvider.assetPaths.put("spawners", new HashSet<>());

        ForgeConfigProvider.assetPaths.get("elements").add("jsons/bosselements");
        ForgeConfigProvider.assetPaths.get("projectiles").add("jsons/bossprojectiles");

        if(EarlyConfigReader.getBoolean(
                MinorFeaturesConfig.REPLACE_WATER_MONSTER,
                ForgeConfigHandler.minorFeaturesConfig.waterMonsterSpawningConfigs
        )) ForgeConfigProvider.assetPaths.get("creatures").add("jsons/waterspawning");

        if(EarlyConfigReader.getBoolean(
                CreatureInteractConfig.REPLACE_TRANSFORM_INTO_BOSS,
                ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.canTransformIntoBossFlagExamples
        )) ForgeConfigProvider.assetPaths.get("spawners").add("jsons/examples/bosstransformtag");

        if(EarlyConfigReader.getBoolean(
                CreatureStatsConfig.REPLACE_DUNGEON_BOSS,
                ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossRareBoost
        )) ForgeConfigProvider.assetPaths.get("dungeons/schematics").add("jsons/rebalancedungeons/schematics");
        if(EarlyConfigReader.getBoolean(
                AltarsConfig.ADD_WITHERING_HEIGHTS_ALTAR,
                ForgeConfigHandler.server.altarsConfig.witheringHeightsAltar
        )) ForgeConfigProvider.assetPaths.get("mobevents_events").add("jsons/witheraltar");

        if(FermiumRegistryAPI.isModPresent(ModLoadedUtil.BLOODMOON_MODID)) {
            if(EarlyConfigReader.getBoolean(
                    IntegrationConfig.REPLACE_BLOODMOON_SPAWNERS_NORMAL,
                    ForgeConfigHandler.integrationConfig.bloodmoonSpawnerNormal
            )) ForgeConfigProvider.assetPaths.get("spawners").add("jsons/bloodmoon/normal");
            if(EarlyConfigReader.getBoolean(
                    IntegrationConfig.ADD_BLOODMOON_SPAWNERS_RARE,
                    ForgeConfigHandler.integrationConfig.bloodmoonSpawnerRare
            )) ForgeConfigProvider.assetPaths.get("spawners").add("jsons/bloodmoon/rare");
        }

        if(FermiumRegistryAPI.isModPresent(ModLoadedUtil.ICEANDFIRE_MODID)){
            if(EarlyConfigReader.getBoolean(
                    IntegrationConfig.ADD_INF_SPAWNER,
                    ForgeConfigHandler.integrationConfig.infLightingDragonSpawner
            )) ForgeConfigProvider.assetPaths.get("spawners").add("jsons/iceandfire");
        }

        if(EarlyConfigReader.getBoolean(
                CustomStaffConfig.REPLACE_EVENTFUL_STAFF_DROPS,
                ForgeConfigHandler.server.customStaffConfig.registerEventfulStaffs
        )) ForgeConfigProvider.assetPaths.get("mobevents_spawners").add("jsons/eventfulstaffs");

        if(EarlyConfigReader.getBoolean(
                MinorFeaturesConfig.ADD_DIAMOND_PAXEL,
                ForgeConfigHandler.minorFeaturesConfig.addDiamondPaxel
        )) ForgeConfigProvider.assetPaths.get("equipment").add("jsons/basicequipment");

        // Need special handling, maybe mixins to handle these guys
//        if(false) {
//            ForgeConfigProvider.assetPaths.get("creatures").add("jsons/sonoftitans");
//            ForgeConfigProvider.assetPaths.get("creatures").add("jsons/srp/creatures");
//            ForgeConfigProvider.assetPaths.get("elements").add("jsons/srp/elements");
//        }
    }

    public static void init(){
        //initialise always available (instead of lazy created) sets here
    }

    public static void reset() {
        ForgeConfigProvider.chargeStaffEnchantsBlacklist.clear();
        ForgeConfigProvider.flowersaurBiomes.clear();
        ForgeConfigProvider.altarBeastiaryBlacklist.clear();
        ForgeConfigProvider.creatureBeastiaryBlacklist.clear();
        ForgeConfigProvider.creatureSubspeciesBeastiaryBlacklist.clear();
        ForgeConfigProvider.elementBeastiaryBlacklist.clear();
        ForgeConfigProvider.equipmentPartRenderSkip.clear();
        ForgeConfigProvider.craftedEquipmentEnchantsBlacklist.clear();
        ForgeConfigProvider.transformBossSpawnerNames.clear();
        ForgeConfigProvider.elementsApplyBuffBlacklist.clear();
        ForgeConfigProvider.effectsApplyScaleLevelLimited.clear();
        ForgeConfigProvider.elementsApplyScaleLevelLimitedBuffs.clear();
        ForgeConfigProvider.elementsApplyScaleLevelLimitedDebuffs.clear();
        ForgeConfigProvider.cleansedCureEffects.clear();
        ForgeConfigProvider.immunizationCureEffects.clear();
        ForgeConfigProvider.bossBlacklistedEffects.clear();
        ForgeConfigProvider.minionBlacklistedEffects.clear();
        init();
    }

    public static Set<String> getAssetPathSetFor(String lycanitesAssetPath){
        if(ForgeConfigProvider.assetPaths.containsKey(lycanitesAssetPath)) return ForgeConfigProvider.assetPaths.get(lycanitesAssetPath);
        return null;
    }

    public static Set<String> getAltarBeastiaryBlacklist(){
        if(ForgeConfigProvider.altarBeastiaryBlacklist.isEmpty()
                && ForgeConfigHandler.clientFeaturesMixinConfig.altarInfoBeastiaryBlacklist.length > 0)
            ForgeConfigProvider.altarBeastiaryBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.altarInfoBeastiaryBlacklist)
                    .collect(Collectors.toList()));
        return ForgeConfigProvider.altarBeastiaryBlacklist;
    }

    public static Set<String> getCreatureBeastiaryBlacklist(){
        if(ForgeConfigProvider.creatureBeastiaryBlacklist.isEmpty()
                && ForgeConfigHandler.clientFeaturesMixinConfig.creatureInfoBeastiaryBlacklist.length > 0)
            ForgeConfigProvider.creatureBeastiaryBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.creatureInfoBeastiaryBlacklist)
                    .collect(Collectors.toList()));
        return ForgeConfigProvider.creatureBeastiaryBlacklist;
    }

    public static Map<String, Set<Integer>> getCreatureSubspeciesBeastiaryBlacklist(){
        if(ForgeConfigProvider.creatureSubspeciesBeastiaryBlacklist.isEmpty()
                && ForgeConfigHandler.clientFeaturesMixinConfig.creatureSubspeciesInfoBeastiaryBlacklist.length > 0)
            ForgeConfigProvider.creatureSubspeciesBeastiaryBlacklist.putAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.creatureSubspeciesInfoBeastiaryBlacklist)
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(),
                            split -> {
                                try {
                                    return Arrays.stream(split[1].split(",")).map(String::trim).map(Integer::valueOf).collect(Collectors.toSet());
                                } catch (Exception e){
                                    return new HashSet<>();
                                }
                            }
                    )));
        return ForgeConfigProvider.creatureSubspeciesBeastiaryBlacklist;
    }

    public static Set<String> getElementBeastiaryBlacklist(){
        if(ForgeConfigProvider.elementBeastiaryBlacklist.isEmpty()
                && ForgeConfigHandler.clientFeaturesMixinConfig.elementInfoBeastiaryBlacklist.length > 0)
            ForgeConfigProvider.elementBeastiaryBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.elementInfoBeastiaryBlacklist)
                    .collect(Collectors.toList()));
        return ForgeConfigProvider.elementBeastiaryBlacklist;
    }

    public static Set<String> getEquipmentPartRendersToSkip(){
        if(ForgeConfigProvider.equipmentPartRenderSkip.isEmpty()
                && ForgeConfigHandler.clientFeaturesMixinConfig.equipmentRenderPartNamesToSkip.length > 0)
            ForgeConfigProvider.equipmentPartRenderSkip.addAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.equipmentRenderPartNamesToSkip)
                    .collect(Collectors.toList()));
        return ForgeConfigProvider.equipmentPartRenderSkip;
    }

    public static Set<ResourceLocation> getFlowersaurBiomes(){
        if(ForgeConfigProvider.flowersaurBiomes.isEmpty()
                && ForgeConfigHandler.minorFeaturesConfig.flowersaurSpawningBiomes.length > 0)
            ForgeConfigProvider.flowersaurBiomes.addAll(Arrays
                    .stream(ForgeConfigHandler.minorFeaturesConfig.flowersaurSpawningBiomes)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.flowersaurBiomes;
    }

    public static Set<Enchantment> getChargeStaffEnchantmentBlacklist() {
        if(ForgeConfigProvider.chargeStaffEnchantsBlacklist.isEmpty()
                && ForgeConfigHandler.server.customStaffConfig.blacklistedChargeStaffEnchants.length > 0)
            ForgeConfigProvider.chargeStaffEnchantsBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.server.customStaffConfig.blacklistedChargeStaffEnchants)
                    .map(ResourceLocation::new)
                    .map(ForgeRegistries.ENCHANTMENTS::getValue)
                    .collect(Collectors.toSet())
            );
        return ForgeConfigProvider.chargeStaffEnchantsBlacklist;
    }

    public static Set<Enchantment> getEquipmentEnchantmentBlacklist() {
        if(ForgeConfigProvider.craftedEquipmentEnchantsBlacklist.isEmpty()
                && ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.blacklistedEquipmentEnchants.length > 0)
            ForgeConfigProvider.craftedEquipmentEnchantsBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.blacklistedEquipmentEnchants)
                    .map(ResourceLocation::new)
                    .map(ForgeRegistries.ENCHANTMENTS::getValue)
                    .collect(Collectors.toSet())
            );
        return ForgeConfigProvider.craftedEquipmentEnchantsBlacklist;
    }

    public static Set<String> getCanTransformIntoBossSpawnerNames(){
        if(ForgeConfigProvider.transformBossSpawnerNames.isEmpty()
                && ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.transformBossSpawnerNameStrings.length > 0)
            ForgeConfigProvider.transformBossSpawnerNames.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureInteractConfig.transformBossSpawnerNameStrings)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.transformBossSpawnerNames;
    }

    public static Map<String, Integer> getLevelLimitedEffects() {
        if (ForgeConfigProvider.effectsApplyScaleLevelLimited.isEmpty())
            ForgeConfigProvider.effectsApplyScaleLevelLimited.putAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.effectsLevelLimited)
                    .map(s -> s.split(","))
                    .filter(split -> split[2].equals("true"))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(), //Key
                            split -> {                //Value
                                try {
                                    return Integer.valueOf(split[1].trim());
                                } catch (Exception e) {
                                    LycanitesTweaks.LOGGER.error("Failed to parse {} in effectsLevelLimited", split[1].trim());
                                }
                                return 0;
                            }
                    )));
        return ForgeConfigProvider.effectsApplyScaleLevelLimited;
    }

    public static Map<String, Integer> getLevelLimitedElementBuffs(){
        if(ForgeConfigProvider.elementsApplyScaleLevelLimitedBuffs.isEmpty())
            ForgeConfigProvider.elementsApplyScaleLevelLimitedBuffs.putAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.elementsLevelLimitedBuffs)
                    .map(s -> s.split(","))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(), //Key
                            split -> {                //Value
                                try {
                                    return Integer.valueOf(split[1].trim());
                                } catch (Exception e) {
                                    LycanitesTweaks.LOGGER.error("Failed to parse {} in elementsLevelLimitedBuffs", split[1].trim());
                                }
                                return 0;
                            }
                    )));
        return ForgeConfigProvider.elementsApplyScaleLevelLimitedBuffs;
    }

    public static Map<String, Integer> getLevelLimitedElementDebuffs(){
        if(ForgeConfigProvider.elementsApplyScaleLevelLimitedDebuffs.isEmpty())
            ForgeConfigProvider.elementsApplyScaleLevelLimitedDebuffs.putAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.elementsLevelLimitedDebuffs)
                    .map(s -> s.split(","))
                    .collect(Collectors.toMap(
                            split -> split[0].trim(), //Key
                            split -> {                //Value
                                try {
                                    return Integer.valueOf(split[1].trim());
                                } catch (Exception e) {
                                    LycanitesTweaks.LOGGER.error("Failed to parse {} in elementsLevelLimitedDebuffs", split[1].trim());
                                }
                                return 0;
                            }
                    )));
        return ForgeConfigProvider.elementsApplyScaleLevelLimitedDebuffs;
    }

    public static Set<ResourceLocation> getCleansedCureEffects(){
        if(ForgeConfigProvider.cleansedCureEffects.isEmpty())
            ForgeConfigProvider.cleansedCureEffects.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.cleansedEffectsToCure)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.cleansedCureEffects;
    }

    public static Set<ResourceLocation> getImmunizationCureEffects(){
        if(ForgeConfigProvider.immunizationCureEffects.isEmpty())
            ForgeConfigProvider.immunizationCureEffects.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.immunizationEffectsToCure)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.immunizationCureEffects;
    }

    public static Set<ResourceLocation> getBossBlacklistedEffects(){
        if(ForgeConfigProvider.bossBlacklistedEffects.isEmpty())
            ForgeConfigProvider.bossBlacklistedEffects.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.bossEffectsBlacklist)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.bossBlacklistedEffects;
    }

    public static Set<ResourceLocation> getMinionBlacklistedEffects(){
        if(ForgeConfigProvider.minionBlacklistedEffects.isEmpty())
            ForgeConfigProvider.minionBlacklistedEffects.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.minionEffectsBlacklist)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.minionBlacklistedEffects;
    }

    public static Set<String> getElementsApplyBuffBlacklist(){
        if(ForgeConfigProvider.elementsApplyBuffBlacklist.isEmpty())
            ForgeConfigProvider.elementsApplyBuffBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.elementsBuffsBlacklist)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.elementsApplyBuffBlacklist;
    }
}
