package lycanitestweaks.mixin.bloodmoon;

import com.llamalad7.mixinextras.sugar.Local;
import lumien.bloodmoon.server.BloodmoonSpawner;
import lycanitestweaks.compat.BloodmoonUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BloodmoonSpawner.class)
public class BloodmoonSpawner_SamplePlayersMixin {

    @Inject(
            method = "findChunksForSpawning",
            at = @At(value = "CONSTANT", args = "intValue=8"),
            remap = false
    )
    private void lycanitesTweaks_bloodmoonBloodmoonSpawner_findChunksForSpawningSaveValidPlayers(WorldServer worldServerIn, boolean spawnHostileMobs, boolean spawnPeacefulMobs, boolean spawnOnSetTickRate, CallbackInfoReturnable<Integer> cir, @Local EntityPlayer entityplayer){
        BloodmoonUtil.addExperiencing(entityplayer);
    }
}
