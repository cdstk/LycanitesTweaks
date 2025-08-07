package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_HideNBTBossBarMixin extends EntityLiving {

    public BaseCreatureEntity_HideNBTBossBarMixin(World world) {
        super(world);
    }

    // Having issues targeting the true return value
    @ModifyReturnValue(
            method = "showBossInfo",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_showBossInfoHideDespawnable(boolean showInfo){
        return showInfo && this.isNoDespawnRequired();
    }

}
