package lycanitestweaks.mixin.lycanitesmobspatches.creature.pickup;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.FearEntity;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FearEntity.class)
public abstract class FearEntity_PickupMixin extends BaseCreatureEntity {

    public FearEntity_PickupMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/FearEntity;canPickupEntity(Lnet/minecraft/entity/EntityLivingBase;)Z", remap = false)
    )
    private boolean lycanitesTweaks_lycanitesMobsFearEntity_onLivingUpdateCheckRange(boolean original, @Local EntityLivingBase fearedEntityLiving){
        return original && this.getDistance(fearedEntityLiving) <= LycanitesEntityUtil.getAutoDropPickupDistance(this, fearedEntityLiving);
    }
}
