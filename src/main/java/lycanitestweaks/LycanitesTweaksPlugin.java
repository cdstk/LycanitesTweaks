package lycanitestweaks;

import fermiumbooter.FermiumRegistryAPI;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LycanitesTweaksPlugin implements IFMLLoadingPlugin {

	public LycanitesTweaksPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.registerAnnotatedMixinConfig(ForgeConfigHandler.class, null);

		// Mod Compat - This way doesn't render the error message if people use the default
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.featureclientbeastiaryddd.json",
		FermiumRegistryAPI.isModPresent("distinctdamagedescriptions") && ForgeConfigHandler.integrationConfig.beastiaryGUIDDD);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.switchboxlovearrowfix.json",
		FermiumRegistryAPI.isModPresent("switchbow") && ForgeConfigHandler.integrationConfig.switchbowLoveArrowFix);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.potioncorejumpfix.json",
		FermiumRegistryAPI.isModPresent("potioncore") && ForgeConfigHandler.integrationConfig.potionCoreJumpFix);
		//
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.equipmentreachfix.json",
				FermiumRegistryAPI.isModPresent("reachfix") && ForgeConfigHandler.integrationConfig.craftedEquipmentReachFix);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.equipmentrlcombatsweep.json",
				FermiumRegistryAPI.isModPresent("bettercombatmod") && ForgeConfigHandler.integrationConfig.craftedEquipmentRLCombatSweep);
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}