package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.llamalad7.mixinextras.sugar.Local;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        EntityAmalgalich.class,
        EntityAsmodeus.class,
        EntityRahovart.class
})
public abstract class BaseCreatureEntitys_BossUnderArenaMixin extends BaseCreatureEntity {

    public BaseCreatureEntitys_BossUnderArenaMixin(World world) {
        super(world);
    }

    @Inject(
            method = "isDamageEntityApplicable",
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/EntityPlayer;capabilities:Lnet/minecraft/entity/player/PlayerCapabilities;"),
            cancellable = true
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntitys_isDamageEntityApplicablePlayerUnderArena(Entity entity, CallbackInfoReturnable<Boolean> cir, @Local EntityPlayer player){
        if(!player.canEntityBeSeen(this)) {
            if (!player.capabilities.disableDamage && player.posY + player.height < this.posY) {
                cir.setReturnValue(false);
            }
        }
    }
}
