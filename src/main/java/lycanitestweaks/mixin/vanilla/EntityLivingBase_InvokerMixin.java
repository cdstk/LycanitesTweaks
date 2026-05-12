package lycanitestweaks.mixin.vanilla;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLivingBase.class)
public interface EntityLivingBase_InvokerMixin {

    @Invoker(value = "getExperiencePoints")
    int lycanitesTweaks$invokeGetExperiencePoints(EntityPlayer player);
}
