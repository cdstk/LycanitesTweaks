package lycanitestweaks.mixin.lycanitestweaksminor.aitweaks;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_FearMoreMixin {

    @Shadow(remap = false) public CreatureInfo creatureInfo;

    @ModifyVariable(
            method = "canPickupEntity",
            at = @At("STORE"),
            name = "heavyTarget",
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityDummyTargetHeavy(boolean heavyTarget, EntityLivingBase victim){
        if(this.creatureInfo.dummy) {
            return false; // Mechanic such as fear
        }
        return heavyTarget;
    }

    @Definition(id = "EntityBoat", type = EntityBoat.class)
    @Expression("? instanceof EntityBoat")
    @ModifyExpressionValue(
            method = "canPickupEntity",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityDummyBoat(boolean isBoat, EntityLivingBase entity){
        if(this.creatureInfo.dummy) {
            return !(entity.getRidingEntity() instanceof EntityLivingBase);
        }
        else {
            return isBoat;
        }
    }

    @Definition(id = "EntityMinecart", type = EntityMinecart.class)
    @Expression("? instanceof EntityMinecart")
    @ModifyExpressionValue(
            method = "canPickupEntity",
            at = @At("MIXINEXTRAS:EXPRESSION"),
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityDummyMinecart(boolean isMinecart){
        return !this.creatureInfo.dummy && isMinecart; // Mechanic such as fear
    }

    @ModifyExpressionValue(
            method = "canPickupEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z")
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canPickupEntityDummyTargetPotions(boolean isPotionActive){
        return !this.creatureInfo.dummy && isPotionActive; // Mechanic such as fear
    }
}
