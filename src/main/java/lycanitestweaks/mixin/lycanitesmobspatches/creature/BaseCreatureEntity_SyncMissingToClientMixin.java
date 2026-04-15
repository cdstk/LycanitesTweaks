package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.network.PacketCreaturePropertiesSync;
import lycanitestweaks.network.PacketHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_SyncMissingToClientMixin extends EntityLiving {

    @Shadow(remap = false) public abstract BossInfo getBossInfo();
    @Shadow(remap = false) public BossInfoServer bossInfo;
    @Shadow(remap = false) public boolean spawnedAsBoss;

    public BaseCreatureEntity_SyncMissingToClientMixin(World world) {
        super(world);
    }

    @WrapWithCondition(
            method = "refreshAttributes",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;setHealth(F)V")
    )
    private boolean lycanitesTweaks_lycanitesMobsBaseCreatureEntity_refreshAttributesSideCheck(BaseCreatureEntity instance, float v){
        return !this.world.isRemote;
    }

    // TODO Retest the entire chain, this solves Boss Info not consistently using Custom Names
    // TODO Make getDisplayName and getName usages consistent with Vanilla
    @ModifyArg(
            method = "refreshBossHealthName",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BossInfoServer;setName(Lnet/minecraft/util/text/ITextComponent;)V")
    )
    private ITextComponent lycanitesTweaks_lycanitesMobsBaseCreatureEntity_refreshBossHealthNameCustom(ITextComponent nameIn){
        return this.hasCustomName() ? this.getDisplayName() : nameIn;
    }

    // Random rare variant boss bar sync for first spawning
    @Inject(
            method = "onFirstSpawn",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingRandomRareBoss(CallbackInfo ci){
        if(this.getBossInfo() != null && this.world instanceof WorldServer){
            ((WorldServer) this.world).getEntityTracker().getTrackingPlayers(this).forEach(trackingPlayer -> {
                if(trackingPlayer instanceof EntityPlayerMP) this.bossInfo.addPlayer((EntityPlayerMP) trackingPlayer);
            });
        }
    }

    // Boss bar sync when changing at runtime, such as nbt changes
    @Inject(
            method = "setVariant",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_setVariantRareBoss(int variantIndex, CallbackInfo ci){
        if(this.getBossInfo() != null && this.world instanceof WorldServer){
            ((WorldServer) this.world).getEntityTracker().getTrackingPlayers(this).forEach(trackingPlayer -> {
                if(trackingPlayer instanceof EntityPlayerMP) this.bossInfo.addPlayer((EntityPlayerMP) trackingPlayer);
            });
        }
    }

    // Boss Bar Name from /summon or Spawner
    @Inject(
            method = "addTrackingPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BossInfoServer;addPlayer(Lnet/minecraft/entity/player/EntityPlayerMP;)V")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingPlayerBossInfo(EntityPlayerMP player, CallbackInfo ci){
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    // Extra Mob Behavior NBT sync
    @Inject(
            method = "addTrackingPlayer",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_addTrackingPlayerSyncMissing(EntityPlayerMP player, CallbackInfo ci){
        PacketHandler.instance.sendTo(new PacketCreaturePropertiesSync((BaseCreatureEntity)(Object)this), player);
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_readEntityFromNBTSyncMissing(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        if (this.hasCustomName() && this.getBossInfo() != null) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Unique
    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        if(this.getBossInfo() != null)
            this.bossInfo.setName(this.getDisplayName());
    }
}
