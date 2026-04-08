package lycanitestweaks.mixin.lycanitesmobspatches.creature;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.creature.EntityRahovart;
import com.lycanitesmobs.core.entity.projectile.EntityHellfireBarrier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(EntityRahovart.class)
public abstract class EntityRahovart_SaveBarriersMixin extends BaseCreatureEntity {

    @Shadow(remap = false) public List<EntityHellfireBarrier> hellfireBarriers;
    @Shadow(remap = false) public abstract void hellfireBarrierAttack(double angle);

    @Unique
    private static final String BARRIER_ROTATIONS_NBT = "BarrierRotations";
    @Unique
    private final Set<Double> lycanitesTweaks$barriersFromNBT = new HashSet<>();

    public EntityRahovart_SaveBarriersMixin(World world) {
        super(world);
    }

    @Inject(
            method = "hellfireBarrierUpdate",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_hellfireBarrierUpdateCreateFromNBT(CallbackInfo ci){
        if(!this.lycanitesTweaks$barriersFromNBT.isEmpty()){
            this.lycanitesTweaks$barriersFromNBT.forEach(this::hellfireBarrierAttack);
            this.lycanitesTweaks$barriersFromNBT.clear();
        }
    }

    @Inject(
            method = "hellfireBarrierCleanup",
            at = @At("HEAD"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_hellfireBarrierCleanupCached(CallbackInfo ci){
        this.lycanitesTweaks$barriersFromNBT.clear();
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_readFromNBTBarriers(NBTTagCompound nbtTagCompound, CallbackInfo ci){
        if (nbtTagCompound.hasKey(BARRIER_ROTATIONS_NBT)) {
            NBTTagList barrierIDs = nbtTagCompound.getTagList(BARRIER_ROTATIONS_NBT, 10);

            for(int i = 0; i < barrierIDs.tagCount(); ++i) {
                NBTTagCompound rotationTag = barrierIDs.getCompoundTagAt(i);
                if(rotationTag.hasKey("Rotation")){
                    this.lycanitesTweaks$barriersFromNBT.add(rotationTag.getDouble("Rotation"));
                }
            }
        }
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void lycanitesTweaks_lycanitesMobsEntityRahovart_writeEntityToNBTBarriers(NBTTagCompound nbtTagCompound, CallbackInfo ci) {
        if(!this.hellfireBarriers.isEmpty()) {
            NBTTagList barrierIDs = new NBTTagList();
            for(EntityHellfireBarrier entity : this.hellfireBarriers) {
                NBTTagCompound rotation = new NBTTagCompound();
                rotation.setDouble("Rotation", entity.rotation);
                barrierIDs.appendTag(rotation);
            }
            nbtTagCompound.setTag(BARRIER_ROTATIONS_NBT, barrierIDs);
        }
    }
}
