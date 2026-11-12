package lycanitestweaks.mixin.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.util.LycanitesMobsWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBase_OwnableCreditMixin {

    @Shadow protected int recentlyHit;
    @Shadow protected EntityPlayer attackingPlayer;

    @ModifyExpressionValue(
            method = "attackEntityFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DamageSource;getTrueSource()Lnet/minecraft/entity/Entity;")
    )
    private Entity lycanitesTweaks_vanillaEntityLivingBase_attackEntityFromLycanitesTame(Entity original){
        if(LycanitesMobsWrapper.getLycanitesEntityOwner(original) instanceof EntityPlayer) {
            this.recentlyHit = 100;
            this.attackingPlayer = null;
        }
        else if(original instanceof IEntityOwnable && ForgeConfigHandler.minorFeaturesConfig.vanillaTameableDmgCreditAll) {
            IEntityOwnable ownable = (IEntityOwnable) original;
            if(ownable.getOwner() instanceof EntityPlayer) {
                this.recentlyHit = 100;
                this.attackingPlayer = null;
            }
        }
        return original;
    }
}
