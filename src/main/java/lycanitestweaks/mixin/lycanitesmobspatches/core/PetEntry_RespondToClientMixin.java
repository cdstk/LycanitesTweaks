package lycanitestweaks.mixin.lycanitesmobspatches.core;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.pets.PetEntry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PetEntry.class)
public abstract class PetEntry_RespondToClientMixin {

    @Shadow(remap = false) public EntityLivingBase host;

    @Unique
    private boolean lycanitesTweaks$clientModified = false;

    @Inject(
            method = "onUpdate",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsPetEntry_onUpdateRefreshClient(World world, CallbackInfo ci){
        if(this.lycanitesTweaks$clientModified && this.host instanceof EntityPlayerMP) {
            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer((EntityPlayer) this.host);
            if(extendedPlayer != null) {
                extendedPlayer.sendPetEntryToPlayer((PetEntry)(Object)this);
            }
        }
        this.lycanitesTweaks$clientModified = false;
    }

    @Inject(
            method = "onBehaviourUpdate",
            at = @At("TAIL"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsPetEntry_onBehaviourUpdateFlagClientModified(CallbackInfo ci){
        this.lycanitesTweaks$clientModified = true;
    }
}
