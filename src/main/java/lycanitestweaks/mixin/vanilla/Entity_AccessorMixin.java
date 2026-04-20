package lycanitestweaks.mixin.vanilla;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface Entity_AccessorMixin {

    @Accessor(value = "fire")
    int lycanitesTweaks$getFireTicks();
}
