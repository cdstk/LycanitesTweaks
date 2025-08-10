package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks;

import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityIgnibus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = {
//        EntityEyewig.class,
        EntityIgnibus.class
//        EntityIoray.class
})
public abstract class RideableCreatureEntity_RapidProjectileAbilityScaleMixin extends RideableCreatureEntity {

    public RideableCreatureEntity_RapidProjectileAbilityScaleMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "mountAbility",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/RapidFireProjectileEntity;<init>(Lcom/lycanitesmobs/core/info/projectile/ProjectileInfo;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;II)V", ordinal = 0),
            index = 2,
            remap = false
    )
    public EntityLivingBase lycanitesTweaks_EntityIgnibus_mountAbilityLevelScaleProjectile(EntityLivingBase entityLivingBase){
        return this;
    }
}
