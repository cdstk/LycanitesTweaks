package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.pets.PetEntry;
import com.lycanitesmobs.core.pets.SummonSet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PetEntry.class)
public abstract class PetEntry_TeleportMixin {

    @Shadow(remap = false) public Entity entity;
    @Shadow(remap = false) public SummonSet summonSet;
    @Shadow(remap = false) public abstract void despawnEntity();

    @Inject(
            method = "onUpdate",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/pets/PetEntry;entity:Lnet/minecraft/entity/Entity;", ordinal = 0),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsPetEntry_onUpdateResetUnloadedPet(World world, CallbackInfo ci){
        // Unloaded Check:
        if (this.entity != null && !this.entity.isAddedToWorld()) {
            this.despawnEntity();
            if (this.summonSet.playerExt != null)
                this.summonSet.playerExt.sendPetEntryToPlayer((PetEntry)(Object)this);
        }
    }
}
