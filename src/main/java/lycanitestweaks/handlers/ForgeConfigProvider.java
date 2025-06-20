package lycanitestweaks.handlers;

import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class ForgeConfigProvider {
    private static final Set<ResourceLocation> flowersaurBiomes = new HashSet<>();
    private static final List<PlayerMobLevelsConfig.BonusCategory> pmlBonusCategoryClientRenderOrder = new ArrayList<>();

    public static void init(){
        //initialise always available (instead of lazy created) sets here
    }

    public static void reset() {
        ForgeConfigProvider.flowersaurBiomes.clear();
        ForgeConfigProvider.pmlBonusCategoryClientRenderOrder.clear();
        init();
    }

    public static Set<ResourceLocation> getFlowersaurBiomes(){
        if(flowersaurBiomes.isEmpty() && ForgeConfigHandler.minorFeaturesConfig.flowersaurSpawningBiomes.length > 0)
            flowersaurBiomes.addAll(Arrays
                    .stream(ForgeConfigHandler.minorFeaturesConfig.flowersaurSpawningBiomes)
                    .map(ResourceLocation::new)
                    .collect(Collectors.toSet()));
        return flowersaurBiomes;
    }

    public static List<PlayerMobLevelsConfig.BonusCategory> getPmlBonusCategoryClientRenderOrder(){
        if(pmlBonusCategoryClientRenderOrder.isEmpty() && ForgeConfigHandler.clientFeaturesMixinConfig.pmlBeastiaryOrder.length > 0)
            pmlBonusCategoryClientRenderOrder.addAll(Arrays
                    .stream(ForgeConfigHandler.clientFeaturesMixinConfig.pmlBeastiaryOrder)
                    .map(PlayerMobLevelsConfig.BonusCategory::get)
                    .collect(Collectors.toList()));
        return pmlBonusCategoryClientRenderOrder;
    }
}
