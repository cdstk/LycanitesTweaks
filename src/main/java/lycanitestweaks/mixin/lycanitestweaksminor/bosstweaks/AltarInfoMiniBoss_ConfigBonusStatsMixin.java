package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.AltarInfo;
import com.lycanitesmobs.core.info.altar.AltarInfoCelestialGeonach;
import com.lycanitesmobs.core.info.altar.AltarInfoCrimsonEpion;
import com.lycanitesmobs.core.info.altar.AltarInfoEbonCacodemon;
import com.lycanitesmobs.core.info.altar.AltarInfoLunarGrue;
import com.lycanitesmobs.core.info.altar.AltarInfoMottleAbaia;
import com.lycanitesmobs.core.info.altar.AltarInfoPhosphorescentChupacabra;
import com.lycanitesmobs.core.info.altar.AltarInfoRoyalArchvile;
import com.lycanitesmobs.core.info.altar.AltarInfoUmberLobber;
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
public abstract class AltarInfoMiniBoss_ConfigBonusStatsMixin {

    @Inject(
            method = "activate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", remap = true),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsAltarInfoMiniBoss_activateConfigStats(Entity entity, World world, BlockPos pos, int variant, CallbackInfoReturnable<Boolean> cir, @Local BaseCreatureEntity creature){
        if(creature.extraMobBehaviour != null) {
            AltarInfo.rareSubspeciesMutlipliers.forEach((statName, rareMultiplier) -> {
                switch (statName){
                    case "HEALTH": creature.extraMobBehaviour.multiplierHealth = rareMultiplier; break;
                    case "DEFENSE": creature.extraMobBehaviour.multiplierDefense = rareMultiplier; break;
                    case "SPEED": creature.extraMobBehaviour.multiplierSpeed = rareMultiplier; break;
                    case "DAMAGE": creature.extraMobBehaviour.multiplierDamage = rareMultiplier; break;
                    case "HASTE": creature.extraMobBehaviour.multiplierHaste = rareMultiplier; break;
                    case "EFFECT": creature.extraMobBehaviour.multiplierEffect = rareMultiplier; break;
                    case "PIERCE": creature.extraMobBehaviour.multiplierPierce = rareMultiplier; break;
                }
            });
        }
    }
}
