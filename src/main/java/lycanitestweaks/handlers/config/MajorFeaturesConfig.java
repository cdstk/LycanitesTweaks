package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.handlers.config.major.*;
import net.minecraftforge.common.config.Config;

public class MajorFeaturesConfig {
    @Config.Name("Additional Creature Interactions")
    @MixinConfig.SubInstance
    public final CreatureInteractConfig creatureInteractConfig = new CreatureInteractConfig();

    @Config.Name("Capability: Crystal Stored Creature Bosses")
    @MixinConfig.SubInstance
    public final EntityStoreCreatureConfig escConfig = new EntityStoreCreatureConfig();

    @Config.Name("Capability: Player Mob Levels Bonus")
    @MixinConfig.SubInstance
    public final PlayerMobLevelsConfig pmlConfig = new PlayerMobLevelsConfig();

    @Config.Name("Enhanced Amalgalich")
    @MixinConfig.SubInstance
    public final BossAmalgalichConfig amalgalichConfig = new BossAmalgalichConfig();

    @Config.Name("Enhanced Asmodeus")
    @MixinConfig.SubInstance
    public final BossAsmodeusConfig asmodeusConfig = new BossAsmodeusConfig();

    @Config.Name("Enhanced Rahovart")
    @MixinConfig.SubInstance
    public final BossRahovartConfig rahovartConfig = new BossRahovartConfig();

    @Config.Name("Rework Summoning")
    @MixinConfig.SubInstance
    public final ImperfectSummoningConfig imperfectSummoningConfig = new ImperfectSummoningConfig();

    @Config.Name("Tweak Creature Stats")
    @MixinConfig.SubInstance
    public final CreatureStatsConfig creatureStatsConfig = new CreatureStatsConfig();

    @Config.Name("Tweak Lycanites Item")
    @MixinConfig.SubInstance
    public final ItemTweaksConfig itemTweaksConfig = new ItemTweaksConfig();
}
