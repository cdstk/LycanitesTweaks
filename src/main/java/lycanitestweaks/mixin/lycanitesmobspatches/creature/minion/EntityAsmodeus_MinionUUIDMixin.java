package lycanitestweaks.mixin.lycanitesmobspatches.creature.minion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityAsmodeus;
import com.lycanitesmobs.core.entity.creature.EntityAstaroth;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityAsmodeus.class)
public abstract class EntityAsmodeus_MinionUUIDMixin extends BaseCreatureEntity {

    @Shadow(remap = false) public List<EntityAstaroth> astarothMinions;

    public EntityAsmodeus_MinionUUIDMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "readFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 3)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityAsmodeus_readFromNBTReplaceID(boolean original){
        return false;
    }

    @Inject(
            method = "readFromNBT",
            at = @At(value = "TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityAsmodeus_readFromNBTMechanicsMinions(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        this.minions.forEach(minion -> {
            if(this.getBattlePhase() > 0 && minion instanceof EntityAstaroth)
                this.astarothMinions.add((EntityAstaroth) minion);
        });
    }

    @ModifyExpressionValue(
            method = "writeEntityToNBT",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityAsmodeus;getBattlePhase()I", remap = false)
    )
    private int lycanitesTweaks_lycanitesMobsEntityAsmodeus_writeEntityToNBTSkipMinion(int original){
        return -1;
    }
}
