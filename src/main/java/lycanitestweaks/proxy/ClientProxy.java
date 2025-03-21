package lycanitestweaks.proxy;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.Item;
import lycanitestweaks.handlers.ModRegistry;
import lycanitestweaks.models.ModelExampleArmor;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends CommonProxy {

    private static final ModelExampleArmor exampleArmor = new ModelExampleArmor(1.0F);
    private static final ModelExampleArmor exampleArmorLegs = new ModelExampleArmor(0.5F);

    private static final Map<Item, ModelBiped> exampleArmorModels = new HashMap<Item, ModelBiped>();

    @Override
    public void preInit() {
        exampleArmorModels.put(ModRegistry.exampleHelmet, exampleArmor);
        exampleArmorModels.put(ModRegistry.exampleChestplate, exampleArmor);
        exampleArmorModels.put(ModRegistry.exampleLeggings, exampleArmorLegs);
        exampleArmorModels.put(ModRegistry.exampleBoots, exampleArmor);
    }

    @Override
    public Map<Item, ModelBiped> getExampleArmor() {
        return exampleArmorModels;
    }
}