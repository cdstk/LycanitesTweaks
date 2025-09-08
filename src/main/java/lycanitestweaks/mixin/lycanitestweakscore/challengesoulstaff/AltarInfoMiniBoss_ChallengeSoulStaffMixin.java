package lycanitestweaks.mixin.lycanitestweakscore.challengesoulstaff;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.ItemDrop;
import com.lycanitesmobs.core.info.altar.AltarInfoCelestialGeonach;
import com.lycanitesmobs.core.info.altar.AltarInfoCrimsonEpion;
import com.lycanitesmobs.core.info.altar.AltarInfoEbonCacodemon;
import com.lycanitesmobs.core.info.altar.AltarInfoLunarGrue;
import com.lycanitesmobs.core.info.altar.AltarInfoMottleAbaia;
import com.lycanitesmobs.core.info.altar.AltarInfoPhosphorescentChupacabra;
import com.lycanitesmobs.core.info.altar.AltarInfoRoyalArchvile;
import com.lycanitesmobs.core.info.altar.AltarInfoUmberLobber;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.LycanitesTweaksRegistry;
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
}, priority = 1001)
public abstract class AltarInfoMiniBoss_ChallengeSoulStaffMixin {

    @Inject(
            method = "activate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", remap = true),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsAltarInfoMiniBoss_activateSoulStaffDrop(Entity entity, World world, BlockPos pos, int variant, CallbackInfoReturnable<Boolean> cir, @Local BaseCreatureEntity creature){
        if(variant == 0 && ForgeConfigHandler.server.customStaffConfig.challengeStaffUncommon) return;

        ItemDrop staffDrop = new ItemDrop(LycanitesTweaksRegistry.challengeSoulStaff.getRegistryName().toString(), 0, 1F);
        staffDrop.bonusAmount = false;
        staffDrop.amountMultiplier = false;
        creature.addSavedItemDrop(staffDrop);
    }
}
