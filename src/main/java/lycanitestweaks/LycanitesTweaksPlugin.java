package lycanitestweaks;

import fermiumbooter.FermiumRegistryAPI;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.ForgeConfigProvider;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class LycanitesTweaksPlugin implements IFMLLoadingPlugin {

	public LycanitesTweaksPlugin() {
		MixinBootstrap.init();

		//		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.client.bigchildheadall.json"); // funny but clearly broken

		// Always
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.client.rainbow.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.core.itemswithcreatureinfo.json");

		// Conditional based on config
		FermiumRegistryAPI.enqueueMixin(true, "mixins.lycanitestweaks.feature.spawnedasbossrngname.json", () -> ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.spawnedAsBossNaturalSpawnNames > 0);

		if(ForgeConfigHandler.integrationConfig.removeDuplicateMixins){
			if(ForgeConfigHandler.mixinPatchesConfig.fixAsmodeusMinions)
				FermiumRegistryAPI.removeMixin("mixins.fermiummixins.late.lycanitesmobs.minion.json");
			if(ForgeConfigHandler.mixinPatchesConfig.customItemEntityRegistryPatch)
				FermiumRegistryAPI.removeMixin("mixins.fermiummixins.late.lycanitesmobs.customitem.json");
			if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.equipmentEnchantments)
				FermiumRegistryAPI.removeMixin("mixins.rlmixins.late.lycanitesmobs.equipmentenchantments.json");
		}

		ForgeConfigProvider.pluginInit();
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