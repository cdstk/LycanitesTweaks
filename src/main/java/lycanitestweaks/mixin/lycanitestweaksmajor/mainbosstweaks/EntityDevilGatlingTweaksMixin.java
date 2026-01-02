package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityDevilGatling;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.potion.PotionVoided;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDevilGatling.class)
public abstract class EntityDevilGatlingTweaksMixin extends BaseProjectileEntity {

    public EntityDevilGatlingTweaksMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/projectile/EntityDevilGatling;setDamage(I)V"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityDevilGatling_setupDamage(int damage){
        return ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilGatlingBaseDamage;
    }

    @ModifyExpressionValue(
            method = "onDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ObjectLists;inEffectList(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityDevilGatling_onDamageAnyPurge(boolean original, @Local Potion potion){
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilGatlingPurgeAnyBuff)
            return !potion.isBadEffect();
        return original;
    }

    @Inject(
            method = "onDamage",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityDevilGatling_onDamageAddVoided(EntityLivingBase target, float damage, boolean attackSuccess, CallbackInfo ci){
        if(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilGatlingVoidedTime > 0  && ForgeConfigHandler.server.effectsConfig.registerVoided)
            target.addPotionEffect(new PotionEffect(PotionVoided.INSTANCE, this.getEffectDuration(ForgeConfigHandler.majorFeaturesConfig.asmodeusConfig.devilGatlingVoidedTime), 0));
    }
}
