package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExtendedEntity.class)
public abstract class ExtendedEntity_FearMoreMixin {

    @Shadow(remap = false) public Entity pickedUpByEntity;

    @WrapWithCondition(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/ExtendedEntity;setPickedUpByEntity(Lnet/minecraft/entity/Entity;)V", ordinal = 2),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntityFearWeight(ExtendedEntity instance, Entity message){
        if(this.pickedUpByEntity instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) this.pickedUpByEntity;
            if(creature.creatureInfo.dummy) {
                return false;
            }
        }
        return true;
    }
}
