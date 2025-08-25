package lycanitestweaks.mixin.claimit;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lycanitesmobs.core.info.altar.AltarInfoCelestialGeonach;
import com.lycanitesmobs.core.info.altar.AltarInfoCrimsonEpion;
import com.lycanitesmobs.core.info.altar.AltarInfoEbonCacodemon;
import com.lycanitesmobs.core.info.altar.AltarInfoLunarGrue;
import com.lycanitesmobs.core.info.altar.AltarInfoMottleAbaia;
import com.lycanitesmobs.core.info.altar.AltarInfoPhosphorescentChupacabra;
import com.lycanitesmobs.core.info.altar.AltarInfoRoyalArchvile;
import com.lycanitesmobs.core.info.altar.AltarInfoUmberLobber;
import dev.itsmeow.claimit.api.claim.ClaimManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        AltarInfoCelestialGeonach.class,
        AltarInfoCrimsonEpion.class,
        AltarInfoEbonCacodemon.class,
        AltarInfoLunarGrue.class,
        AltarInfoMottleAbaia.class,
        AltarInfoPhosphorescentChupacabra.class,
        AltarInfoRoyalArchvile.class,
        AltarInfoUmberLobber.class
})
public abstract class AltarInfoMiniBoss_ClaimItMixin {

    @ModifyReturnValue(
            method = "quickCheck",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsAltarInfo_quickCheckClaimIt(boolean original, Entity entity, World world, BlockPos pos){
        return original && !ClaimManager.getManager().isBlockInAnyClaim(world, pos);
    }

    @ModifyReturnValue(
            method = "fullCheck",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsAltarInfo_fullCheckClaimIt(boolean original, Entity entity, World world, BlockPos pos){
        return original && !ClaimManager.getManager().isBlockInAnyClaim(world, pos);
    }
}
