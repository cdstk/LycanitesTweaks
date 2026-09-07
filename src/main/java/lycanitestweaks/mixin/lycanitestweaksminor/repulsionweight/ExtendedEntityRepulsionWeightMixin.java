package lycanitestweaks.mixin.lycanitestweaksminor.repulsionweight;

import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedEntity;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExtendedEntity.class)
public abstract class ExtendedEntityRepulsionWeightMixin {

    @Shadow(remap = false)
    public EntityLivingBase entity;
    @Shadow(remap = false)
    public Entity pickedUpByEntity;
    @Shadow(remap = false)
    public abstract void setPickedUpByEntity(Entity pickedUpByEntity);

    @Inject(
            method = "updatePickedUpByEntity",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/ObjectManager;getEffect(Ljava/lang/String;)Lcom/lycanitesmobs/PotionBase;"),
            cancellable = true,
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsExtendedEntity_updatePickedUpByEntity(CallbackInfo ci){
        Potion repulsion = ObjectManager.getEffect("repulsion");
        if (repulsion != null && this.entity.isPotionActive(repulsion)) {
            if(this.pickedUpByEntity instanceof BaseCreatureEntity) {
                BaseCreatureEntity creature = (BaseCreatureEntity) this.pickedUpByEntity;
                if(!creature.creatureInfo.dummy || !ForgeConfigHandler.minorFeaturesConfig.fearMoreThings) {
                    creature.dropPickupEntity();
                }
            }
            else this.setPickedUpByEntity(null);
            ci.cancel();
        }
    }
}
