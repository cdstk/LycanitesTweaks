package lycanitestweaks.mixin.lycanitesmobspatches.addpotioneffectclient;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.block.effect.BlockDoomfire;
import com.lycanitesmobs.core.block.effect.BlockFrostCloud;
import com.lycanitesmobs.core.block.effect.BlockFrostfire;
import com.lycanitesmobs.core.block.effect.BlockHellfire;
import com.lycanitesmobs.core.block.effect.BlockIcefire;
import com.lycanitesmobs.core.block.effect.BlockPoisonCloud;
import com.lycanitesmobs.core.block.effect.BlockPoopCloud;
import com.lycanitesmobs.core.block.effect.BlockPrimefire;
import com.lycanitesmobs.core.block.effect.BlockScorchfire;
import com.lycanitesmobs.core.block.effect.BlockShadowfire;
import com.lycanitesmobs.core.block.fluid.BlockFluidAcid;
import com.lycanitesmobs.core.block.fluid.BlockFluidOoze;
import com.lycanitesmobs.core.block.fluid.BlockFluidPoison;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = {
        BlockDoomfire.class,
        BlockFrostCloud.class,
        BlockFrostfire.class,
        BlockHellfire.class,
        BlockIcefire.class,
        BlockPoisonCloud.class,
        BlockPoopCloud.class,
        BlockPrimefire.class,
        BlockScorchfire.class,
        BlockShadowfire.class,
        BlockFluidAcid.class,
        BlockFluidOoze.class,
        BlockFluidPoison.class
})
public abstract class BlockBaseClientAddPotionMixin {

    @WrapWithCondition(
            method = "onEntityCollision",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;addPotionEffect(Lnet/minecraft/potion/PotionEffect;)V")
    )
    public boolean lycanitesTweaks_lycanitesMobsBlockBase_onEntityCollisionClientPotion(EntityLivingBase instance, PotionEffect potionEffect){
        return !instance.getEntityWorld().isRemote;
    }
}
