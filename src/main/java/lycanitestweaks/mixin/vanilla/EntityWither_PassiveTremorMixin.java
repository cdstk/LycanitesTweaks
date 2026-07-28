package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.boss.EntityWither$1")
public abstract class EntityWither_PassiveTremorMixin {

    @ModifyReturnValue(
            method = "apply*",
            at = @At("RETURN")
    )
    public boolean lycanitesTweaks_vanillaEntityWitherPredicate_applyNotTremor(boolean original, @Local(argsOnly = true) Entity target){
        return original && !LycanitesMobsWrapper.isTremor(target);
    }
}
