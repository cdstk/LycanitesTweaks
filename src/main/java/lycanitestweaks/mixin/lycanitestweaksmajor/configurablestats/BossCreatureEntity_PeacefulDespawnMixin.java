package lycanitestweaks.mixin.lycanitestweaksmajor.configurablestats;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.info.CreatureInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseCreatureEntity.class)
public abstract class BossCreatureEntity_PeacefulDespawnMixin {

    @Shadow(remap = false)
    public CreatureInfo creatureInfo;
    @Shadow(remap = false)
    public boolean spawnedAsBoss;

    @Inject(
           method = "canDespawn",
           at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/info/CreatureSpawn;despawnNatural:Z", remap = false),
           cancellable = true
   )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_canDespawnPeacefulBosses(CallbackInfoReturnable<Boolean> cir){
        if(this.creatureInfo.peaceful && this.spawnedAsBoss) cir.setReturnValue(true);
   }
}
