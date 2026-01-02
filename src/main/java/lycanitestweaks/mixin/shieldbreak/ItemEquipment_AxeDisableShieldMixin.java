package lycanitestweaks.mixin.shieldbreak;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ItemEquipment.class)
public abstract class ItemEquipment_AxeDisableShieldMixin extends ItemBase {

    @Shadow(remap = false) public abstract Set<String> getToolClasses(ItemStack itemStack);

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker){
        return this.getToolClasses(stack).contains("axe") || super.canDisableShield(stack, shield, entity, attacker);
    }
}
