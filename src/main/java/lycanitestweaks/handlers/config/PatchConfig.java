package lycanitestweaks.handlers.config;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class PatchConfig {
    /*
     *
     * Temporary Mixin Patches, intended to become PR in LycanitesMobs
     *
     */

    // Preferring this approach as injecting pickup behavior into the vanilla bucket is silly
    @Config.Comment("Register Forge's Fluid Buckets for Lycanites Fluids. Fixes dispensers not being able to pick up Lycanites Fluids.\n" +
            "Keeps the original buckets loaded, the forge bucket item will appear where Lycanites Custom Bucket has no handling.")
    @Config.Name("Add Forge Universal Fluid Buckets")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesforgebucket.json")
    public boolean forgeFluidBuckets = true;

    @Config.Comment("Altars post LivingDestroyBlockEvent for every call to setBlockToAir")
    @Config.Name("Altar Posts Forge Event")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesaltarforgeevent.json")
    public boolean altarPostsForgeEvent = true;

    @Config.Comment("PlaceBlockGoal post LivingDestroyBlockEvent for every call to canPlaceBlock")
    @Config.Name("Can Place Block Goal Forge Event")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesplaceblockgoalforgeevent.json")
    public boolean canPlaceBlockGoalForgeEvent = true;

    @Config.Comment("Disables Soul Bounds using portals, which would kill them and set respawn cooldown")
    @Config.Name("Disable Soul Bounds Using Portals")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchessoulboundnoportal.json")
    public boolean soulBoundNoPortal = true;

    @Config.Comment("Fix hostile AgeableCreature babies not dropping loot")
    @Config.Name("Fix AgeableCreature baby drops")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesageablebabydrops.json")
    public boolean fixAgeableBabyDrops = true;

    // Rahovart is an example with oversight and can spawn infinite minions regardless of this
    @Config.Comment("Add persistence to minions spawned through the summonMinion method\n" +
            "This fixes vanilla Boss issues caused by despawning minions when mechanics depend on minions\n" +
            "If master dies, minion will be force despawned after 1 minute\n" +
            "Default is false as LycanitesTweaks boss enhancements directly addresses")
    @Config.Name("Fix Boss Mechanics Minion Persistence")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesbasecreatureminionpersistence.json")
    public boolean fixBaseCreatureSummonPersistence = false;

    @Config.Comment("Add a call to super in BaseCreature's isPotionApplicable method, restores vanilla parity for Undead mobs")
    @Config.Name("Fix BaseCreature Potion Applicable")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesbasecreaturepotionapplicable.json")
    public boolean fixBaseCreaturePotionApplicableSuper = true;

    @Config.Comment("Fix client side showing incorrect stats in the beastiary, interact gui, and other mods")
    @Config.Name("Fix Client Pet Stat Desync")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchespetentrydesync.json")
    public boolean fixClientPetEntryVisual = true;

    @Config.Comment("Fix divide by zero crash in FireProjectilesGoal and RangedSpeed of zero preventing attacks")
    @Config.Name("Fix Creature Ranged Speed")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesrangedspeeddividebyzero.json")
    public boolean fixRangedSpeedDivideZero = true;

    @Config.Comment("Fix Ettin checking for inverted griefing flag")
    @Config.Name("Fix Ettin grief flag")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesettingriefflag.json")
    public boolean fixEttinBlockBreak = true;

    @Config.Comment("Fix Fear checking for creative capabilities instead of flight")
    @Config.Name("Fix Fear Survival Flying")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesfearsurvivalflying.json")
    public boolean fixFearSurvivalFlying = true;

    @Config.Comment("Fix HealWhenNoPlayersGoal trigger check using AND instead of OR therefore bricking in most cases")
    @Config.Name("Fix Heal Goal Check")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patcheshealgoalcheck.json")
    public boolean fixHealGoalCheck = true;

    @Config.Comment("Fix Mounting when trying to Heal a tamed creature with food, will no longer mount when trying to heal the creature")
    @Config.Name("Fix Mounting With Heal Food")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchesnomountwithfood.json")
    public boolean fixMountingWithHealFood = true;


    // Mixin is not needed, original handling does not need to be canceled
    // Handle apply curing in PR, not needed here as original handling will catch active blindness
    @Config.Comment("Have NV deny Blindness from applying. This fixes visual flashing when blindness is applied every tick")
    @Config.Name("Fix Night Vision Curing Blindness")
    public boolean fixNVCuringBlindness = true;

    @Config.Comment("Fix Pickup host entity losing track of target such as when holding inside a wall")
    @Config.Name("Fix Pickup Target")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patchespickuptarget.json")
    public boolean fixPickupTargetAlways = true;

    // Better fix https://gitlab.com/Lycanite/LycanitesMobs/-/merge_requests/461
    @Config.Comment("Fix properties, such as tamed state, being set after stat calculation. This fix adds a late additional recalculation.")
    @Config.Name("Fix Properties Set After Stat Calculation")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patcheslatestatcalc.json")
    public boolean fixLateSettingProperties = true;

    @Config.Comment("Fix Serpix Blizzard projectile spawning in the ground")
    @Config.Name("Fix Serpix Blizzard Offset")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.patcheserpixblizzardoffset.json")
    public boolean fixSerpixBlizzardOffset = true;

    @Config.Comment("All Lyc containers (Equipment Forges, Infuser, Station, Summoning Pedestal) have no Item Quick Move implementation (via shift clicking). This fix makes them use newer mc quick move mechanics where the crafting slots are preferred over the player inventory+hotbar.\n" +
            "Also fixes a minimal bug in Lyca Pet chest inventory where one slot (bottom right in player inventory) was not reachable via quick move.")
    @Config.Name("Fix Container Quick Move")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.containerbettershifting.json")
    public boolean fixContainerQuickMove = true;

    @Config.Comment("Players can only interact with Lyca crafting blocks from very low distances. This fix instead makes them copy the vanilla block (crafting table, furnace etc) behavior")
    @Config.Name("Fix Tile Entity Interaction Distance")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.lycanitestweaks.tileentityinteractiondistance.json")
    public boolean fixTileEntityInteractionDistance = true;
}
