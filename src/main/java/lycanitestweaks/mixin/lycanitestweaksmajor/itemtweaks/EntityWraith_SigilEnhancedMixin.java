package lycanitestweaks.mixin.lycanitestweaksmajor.itemtweaks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityWraith;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import lycanitestweaks.handlers.ForgeConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityWraith.class)
public abstract class EntityWraith_SigilEnhancedMixin extends TameableCreatureEntity {

    @Unique
    ElementInfo lycanitesTweaks$addVoiding = null;

    public EntityWraith_SigilEnhancedMixin(World world) {
        super(world);
    }

    @WrapWithCondition(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityWraith;attackEntityAsMob(Lnet/minecraft/entity/Entity;D)Z", remap = false)
    )
    public boolean lycanitesTweaks_lycanitesMobsEntityWraith_onLivingUpdateSigilMelee(EntityWraith instance, Entity target, double damageScale){
        instance.attackMelee(target, ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.wraithSigilDamageScale);
        if(ForgeConfigHandler.majorFeaturesConfig.itemTweaksConfig.wraithSigilVoiding && target instanceof EntityLivingBase) {
            // DO NOT ADD TO ELEMENT LIST, Perma affects all instances
            if (this.lycanitesTweaks$addVoiding == null) this.lycanitesTweaks$addVoiding = ElementManager.getInstance().getElement("voiding");
            if (this.lycanitesTweaks$addVoiding != null) {
                this.lycanitesTweaks$addVoiding.debuffEntity(
                        (EntityLivingBase) target,
                        this.getEffectDuration(1),
                        this.getEffectAmplifier(1)
                );
            }
        }
        return false;
    }
}
