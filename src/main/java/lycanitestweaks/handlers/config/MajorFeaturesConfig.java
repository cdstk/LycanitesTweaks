package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.handlers.config.major.BossAmalgalichConfig;
import lycanitestweaks.handlers.config.major.BossAsmodeusConfig;
import lycanitestweaks.handlers.config.major.BossRahovartConfig;
import lycanitestweaks.handlers.config.major.CreatureInteractConfig;
import lycanitestweaks.handlers.config.major.CreatureStatsConfig;
import lycanitestweaks.handlers.config.major.EntityStoreCreatureConfig;
import lycanitestweaks.handlers.config.major.ImperfectSummoningConfig;
import lycanitestweaks.handlers.config.major.ItemTweaksConfig;
import lycanitestweaks.handlers.config.major.PlayerMobLevelsConfig;
import lycanitestweaks.handlers.config.server.PlayerConfig;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class MajorFeaturesConfig {

    @Config.Comment("Interactions where a player is involved somehow")
    @Config.Name("Creature Interactions")
    public final CreatureInteractConfig creatureInteractConfig = new CreatureInteractConfig();

    @Config.Comment("Capability to replicate Lycanites Mobs PetEntry for non players.\n" +
            "Intended to be used by Ender Crystal Reskins.\n" +
            "Boss Summon Crystals activate when a player melees or gets close to them.\n" +
            "They primarily replace Dungeon and Altar mini bosses as a necessary player trigger for Player Mob Level mechanics.\n" +
            "Encounter Summon Crystals are similar except they despawn with distance in order to act as situational rewards.\n" +
            "During Mob Events they spawn as a pseudo Rare Variant for every creature.")
    @Config.Name("Crystal Stored Creature Entities")
    public final EntityStoreCreatureConfig escConfig = new EntityStoreCreatureConfig();

    @Config.Comment("Capability to store contextual information about a player and apply a contextual boost of levels to a Lycanites.\n" +
            "Works together with Lycanites' Beastiary and player gear in order to determine boosts for hostile/tamed contexts.\n" +
            "There are many calculation options and contexts to adjust, tamed contexts are affected more by Beastiary while hostile are affected by gear.\n" +
            "Overall intended to provide passive progression with a few opt-in challenges without overwhelming a player with tough mobs.")
    @Config.Name("Player Mob Levels Bonus")
    public final PlayerMobLevelsConfig pmlConfig = new PlayerMobLevelsConfig();

    @Config.Comment("A generally tougher but very configurable Amalgalich")
    @Config.Name("Enhanced Amalgalich")
    public final BossAmalgalichConfig amalgalichConfig = new BossAmalgalichConfig();

    @Config.Comment("A generally tougher but very configurable Asmodeus")
    @Config.Name("Enhanced Asmodeus")
    public final BossAsmodeusConfig asmodeusConfig = new BossAsmodeusConfig();

    @Config.Comment("A generally tougher but very configurable Rahovart")
    @Config.Name("Enhanced Rahovart")
    public final BossRahovartConfig rahovartConfig = new BossRahovartConfig();

    @Config.Comment("Reworks how Knowledge interacts with Summoning.\n" +
            "Summon Staff works with Knowledge Rank 1, however it will summon with reduced hp or damage (up to -95%), and sometimes fully hostile.\n" +
            "Increasing Knowledge reduces the chances and the stat reduction modifiers.\n" +
            "Knowledge Rank 2 will summon a normal minion without any nerfs.")
    @Config.Name("Imperfect Summoning")
    public final ImperfectSummoningConfig imperfectSummoningConfig = new ImperfectSummoningConfig();

    @Config.Comment("Various options to balance stats and debuff.\n" +
            "Additional modifiers for Bosses, such as summoning level matching minions and nbt SpawnedAsBoss tag providing Rare Variant Stats.\n" +
            "LycanitesTweaks caps speed/piercing and certain debuffs as high values don't provide a fair experience.\n" +
            "Boss modifiers address high health bonuses + Boss Damage Limit being not fun.")
    @Config.Name("Tweak Creature Stats")
    public final CreatureStatsConfig creatureStatsConfig = new CreatureStatsConfig();

    @Config.Comment("Tweaks to the vanilla Lycanites items, generally preparing them for integration with other mods, namely RLCombat and So Many Enchantments.\n" +
            "Cleansing Crystal and Immunizer can be given configurable cure sets.\n" +
            "Summoning Staffs having the creature charge leveling mechanic is provided here.")
    @Config.Name("Vanilla Lycanites Item Tweaks")
    public final ItemTweaksConfig itemTweaksConfig = new ItemTweaksConfig();

    @Config.Comment("Tweaks to the vanilla Lycanites player, generally very basic integration such as using Trinkets and Bauble's magic regen attribute.")
    @Config.Name("Vanilla Lycanites Player")
    public final PlayerConfig playerConfig = new PlayerConfig();
}
