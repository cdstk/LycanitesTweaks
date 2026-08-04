package lycanitestweaks.mixin.lycanitestweaksminor.bosstweaks;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAmalgalich;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import com.lycanitesmobs.core.info.CreatureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
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
public abstract class BaseCreatureEntitys_BossFarPetDmgMixin extends BaseCreatureEntity {

    public BaseCreatureEntitys_BossFarPetDmgMixin(World world) {
        super(world);
    }

    @Inject(
            method = "isDamageEntityApplicable",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsBaseCreatureEntitys_isDamageEntityApplicableFarPetOwner(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(entity instanceof IEntityOwnable) {
            Entity owner = ((IEntityOwnable) entity).getOwner();
            if(owner instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) owner;

                if(player.posY > this.posY + CreatureManager.getInstance().config.bossAntiFlight) {
                    player.sendStatusMessage(new TextComponentTranslation("boss.damage.protection.range"), true);
                    cir.setReturnValue(false);
                }

                if(!player.canEntityBeSeen(this)) {
                    if(player.posY > this.posY + this.height || player.posY < this.posY) {
                        player.sendStatusMessage(new TextComponentTranslation("boss.damage.protection.range"), true);
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
