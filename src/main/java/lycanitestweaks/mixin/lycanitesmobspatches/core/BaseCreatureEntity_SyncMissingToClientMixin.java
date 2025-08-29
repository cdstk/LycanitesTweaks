package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.network.PacketCreaturePropertiesSync;
import lycanitestweaks.network.PacketHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_SyncMissingToClientMixin extends EntityLiving {

    @Shadow(remap = false) public boolean firstSpawn;

    @Shadow(remap = false) public abstract void onFirstSpawn();

    public BaseCreatureEntity_SyncMissingToClientMixin(World world) {
        super(world);
    }

    // Random rare variant boss bar sync
    @Inject(
            method = "addTrackingPlayer",
            at = @At("HEAD")
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingRandomRareBoss(EntityPlayerMP player, CallbackInfo ci){
        if(this.firstSpawn) this.onFirstSpawn();
    }

    // Extra Mob Behavior NBT sync
    @Inject(
            method = "addTrackingPlayer",
            at = @At("TAIL")
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingPlayerSyncMissing(EntityPlayerMP player, CallbackInfo ci){
        PacketHandler.instance.sendTo(new PacketCreaturePropertiesSync((BaseCreatureEntity)(Object)this), player);
    }
}
