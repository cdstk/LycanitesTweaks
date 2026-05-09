package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableCreatureEntity.class)
public abstract class AgeableCreatureEntity_TemporaryBabyMixin extends BaseCreatureEntity {

    public AgeableCreatureEntity_TemporaryBabyMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onCreateBaby",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsAgeableCreatureEntity_onCreateBabyTemporaryMinion(AgeableCreatureEntity partner, AgeableCreatureEntity baby, CallbackInfo ci){
        if(this.isTemporary || partner.isTemporary) {
            baby.setTemporary(Math.max(this.temporaryDuration, partner.temporaryDuration));
        }
        baby.setMinion(this.isMinion() || partner.isMinion());

        // Credit to the mind who was Soul Binding these things
        if(baby.isTemporary && baby.isMinion()) {
            baby.setCustomNameTag("Enraged Chicken Spawn");
        }
    }
}
