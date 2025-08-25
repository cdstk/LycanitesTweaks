package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import net.minecraftforge.common.config.Config;

@MixinConfig(name = LycanitesTweaks.MODID)
public class PatchConfig {
    /*
     *
     * Temporary Mixin Patches, intended to become PR in LycanitesMobs
     *
     */

    @Config.Comment("Lycanites Mobs a few config map errors and therefore always uses a hidden default.\n" +
            "This will allow the missing configs to generate and be changed.\n" +
            "\tCreatureConfig -> \"Elemental Fusion Enabled\"\n" +
            "\tCreatureConfig -> \"Elemental Fusion Mix Bonus\"\n" +
            "The following are default misspellings that would need to be regenerated or manually corrected.\n" +
            "\tItemConfig -> \"prismarine_crystals\" (Max Sharpness repair item)")
    @Config.Name("0. Fix Lycanites Config Errors")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.configerrors.json")
    public boolean fixLycanitesConfigErrors = true;

    /*
     * Addressed
     * Block onEntityCollision
     * EntityIgnibus riderEffects
     * EntityIoray riderEffects onDismounted
     * EntityRoa riderEffects onDismounted
     * EntitySalamander riderEffects onDismounted
     * EntityThresher riderEffects onDismounted
     * GenericFoodItem onFoodEaten
     */
    @Config.Comment("Lycanites Mobs has multiple instances of applying potion effects on client.\n" +
            "This will fix known cases such as fire blocks, eating foods, and mounting interactions.")
    @Config.Name("Cancel Applying Potions on Client")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchescancelclientpotionadd.json")
    public boolean cancelClientPotionAdding = true;

    @Config.Comment("Fix an inconsistency in Rare/Boss entity audio where firing projectile, was 1/2 and 1/4 the range of other sound effects.\n" +
            "Affects audible distance instead of actual audio volume.\n" +
            "For example Rahovart's Hellfire Balls were muted at the edge of his arena while Asmodeus' Primary Attack could be heard at further distances.\n" +
            "Asmodeus' Gatling [projectile.devilgatling] may need audio balancing since it is harsher than the deeper attack sfx and was rarely audible.")
    @Config.Name("Fix Boss Entity Projectile Volume")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesbossprojectilevolume.json")
    public boolean bossProjectileVolume = true;

    @Config.Comment("Enable various unused sound files with obvious intent that can not be fixed with sounds.json\n" +
            "Ignibus melee/range attack\n" +
            "Rahovart's hell fire wave launch/activation\n" +
            "All rapid fire projectiles (Cinder, Serpix, Ignibus)")
    @Config.Name("Enable Unused Sounds")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.soundfixes.json")
    public boolean enableUnusedSounds = true;

    @Config.Comment("Prevent bosses from having an avoid target to skip pathfinding and save on performance.\n" +
            "Stationary bosses should not be pathfinding and their large sizes produces large lag spikes if they try to do so.")
    @Config.Name("Optimize Stationary Bosses Trying to Pathfind")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.bossavoidtarget.json")
    public boolean stationaryBossNullAvoidTarget = true;

    @Config.Comment("Prevent explosions from affecting invincible large projectiles to save on performance.\n" +
            "For example two or more Wraiths exploding in Rahovart's flamewalls produced large lag spikes.")
    @Config.Name("Optimize Explosions On Invincible Projectiles")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.bossprojectileexplosionimmunity.json")
    public boolean stationaryProjectileExplosions = true;

    @Config.Comment("Removes the floor/ceiling rounding with Rejuvenation and Decay. Rejuv will not have a minimum healing boost and Decay will not nullify low healing.")
    @Config.Name("Remove Healing Rejuv/Decay Rounding")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.featurerejuvdecayroundingbegone.json")
    public boolean removeHealEffectsRounding = true;

    @Config.Comment("Fixes a bug where minions despawn and don't get properly cleared from boss mechanics")
    @Config.Name("Fix Asmodeus' Hellshield Minions")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.asmodeusminions.json")
    public boolean fixAsmodeusMinions = true;

    @Config.Comment("Fix Lycanites Entities spawning their minions in walls. If collision is detected, spawn on top of host instead.")
    @Config.Name("Fix Minions Spawning in Walls")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patcheswallminions.json")
    public boolean fixWallMinion = true;

    // Preferring this approach as injecting pickup behavior into the vanilla bucket is silly
    @Config.Comment("Register Forge's fluid buckets for Lycanites fluids. Fixes dispensers not being able to pick up Lycanites fluids.\n" +
            "Keeps the original buckets loaded, the forge bucket item will appear where Lycanites custom bucket has no handling.")
    @Config.Name("Add Forge Universal Fluid Buckets")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesforgebucket.json")
    public boolean forgeFluidBuckets = true;

    @Config.Comment("The vanilla Lycanites option for \"Variant Rare Despawning\" has an inconsistency where peaceful mode allowed (Chupacabras) entities never despawned.\n" +
            "This will fix it and allow them to despawn to be consistent with other Rare variants.")
    @Config.Name("Rare Variant Despawning Consistency")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.raredespawnconsistency.json")
    public boolean rareVariantDespawnConsistency = true;

    @Config.Comment("Fix Soulcubes not being instantly removed after the Boss Event starts.\n" +
            "The intent was that the arena builder would replace it, however the delay allows the Soulcube to be collected in various cases.")
    @Config.Name("Main Boss Altar Instantly Removes Soulcube")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.altarmainbosssoulcube.json")
    public boolean altarMainBossConsumeSoulcube = true;

    @Config.Comment("Fix Altar spawned bosses being able to despawn when Variant despawning is enabled")
    @Config.Name("Mini Boss Altar Persistence")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.altarminibosspersistence.json")
    public boolean altarMiniBossPersistence = true;

    @Config.Comment("Altars post LivingDestroyBlockEvent for every call to setBlockToAir. This fixes custom structure cheeses through griefing.")
    @Config.Name("Mini Boss Altar Posts Forge Event")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.altarforgeevent.json")
    public boolean altarPostsForgeEvent = true;

    @Config.Comment("PlaceBlockGoal post LivingDestroyBlockEvent for every call to canPlaceBlock. This fixes griefing protected blocks.")
    @Config.Name("Vespids Posts Forge Event")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesplaceblockgoalforgeevent.json")
    public boolean canPlaceBlockGoalForgeEvent = true;

    @Config.Comment("Disables Soul Bounds using portals, which would kill them and set respawn cooldown")
    @Config.Name("Disable Soul Bounds Using Portals")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchessoulboundnoportal.json")
    public boolean soulBoundNoPortal = true;

    @Config.Comment("Fix hostile AgeableCreature babies not dropping loot")
    @Config.Name("Fix Baby Mobs Dropping No Loot")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesageablebabydrops.json")
    public boolean fixAgeableBabyDrops = true;

    // Rahovart is an example with oversight and can spawn infinite minions regardless of this
    @Config.Comment("Add persistence to minions spawned through the summonMinion method\n" +
            "This fixes vanilla Boss issues caused by despawning minions when mechanics depend on minions\n" +
            "If master dies, minion will be force despawned after 1 minute\n" +
            "Default is false as LycanitesTweaks boss enhancements directly addresses")
    @Config.Name("Fix Boss Mechanics Minion Persistence")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesbasecreatureminionpersistence.json")
    public boolean fixBaseCreatureSummonPersistence = false;

    @Config.Comment("Add a call to super in BaseCreature's isPotionApplicable method, restores vanilla parity for Undead mobs")
    @Config.Name("Fix BaseCreature Potion Applicable")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesbasecreaturepotionapplicable.json")
    public boolean fixBaseCreaturePotionApplicableSuper = true;

    @Config.Comment("Fix client side showing incorrect stats in the beastiary, interact gui, and other mods")
    @Config.Name("Fix Client Pet Stat Desync")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchespetentrydesync.json")
    public boolean fixClientPetEntryVisual = true;

    @Config.Comment("Fix divide by zero crash in FireProjectilesGoal and high RangedSpeed preventing attacks")
    @Config.Name("Fix Creature Ranged Speed")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesrangedspeeddividebyzero.json")
    public boolean fixRangedSpeedDivideZero = true;

    @Config.Comment("Fix Ettin checking for inverted griefing flag")
    @Config.Name("Fix Ettin grief flag")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesettingriefflag.json")
    public boolean fixEttinBlockBreak = true;

    @Config.Comment("Fix Fear checking for creative capabilities instead of flight")
    @Config.Name("Fix Fear Survival Flying")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchesfearsurvivalflying.json")
    public boolean fixFearSurvivalFlying = true;

    @Config.Comment("Fix HealWhenNoPlayersGoal trigger check using AND instead of OR therefore bricking in most cases")
    @Config.Name("Fix Heal Goal Check")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patcheshealgoalcheck.json")
    public boolean fixHealGoalCheck = true;

    @Config.Comment("Lycanites allows both hands to interact with a pet with one action.\n" +
            "Therefore the offhand can disrupt the mainhand with an inventory GUI or mounting.\n" +
            "This is primarily aimed at fixing the case where you are healing pet with food and would be forced to always mount it.")
    @Config.Name("Fix Both Hands Performing Item Interactions")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patches.mainhandexclusiveinteract.json")
    public boolean fixMountingWithHealFood = true;


    // Mixin is not needed, original handling does not need to be canceled
    // Handle apply curing in PR, not needed here as original handling will catch active blindness
    @Config.Comment("Have NV deny Blindness from applying. This fixes visual flashing when blindness is applied every tick")
    @Config.Name("Fix Night Vision Curing Blindness")
    public boolean fixNVCuringBlindness = true;

    @Config.Comment("Fix Pickup host entity losing track of target such as when holding inside a wall")
    @Config.Name("Fix Pickup Target")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchespickuptarget.json")
    public boolean fixPickupTargetAlways = true;

    // Better fix https://gitlab.com/Lycanite/LycanitesMobs/-/merge_requests/461
    @Config.Comment("Fix properties, such as tamed state, being set after stat calculation. This fix adds a late additional recalculation.")
    @Config.Name("Fix Properties Set After Stat Calculation")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patcheslatestatcalc.json")
    public boolean fixLateSettingProperties = true;

    @Config.Comment("Fix Serpix Blizzard projectile spawning in the ground")
    @Config.Name("Fix Serpix Blizzard Offset")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patcheserpixblizzardoffset.json")
    public boolean fixSerpixBlizzardOffset = true;

    @Config.Comment("All Lyc containers (Equipment Forges, Infuser, Station, Summoning Pedestal) have no Item Quick Move implementation (via shift clicking). This fix makes them use newer mc quick move mechanics where the crafting slots are preferred over the player inventory+hotbar.\n" +
            "Also fixes a minimal bug in Lyca Pet chest inventory where one slot (bottom right in player inventory) was not reachable via quick move.")
    @Config.Name("Fix Container Quick Move")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchescontainerbettershifting.json")
    public boolean fixContainerQuickMove = true;

    @Config.Comment("Players can only interact with Lyca crafting blocks from very low distances. This fix instead makes them copy the vanilla block (crafting table, furnace etc) behavior")
    @Config.Name("Fix Tile Entity Interaction Distance")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(defaultValue = true, lateMixin = "mixins.lycanitestweaks.patchestileentityinteractiondistance.json")
    public boolean fixTileEntityInteractionDistance = true;
}
