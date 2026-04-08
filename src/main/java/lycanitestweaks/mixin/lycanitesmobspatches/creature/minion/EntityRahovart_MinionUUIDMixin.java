package lycanitestweaks.mixin.lycanitesmobspatches.creature.minion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityBehemoth;
import com.lycanitesmobs.core.entity.creature.EntityBelph;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRahovart.class)
public abstract class EntityRahovart_MinionUUIDMixin extends BaseCreatureEntity {

    @Shadow(remap = false) public List<EntityBelph> hellfireBelphMinions;
    @Shadow(remap = false) public List<EntityBehemoth> hellfireBehemothMinions;

    public EntityRahovart_MinionUUIDMixin(World world) {
        super(world);
    }

    @ModifyExpressionValue(
            method = "readFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 3)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityRahovart_readFromNBTReplaceIDBelph(boolean original){
        return false;
    }

    @ModifyExpressionValue(
            method = "readFromNBT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal = 5)
    )
    private boolean lycanitesTweaks_lycanitesMobsEntityRahovart_readFromNBTReplaceIDBehemoth(boolean original){
        return false;
    }

    @Inject(
            method = "readFromNBT",
            at = @At(value = "TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_readFromNBTMechanicsMinions(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        this.minions.forEach(minion -> {
            if(this.getBattlePhase() == 0 && minion instanceof EntityBelph)
                this.hellfireBelphMinions.add((EntityBelph) minion);
            if(this.getBattlePhase() == 1 && minion instanceof EntityBehemoth)
                this.hellfireBehemothMinions.add((EntityBehemoth) minion);
        });
    }

    @ModifyExpressionValue(
            method = "writeEntityToNBT",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/entity/creature/EntityRahovart;getBattlePhase()I", remap = false)
    )
    private int lycanitesTweaks_lycanitesMobsEntityRahovart_writeEntityToNBTSkipMinion(int original){
        return -1;
    }
}
