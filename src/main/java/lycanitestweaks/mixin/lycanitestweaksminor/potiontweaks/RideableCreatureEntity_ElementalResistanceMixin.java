package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.RideableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RideableCreatureEntity.class)
public abstract class RideableCreatureEntity_ElementalResistanceMixin extends TameableCreatureEntity {

    public RideableCreatureEntity_ElementalResistanceMixin(World world) {
        super(world);
    }

    @Inject(
            method = "riderEffects",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsRideableCreatureEntity_riderEffectsElementalRes(CallbackInfo ci, @Local(argsOnly = true) EntityLivingBase rider){
        // Protect Rider from Potion Effects:
        if(!this.canFreeze()) {
            Potion iceResistance = ForgeConfigProvider.getPetIceResistance();
            if(iceResistance != null)
                rider.addPotionEffect(new PotionEffect(iceResistance, (5 * 20) + 5, 1));
        }
        if(!LycanitesEntityUtil.canElectrocute(this)) {
            Potion lightningResistance = ForgeConfigProvider.getPetLightningResistance();
            if(lightningResistance != null)
                rider.addPotionEffect(new PotionEffect(lightningResistance, (5 * 20) + 5, 1));
        }
    }
}
