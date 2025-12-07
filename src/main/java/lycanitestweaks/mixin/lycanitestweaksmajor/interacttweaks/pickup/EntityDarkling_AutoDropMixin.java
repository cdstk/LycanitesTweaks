package lycanitestweaks.mixin.lycanitestweaksmajor.interacttweaks.pickup;

import com.lycanitesmobs.core.entity.TameableCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityDarkling;
import lycanitestweaks.util.Helpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDarkling.class)
public abstract class EntityDarkling_AutoDropMixin extends TameableCreatureEntity {

    @Shadow(remap = false)
    public abstract boolean hasLatchTarget();
    @Shadow(remap = false)
    public abstract EntityLivingBase getLatchTarget();
    @Shadow(remap = false)
    public abstract void setLatchTarget(EntityLivingBase entity);

    public EntityDarkling_AutoDropMixin(World world) {
        super(world);
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityDarkling;hasLatchTarget()Z", ordinal = 1, remap = false)
    )
    private void lycanitesTweaks_lycanitesMobsEntityDarkling_onLivingUpdatePickupRange(CallbackInfo ci){
        if(this.hasLatchTarget()) {
            EntityLivingBase latchTarget = this.getLatchTarget();
            if (this.getDistance(latchTarget) > Helpers.getAutoDropPickupDistance(this, latchTarget)) {
                this.setPosition(this.posX, this.posY + 3, this.posZ); // Required to not send darkling into the ground
                this.setLatchTarget(null);
            }
        }
    }
}
