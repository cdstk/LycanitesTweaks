package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBeholder;
import com.lycanitesmobs.core.entity.creature.EntityCacodemon;
import com.lycanitesmobs.core.entity.creature.EntityErepede;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
        EntityBeholder.class,
        EntityCacodemon.class,
        EntityErepede.class,
        EntityIgnibus.class
})
public abstract class RideableCreatureEntity_AbilityToggleMixin extends RideableCreatureEntity {

    @Unique
    public boolean lycanitesTweaks$overrideAbilityToggle;

    public RideableCreatureEntity_AbilityToggleMixin(World world) {
        super(world);
    }

    @Inject(
            method = "mountAbility",
            at = @At("HEAD"),
            remap = false
    )
    public void lycanitesTweaks_RideableCreatureEntity_mountAbilityRapidFireOn(Entity rider, CallbackInfo ci){
        this.lycanitesTweaks$overrideAbilityToggle = this.abilityToggled;
        this.abilityToggled = false;
    }

    @Inject(
            method = "mountAbility",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_RideableCreatureEntity_mountAbilityRapidFireOff(Entity rider, CallbackInfo ci){
        this.abilityToggled = this.lycanitesTweaks$overrideAbilityToggle;
    }
}
