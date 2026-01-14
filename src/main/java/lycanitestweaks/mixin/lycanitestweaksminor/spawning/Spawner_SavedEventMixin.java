package lycanitestweaks.mixin.lycanitestweaksminor.spawning;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.ExtendedWorld;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.mobevent.MobEventManager;
import com.lycanitesmobs.core.spawner.MobSpawn;
import com.lycanitesmobs.core.spawner.Spawner;
import lycanitestweaks.capability.lycanitestweaksplayer.ILycanitesTweaksPlayerCapability;
import lycanitestweaks.capability.lycanitestweaksplayer.LycanitesTweaksPlayerCapability;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spawner.class)
public abstract class Spawner_SavedEventMixin {

    @Shadow(remap = false) public String eventName;

    @Inject(
            method = "isEnabled",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ExtendedWorld;getForWorld(Lnet/minecraft/world/World;)Lcom/lycanitesmobs/ExtendedWorld;"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSpawner_isEnabledSavedEvent(World world, EntityPlayer player, CallbackInfoReturnable<Boolean> cir){
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null && ltp.hasSavedMobEvent(this.eventName)){
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "spawnEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ExtendedWorld;getMobEventPlayerServer(Ljava/lang/String;)Lcom/lycanitesmobs/core/mobevent/MobEventPlayerServer;", ordinal = 0),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSpawner_spawnEntitySavedEventMob(World world, ExtendedWorld worldExt, EntityLiving entityLiving, int level, MobSpawn mobSpawn, EntityPlayer player, int chain, CallbackInfo ci, @Local BaseCreatureEntity entityCreature){
        entityCreature.spawnEventType = this.eventName;
        entityCreature.spawnEventCount = -111;
    }

    @Inject(
            method = "spawnEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ExtendedWorld;getMobEventPlayerServer(Ljava/lang/String;)Lcom/lycanitesmobs/core/mobevent/MobEventPlayerServer;", ordinal = 1),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsSpawner_spawnEntitySavedEventEffects(World world, ExtendedWorld worldExt, EntityLiving entityLiving, int level, MobSpawn mobSpawn, EntityPlayer player, int chain, CallbackInfo ci){
        ILycanitesTweaksPlayerCapability ltp = LycanitesTweaksPlayerCapability.getForPlayer(player);
        if(ltp != null && ltp.hasSavedMobEvent(this.eventName)){
            MobEventManager.getInstance()
                    .getMobEvent(this.eventName)
                    .onSpawn(entityLiving, world, player, player.getPosition(), level, ltp.getRemainingEventDuration(this.eventName), -1);
        }
    }
}
