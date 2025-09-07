package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.network.PacketCreaturePropertiesSync;
import lycanitestweaks.network.PacketHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_SyncMissingToClientMixin extends EntityLiving {

    @Shadow(remap = false) public abstract boolean isRareVariant();
    @Shadow(remap = false) public abstract BossInfo getBossInfo();
    @Shadow(remap = false) public BossInfoServer bossInfo;

    public BaseCreatureEntity_SyncMissingToClientMixin(World world) {
        super(world);
    }

    // Random rare variant boss bar sync
    @Inject(
            method = "onFirstSpawn",
            at = @At("TAIL"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingRandomRareBoss(CallbackInfo ci){
        if(this.isRareVariant() && this.getBossInfo() != null &&  this.world instanceof WorldServer){
            ((WorldServer) this.world).getEntityTracker().getTrackingPlayers(this).forEach(trackingPlayer -> {
                if(trackingPlayer instanceof EntityPlayerMP) this.bossInfo.addPlayer((EntityPlayerMP) trackingPlayer);
            });
        }
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
