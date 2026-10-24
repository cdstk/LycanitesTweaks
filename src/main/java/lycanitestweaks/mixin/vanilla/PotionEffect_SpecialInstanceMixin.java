package lycanitestweaks.mixin.vanilla;

import lycanitestweaks.potion.SpecialPotionEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PotionEffect.class)
public abstract class PotionEffect_SpecialInstanceMixin {

    @ModifyArg(
            method = "readCustomPotionEffectFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionEffect;readCurativeItems(Lnet/minecraft/potion/PotionEffect;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/potion/PotionEffect;", remap = false)
    )
    private static PotionEffect lycanitesTweaks_vanillaPotionEffect_readCustomPotionEffectFromNBTSpecialInstance(PotionEffect effect, NBTTagCompound nbt){
        boolean isSpecial = nbt.hasKey(SpecialPotionEffect.CLEANSE_NBT)
                || nbt.hasKey(SpecialPotionEffect.PURGE_NBT)
                || nbt.hasKey(SpecialPotionEffect.NEUTRAL_NBT);

        if(isSpecial) {
            SpecialPotionEffect specialPotionEffect = new SpecialPotionEffect(effect);
            if(nbt.hasKey(SpecialPotionEffect.CLEANSE_NBT)) {
                specialPotionEffect.setCleansable(nbt.getBoolean(SpecialPotionEffect.CLEANSE_NBT));
            }
            if(nbt.hasKey(SpecialPotionEffect.PURGE_NBT)) {
                specialPotionEffect.setPurgeable(nbt.getBoolean(SpecialPotionEffect.PURGE_NBT));
            }
            if(nbt.hasKey(SpecialPotionEffect.NEUTRAL_NBT)) {
                specialPotionEffect.setNeutral(nbt.getBoolean(SpecialPotionEffect.NEUTRAL_NBT));
            }
            effect = specialPotionEffect;
        }

        return effect;
    }
}
