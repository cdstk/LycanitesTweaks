package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.info.altar.AltarInfoAmalgalich;
import com.lycanitesmobs.core.info.altar.AltarInfoAsmodeus;
import com.lycanitesmobs.core.info.altar.AltarInfoRahovart;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {
        AltarInfoAmalgalich.class,
        AltarInfoAsmodeus.class,
        AltarInfoRahovart.class
})
public abstract class AltarInfoMainBoss_ConsumeSoulcubeMixin  {

    @WrapMethod(
            method = "activate",
            remap = false
    )
    private boolean lycanitesTweaks_lycanitesMobsAltarInfoMainBoss_activateSoulcubeConsume(Entity entity, World world, BlockPos pos, int variant, Operation<Boolean> original){
        boolean result = original.call(entity, world, pos, variant);
        if(result) world.setBlockToAir(pos);
        return result;
    }
}
