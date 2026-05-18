package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRahovart.class)
public abstract class EntityRahovart_BossMinionMendMixin extends BaseCreatureEntity {

    public EntityRahovart_BossMinionMendMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onMinionDeath",
            at = @At(value = "RETURN", ordinal = 0),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_onMinionDeathBelpSuper(EntityLivingBase minion, DamageSource damageSource, CallbackInfo ci){
        super.onMinionDeath(minion, damageSource);
    }

    @Inject(
            method = "onMinionDeath",
            at = @At(value = "RETURN", ordinal = 1),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_onMinionDeathBehemothSuper(EntityLivingBase minion, DamageSource damageSource, CallbackInfo ci){
        super.onMinionDeath(minion, damageSource);
    }

}
