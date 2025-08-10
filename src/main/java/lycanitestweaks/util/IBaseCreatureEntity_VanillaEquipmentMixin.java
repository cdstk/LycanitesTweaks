package lycanitestweaks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;

import java.util.HashMap;
import java.util.Map;

public interface IBaseCreatureEntity_VanillaEquipmentMixin {

    // I am sorry it has to be this way
    Map<String, DataParameter<ItemStack>> handDataParameters = new HashMap<>();
}
