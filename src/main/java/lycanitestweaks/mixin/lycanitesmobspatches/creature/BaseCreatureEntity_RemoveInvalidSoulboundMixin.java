package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseCreatureEntity.class)
public abstract class BaseCreatureEntity_RemoveInvalidSoulboundMixin extends EntityLiving {

    public BaseCreatureEntity_RemoveInvalidSoulboundMixin(World world) {
        super(world);
    }

    @Shadow(remap = false) public abstract void setMinion(boolean minion);
    @Shadow(remap = false) public abstract void setTemporary(int duration);

    @Inject(
            method = "readEntityFromNBT",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/BaseCreatureEntity;setDead()V"),
            cancellable = true
    )
    private void lycanitestweaks_lycanitesMobsBaseCreatureEntity_readEntityFromNBTDespawnSoulbound(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        // Schedule removal
        this.setMinion(true); // Flag no drops
        this.setTemporary(0); // Remove next tick
        super.readEntityFromNBT(nbtTagCompound);
        ci.cancel(); // Don't load inventory
    }
}
