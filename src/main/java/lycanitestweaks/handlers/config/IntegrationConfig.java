package lycanitestweaks.handlers.config;

import fermiumbooter.FermiumRegistryAPI;
import fermiumbooter.annotations.MixinConfig;
import lycanitestweaks.LycanitesTweaks;
import lycanitestweaks.compat.ModLoadedUtil;
import net.minecraftforge.common.config.Config;

/** Approach is to have first time boot not enqueue mixins. Follow-up boot will use config based on mods installed. **/
@MixinConfig(name = LycanitesTweaks.MODID)
public class IntegrationConfig {

    @Config.Comment("Allows Soulgazers to be worn as a bauble. Includes keybinds to enable auto/right clicks.")
    @Config.Name("Soulgazer Bauble (BaublesAPI)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.BAUBLES_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.baublessoulgazer.json", defaultValue = false)
    public boolean soulgazerBauble = FermiumRegistryAPI.isModPresent(ModLoadedUtil.BAUBLES_MODID);

    @Config.Comment("If true, Soulgazers will only be equippable into the charm slot. Else any slot can be used.")
    @Config.Name("Soulgazer Bauble Charm")
    public boolean soulgazerBaubleCharm = FermiumRegistryAPI.isModPresent(ModLoadedUtil.BAUBLES_MODID);

    @Config.Comment("Sets Ender Pearls as the repair material")
    @Config.Name("Soulgazer Bauble Ender Pearl Reforge")
    public boolean soulgazerBaubleRepairMaterial = FermiumRegistryAPI.isModPresent(ModLoadedUtil.BAUBLES_MODID);

    @Config.Comment("Adds Distinct Damage Descriptions information to Beastiary")
    @Config.Name("DDD Beastiary Info (Distinct Damage Descriptions)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.DDD_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.featureclientbeastiaryddd.json", defaultValue = false)
    public boolean beastiaryGUIDDD = FermiumRegistryAPI.isModPresent(ModLoadedUtil.DDD_MODID);

    @Config.Comment("Load a 1% chance 6400 tick cycling rain+storm spawner for 'iceandfire:lightningdragon'.\n" +
            "This will try to spawn a copper armored stage 5 dragon for any Ice and Fire version that uses this mobId.\n" +
            "Provides example of usage of \"lycanitestweaks:setNBT\" and \"lycanitestweaks:doInitialSpawn\".")
    @Config.Name("I&F Copper and Lightning JSON Spawner (Ice and Fire)")
    @Config.RequiresMcRestart
    public boolean infLightingDragonSpawner = FermiumRegistryAPI.isModPresent(ModLoadedUtil.ICEANDFIRE_MODID);

    @Config.Comment("This allows using some additional actions and rules in InControl specifically for Lycanites Mobs.\n" +
            "New Actions: \"lycanites_addlevel\", \"lycanites_addlevelrandmin\", \"lycanites_setlevel\", \"lycanites_setsubspecies\", \"lycanites_setvariant\", \"lycanites_setspawnedasboss\", \"lycanites_setbossdamagelimit\"\n" +
            "All actions can only be used in spawn.json.\n" +
            "New Rules: \"lycanites_minlevel\", \"lycanites_maxlevel\", \"lycanites_issubspecies\", \"lycanites_isminion\", \"lycanites_isuncommon\", \"lycanites_israre\", \"lycanites_isspawnedasboss\"\n" +
            "All rules can be used in any incontrol json.\n" +
            "Optifine entity constructor caching will cause duplicate actions to fire on the same entity, \"lycanites_addlevel\" is vulnerable if another mod isn't patching Optifine's behavior.")
    @Config.Name("In Control Compat - Add Actions and Rules")
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.INCONTROL_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.incontrol.json", defaultValue = false)
    @Config.RequiresMcRestart
    public boolean enableInControlCompat = FermiumRegistryAPI.isModPresent(ModLoadedUtil.INCONTROL_MODID);

    @Config.Comment("Allows love arrows breeding to apply on Lycanites animals")
    @Config.Name("Love Arrow Fix (Switch-Bow)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.SWITCHBOW_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.switchboxlovearrowfix.json", defaultValue = false)
    public boolean switchbowLoveArrowFix = FermiumRegistryAPI.isModPresent(ModLoadedUtil.SWITCHBOW_MODID);

    @Config.Comment("Fix Potion Core forcibly overwriting BaseCreatureEntity motionY ")
    @Config.Name("Potion Core Jump Fix (Potion Core)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.POTIONCORE_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.potioncorejumpfix.json", defaultValue = false)
    public boolean potionCoreJumpFix = FermiumRegistryAPI.isModPresent(ModLoadedUtil.POTIONCORE_MODID);

    @Config.Comment("Whether to affect all mobs - otherwise only LycanitesMobs entities are affected")
    @Config.Name("Potion Core Jump Fix - All Mobs")
    public boolean fixAllMobsPotionCoreJump = FermiumRegistryAPI.isModPresent(ModLoadedUtil.POTIONCORE_MODID);

    /*
     *
     * Temporary Mod Compatibility Mixins, things I should really make a PR for
     *
     */

    @Config.Comment("Cancels Custom Sweep and rehandle with RLCombat Sweep")
    @Config.Name("Crafted Equipment RLCombat Sweep (RLCombat)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.RLCOMBAT_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.rlcombatequipmentsweep.json", defaultValue = false)
    public boolean craftedEquipmentRLCombatSweep = FermiumRegistryAPI.isModPresent(ModLoadedUtil.RLCOMBAT_MODID);

    @Config.Comment("A lazy way to get the desired behavior. You should be using RLCombat's config and disabling this.")
    @Config.Name("Crafted Equipment Force Offhand Attack Whitelist (RLCombat)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.RLCOMBAT_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.rlcombatequipmentoffhandforce.json", defaultValue = false)
    public boolean craftedEquipmentForceRLCombatOffhand = FermiumRegistryAPI.isModPresent(ModLoadedUtil.RLCOMBAT_MODID);

    @Config.Comment("Makes Crafted Equipment reach stat influence ReachFix attack range")
    @Config.Name("Crafted Equipment Bonus ReachFix Range (ReachFix)")
    @Config.RequiresMcRestart
    @MixinConfig.CompatHandling(modid = ModLoadedUtil.REACHFIX_MODID, desired = true, reason = "Requires mod to properly function")
    @MixinConfig.MixinToggle(lateMixin = "mixins.lycanitestweaks.equipmentreachfix.json", defaultValue = false)
    public boolean craftedEquipmentReachFix = FermiumRegistryAPI.isModPresent(ModLoadedUtil.REACHFIX_MODID);
}
