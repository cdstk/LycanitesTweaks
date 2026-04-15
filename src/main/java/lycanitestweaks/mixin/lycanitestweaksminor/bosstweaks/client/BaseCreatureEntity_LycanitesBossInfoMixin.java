package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.network.PacketHandler;
import lycanitestweaks.network.PacketLycanitesBossInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Client Side request on EntityJoinWorldEvent too early for conditional bosses
@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_LycanitesBossInfoMixin extends EntityLiving {

    @Shadow(remap = false) public boolean spawnedAsBoss;

    public BaseCreatureEntity_LycanitesBossInfoMixin(World world) {
        super(world);
    }

    @Inject(
            method = "setVariant",
            at = @At(value = "FIELD", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;damageLimit:F"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_setVariantBossInfo(int variantIndex, CallbackInfo ci){
        if(this.hasCustomName() && ForgeConfigHandler.clientFeaturesMixinConfig.otherBossInfoOverlay)
            PacketHandler.instance.sendToServer(new PacketLycanitesBossInfo(this));
    }

    @WrapMethod(
            method = "onSyncUpdate",
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntity_onSyncUpdateBossInfo(Operation<Void> original) {
        boolean wasBoss = this.spawnedAsBoss;
        original.call();
        if(!wasBoss && this.spawnedAsBoss && ForgeConfigHandler.clientFeaturesMixinConfig.otherBossInfoOverlay) {
            PacketHandler.instance.sendToServer(new PacketLycanitesBossInfo(this));
        }
    }
}
