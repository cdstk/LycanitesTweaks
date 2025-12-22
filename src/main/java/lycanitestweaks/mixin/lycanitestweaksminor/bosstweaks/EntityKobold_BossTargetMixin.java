package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityKobold;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityKobold.class)
public abstract class EntityKobold_BossTargetMixin extends TameableCreatureEntity {

    public EntityKobold_BossTargetMixin(World world) {
        super(world);
    }

    @Inject(
            method = "shouldCreatureGroupRevenge",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityKobold_shouldCreatureGroupRevengeBoss(EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        if(this.isBoss()) cir.setReturnValue(super.shouldCreatureGroupRevenge(target));
    }

    @Inject(
            method = "shouldCreatureGroupHunt",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityKobold_shouldCreatureGroupHuntBoss(EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        if(this.isBoss()) cir.setReturnValue(super.shouldCreatureGroupHunt(target));
    }

    @Inject(
            method = "shouldCreatureGroupFlee",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityKobold_shouldCreatureGroupFleeBoss(EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        if(this.isBoss()) cir.setReturnValue(super.shouldCreatureGroupFlee(target));
    }

    @Inject(
            method = "canAttackEntity",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityKobold_canAttackEntityBoss(EntityLivingBase target, CallbackInfoReturnable<Boolean> cir){
        if(this.isBoss()) cir.setReturnValue(super.canAttackEntity(target));
    }
}
