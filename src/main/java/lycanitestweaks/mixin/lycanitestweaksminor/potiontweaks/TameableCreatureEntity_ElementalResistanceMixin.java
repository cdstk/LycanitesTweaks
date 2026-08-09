package lycanitestweaks.mixin.lycanitestweaksminor.potiontweaks;

import com.lycanitesmobs.core.entity.AgeableCreatureEntity;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigProvider;
import lycanitestweaks.util.LycanitesEntityUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TameableCreatureEntity.class)
public abstract class TameableCreatureEntity_ElementalResistanceMixin extends AgeableCreatureEntity {

    @Shadow(remap = false) public abstract EntityPlayer getPlayerOwner();

    public TameableCreatureEntity_ElementalResistanceMixin(World world) {
        super(world);
    }

    @Inject(
            method = "ownerEffects",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/TameableCreatureEntity;canBurn()Z"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsTameableCreatureEntity_ownerEffectsElementalRes(CallbackInfo ci){
        if(!this.canFreeze()) {
            Potion iceResistance = ForgeConfigProvider.getPetIceResistance();
            if(iceResistance != null)
                this.getPlayerOwner().addPotionEffect(new PotionEffect(iceResistance, (5 * 20) + 5, 1));
        }
        if(!LycanitesEntityUtil.canElectrocute(this)) {
            Potion lightningResistance = ForgeConfigProvider.getPetLightningResistance();
            if(lightningResistance != null)
                this.getPlayerOwner().addPotionEffect(new PotionEffect(lightningResistance, (5 * 20) + 5, 1));
        }
    }
}
