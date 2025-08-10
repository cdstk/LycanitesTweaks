package lycanitestweaks.mixin.lycanitestweaksmajor.mainbosstweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseProjectileEntity;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireWall;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.potion.PotionVoided;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHellfireWall.class)
public abstract class EntityHellfireWallTweaksMixin extends BaseProjectileEntity {

    public EntityHellfireWallTweaksMixin(World world) {
        super(world);
    }

    @ModifyArg(
            method = "setup",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/projectile/EntityHellfireWall;setDamage(I)V"),
            remap = false
    )
    public int lycanitesTweaks_lycanitesMobsEntityHellfireWall_setupDamage(int damage){
        return ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttacksBaseDamage;
    }

    @Inject(
            method = "setup",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityHellfireWall_setupKnockback(CallbackInfo ci){
        this.knockbackChance = ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttacksKnockbaceChance;
    }

    @ModifyExpressionValue(
            method = "onDamage",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/info/ObjectLists;inEffectList(Ljava/lang/String;Lnet/minecraft/potion/Potion;)Z"),
            remap = false
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityHellfireWall_onDamageAnyPurge(boolean original, @Local PotionEffect potionEffect){
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttackPurgeAnyBuff)
            return !potionEffect.getPotion().isBadEffect();
        return original;
    }

    @Inject(
            method = "onDamage",
            at = @At("RETURN"),
            remap = false
    )
    public void lycanitesTweaks_lycanitesMobsEntityHellfireWall_onDamageAddVoided(EntityLivingBase target, float damage, boolean attackSuccess, CallbackInfo ci){
        if(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttackVoidedTime > 0  && ForgeConfigHandler.server.effectsConfig.registerVoided)
            target.addPotionEffect(new PotionEffect(PotionVoided.INSTANCE, this.getEffectDuration(ForgeConfigHandler.majorFeaturesConfig.rahovartConfig.hellfireAttackVoidedTime), 0));
    }
}
