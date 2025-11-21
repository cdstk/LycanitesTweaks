package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_ProjectileUseAttributeMixin extends BaseCreatureEntity{

    public TameableCreatureEntity_ProjectileUseAttributeMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "doRangedDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/CreatureStats;getDamage()D"),
            remap = false
    )
    private double lycanitesTweaks_lycanitesMobsTameableCreatureEntity_doRangedDamageWithDamageAttribute(double original){
        return this.getAttackDamage(1);
    }
}
