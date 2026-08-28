package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.pets.PetEntry;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PetEntry.class)
public abstract class PetEntry_LateStatCalcMixin {

    @Shadow(remap = false)
    public Entity entity;

    @Inject(
            method = "spawnEntity",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/pets/PetEntry;spawnCount:I", ordinal = 0),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsPetEntry_spawnEntity(CallbackInfo ci){
        if (this.entity instanceof BaseCreatureEntity) ((BaseCreatureEntity) this.entity).refreshAttributes();
    }
}
