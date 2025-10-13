package lycanitestweaks.mixin.lycanitestweaksclient;

import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.item.special.ItemSoulgazer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ItemSoulgazer.class)
public abstract class ItemSoulgazer_TooltipMixin extends ItemBase {

    @Unique
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(GuiScreen.isShiftKeyDown()) {
            super.addInformation(stack, worldIn, tooltip, flagIn);
        }
        else {
            tooltip.add(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
        }
    }
}