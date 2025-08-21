package lycanitestweaks.mixin.lycanitestweaksclient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.PotionBase;
import com.lycanitesmobs.PotionEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PotionEffects.class)
public abstract class PotionEffects_InsomniaMessageMixin {

    @ModifyArg(
            method = "onSleep",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/player/PlayerSleepInBedEvent;setResult(Lnet/minecraft/entity/player/EntityPlayer$SleepResult;)V"),
            remap = false
    )
    public EntityPlayer.SleepResult lycanitesTweaks_lycanitesMobsPotionEffects_onSleepInsomniaMessage(EntityPlayer.SleepResult result, @Local EntityPlayer player, @Local PotionBase insomnia){
        player.sendStatusMessage(new TextComponentTranslation("tile.bed.lycanites.insomnia", new TextComponentTranslation(insomnia.getName())), true);
        return EntityPlayer.SleepResult.OTHER_PROBLEM;
    }
}
