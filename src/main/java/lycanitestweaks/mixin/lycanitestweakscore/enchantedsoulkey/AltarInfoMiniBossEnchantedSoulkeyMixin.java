package lycanitestweaks.mixin.lycanitestweakscore.enchantedsoulkey;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.altar.*;
import lycanitestweaks.item.ItemEnchantedSoulkey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
public abstract class AltarInfoMiniBossEnchantedSoulkeyMixin {

    @Inject(
            method = "activate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", remap = true),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsAltarInfoMiniBoss_activateSoulkeyLevel(Entity entity, World world, BlockPos pos, int variant, CallbackInfoReturnable<Boolean> cir, @Local BaseCreatureEntity creature){
        if(entity instanceof EntityPlayer) {
            ItemStack itemStack = ((EntityPlayer) entity).getHeldItemMainhand();
            if(itemStack.getItem() instanceof ItemEnchantedSoulkey) {
                creature.enablePersistence();
                creature.setFixateTarget((EntityLivingBase)entity);
                creature.spawnedAsBoss = true;
                creature.addLevel(((ItemEnchantedSoulkey) itemStack.getItem()).getLevel(itemStack) - 1);
            }
        }
    }
}
