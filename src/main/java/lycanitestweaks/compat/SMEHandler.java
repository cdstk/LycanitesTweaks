package lycanitestweaks.compat;

import com.shultrea.rin.util.Types;
import net.minecraft.enchantment.Enchantment;

import java.util.Set;

public abstract class SMEHandler {

    public static boolean doesEquipmentHaveType(Enchantment enchantment, Set<String> featureTypeSet){
        if(enchantment.type == Types.AXE && featureTypeSet.contains("axe")) return true;
        if(enchantment.type == Types.PICKAXE && featureTypeSet.contains("pickaxe")) return true;
        if(enchantment.type == Types.HOE && featureTypeSet.contains("hoe")) return true;
        if(enchantment.type == Types.SPADE && featureTypeSet.contains("shovel")) return true;
        return false;
    }
}
