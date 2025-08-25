package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
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
public abstract class AltarInfoMiniBoss_ForgeEventMixin {

    @ModifyReturnValue(
            method = "quickCheck",
            at = @At("RETURN"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsAltarInfo_quickCheckForgeGriefEvent(boolean original, Entity entity, World world, BlockPos pos){
        if(original && entity instanceof EntityPlayer) return ForgeEventFactory.onEntityDestroyBlock((EntityPlayer) entity, pos, world.getBlockState(pos));
        return original;
    }

    @WrapWithCondition(
            method = "activate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockToAir(Lnet/minecraft/util/math/BlockPos;)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsAltarInfo_activateForgeGriefEvent(World instance, BlockPos pos, @Local(argsOnly = true) Entity entity, @Local BaseCreatureEntity altarBoss){
        if(entity instanceof EntityPlayer) return ForgeEventFactory.onEntityDestroyBlock((EntityPlayer) entity, pos, instance.getBlockState(pos));
        return ForgeEventFactory.onEntityDestroyBlock(altarBoss, pos, instance.getBlockState(pos));
    }
}
