package lycanitestweaks.mixin.vanilla;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLiving.class)
public interface EntityLiving_InvokerMixin {

    @Accessor(value = "experienceValue")
    int lycanitesTweaks$getExperienceValue();

    @Invoker(value = "getAmbientSound")
    SoundEvent lycanitesTweaks$invokeGetAmbientSound();
}
