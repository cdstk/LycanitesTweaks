package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.altar.AltarInfoCelestialGeonach;
import com.lycanitesmobs.core.info.altar.AltarInfoCrimsonEpion;
import com.lycanitesmobs.core.info.altar.AltarInfoEbonCacodemon;
import com.lycanitesmobs.core.info.altar.AltarInfoLunarGrue;
import com.lycanitesmobs.core.info.altar.AltarInfoMottleAbaia;
import com.lycanitesmobs.core.info.altar.AltarInfoPhosphorescentChupacabra;
import com.lycanitesmobs.core.info.altar.AltarInfoRoyalArchvile;
import com.lycanitesmobs.core.info.altar.AltarInfoUmberLobber;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
public abstract class AltarInfoMiniBoss_SpawnedAsBossMixin {

    @Inject(
            method = "activate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;applyVariant(I)V"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsAltarInfo_activateSpawnedAsBossNBT(Entity entity, World world, BlockPos pos, int variant, CallbackInfoReturnable<Boolean> cir, @Local BaseCreatureEntity altarBoss){
        if(variant == 0 && ForgeConfigHandler.majorFeaturesConfig.creatureStatsConfig.altarMiniNBTBossUncommon) return;
        altarBoss.spawnedAsBoss = true;
    }
}
