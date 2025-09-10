package lycanitestweaks.mixin.srparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.PotionBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityNak.class)
public abstract class EntityNak_RepulsionMixin extends EntityPStationary {

    public EntityNak_RepulsionMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;isEntityAlive()Z")
    )
    public boolean lycanitesTweaks_srparasitesEntityNak_onLivingUpdateRepulsion(boolean isEntityAlive){
        PotionBase repulsion = ObjectManager.getEffect("repulsion");
        if(repulsion != null) {
            return isEntityAlive && !this.getAttackTarget().isPotionActive(repulsion);
        }
        return isEntityAlive;
    }
}
