package lycanitestweaks.client.renderer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class RenderContext {

    public static EntityLivingBase currentRenderEntity = null;
    public static ItemStack currentRenderStack = null;
}
